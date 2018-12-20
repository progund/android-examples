import javax.servlet.*;
import javax.servlet.http.*;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.io.IOException;

public class StatsServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    PrintWriter out =
      new PrintWriter(new OutputStreamWriter(response.getOutputStream(),
                                             UTF_8), true);
    Stats stats = new Stats();
    String player = request.getParameter("player");
    StringBuilder sb = new StringBuilder();
    response.setContentType("application/json; charset=" + UTF_8.name());
    if (player == null) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      sb.append("{ \"error\" : \"missing parameter player\"}");
      out.println(sb.toString());
      return;
    }
    sb.append(stats.getStatsFor(player).toJson());
    out.println(sb.toString());
    out.close();
  }
}
