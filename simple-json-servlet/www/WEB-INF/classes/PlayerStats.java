import org.json.*;

public class PlayerStats {
  private int kills;
  private int deaths;
  private int assists;
  private String player;
  
  public PlayerStats(int kills, int deaths, int assists, String player) {
    this.kills = kills;
    this.deaths = deaths;
    this.assists = assists;
    this.player = player;
  }

  public String toJson() {
    try {
      JSONObject jo = new JSONObject();
      jo.put("player", player);
      jo.put("kills", kills);
      jo.put("deaths", deaths);
      jo.put("assists", assists);
      return jo.toString(2);
    } catch (JSONException e) {
      throw new RuntimeException("Error: " + e.toString());
    }
  }
}
