package se.juneday.gitrepoviewer.network;


import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import se.juneday.gitrepoviewer.domain.Repository;
import se.juneday.gitrepoviewer.util.JsonParser;

public class VolleyRepositoryFetcher {

  private static final String LOG_TAG = VolleyRepositoryFetcher.class.getName();
  private static VolleyRepositoryFetcher fetcher;
  private Context context;
  private List<RepositioryChangeListener> listeners;


  public static synchronized VolleyRepositoryFetcher getInstance(Context context) {
    if (fetcher == null) {
      fetcher = new VolleyRepositoryFetcher(context);
    }
    return fetcher;
  }

  private VolleyRepositoryFetcher(Context context) {
    listeners = new ArrayList<>();
    this.context = context;
  }


  public void getRepositories() {
    RequestQueue queue = Volley.newRequestQueue(context);

    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
        Request.Method.GET,
        "https://api.github.com/orgs/progund/repos?per_page=400",
        null,
        new Response.Listener<JSONArray>() {

          @Override
          public void onResponse(JSONArray array) {
            Log.d(LOG_TAG, " got data from Volley");
            List<Repository> repos = JsonParser.parse(array);
            for (RepositioryChangeListener r : listeners) {
              r.onRepositoryChange(repos);
            }
          }
        }, new Response.ErrorListener() {

      @Override
      public void onErrorResponse(VolleyError error) {
        Log.d(LOG_TAG, " cause: " + error.getCause().getMessage());
      }

    });

    // Add the request to the RequestQueue.
    queue.add(jsonArrayRequest);
  }


  /******************************************
   RepositioryChangeListener
   ******************************************/


  public interface RepositioryChangeListener {

    void onRepositoryChange(List<Repository> repos);
  }

  public void addRepositoryChangeListener(RepositioryChangeListener l) {
    listeners.add(l);

  }

}