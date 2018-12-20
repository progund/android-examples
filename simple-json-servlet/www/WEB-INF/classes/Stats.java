import java.util.Map;
import java.util.HashMap;
import java.sql.*;

public class Stats {
  private Map<String, PlayerStats> stats;
  
  public PlayerStats getStatsFor(String player) {
    /*
    stats.put("player1", new PlayerStats(10,10,10, "player1"));
    stats.put("player2", new PlayerStats(20,20,20, "player2"));
    stats.put("player3", new PlayerStats(30,30,30, "player3"));
    */
    if (stats == null) {
      getStats();
    }
    return stats.get(player);
  }

  private void getStats() {
    stats = new HashMap<>();
    try {
      Connection c = DriverManager.getConnection("jdbc:sqlite:playerstats.db");
      Statement stm = c.createStatement();
      ResultSet rs = stm.executeQuery("select name, kills, deaths, assists from player,stats" +
                                      " JOIN has_stats ON has_stats.player_id = player.id" +
                                      " and has_stats.stats_id = stats.id;");
      while (rs.next()) {
        stats.put(rs.getString("name"), new PlayerStats(rs.getInt("kills"),
                                                       rs.getInt("deaths"),
                                                       rs.getInt("assists"),
                                                       rs.getString("name"))
                  );
      }
    } catch (SQLException e) {
      System.err.println("Error: " + e.getMessage());
      throw new RuntimeException ("Error reading from db: " + e.getMessage());
    }
  }
}
