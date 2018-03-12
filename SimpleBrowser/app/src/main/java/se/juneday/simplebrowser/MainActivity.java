package se.juneday.simplebrowser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

  private static String defaultUrl = "http://10.0.2.2:8080/database-servlet";
  private static final String LOG_TAG = MainActivity.class.getSimpleName() ;
  private TextView tv;
  private EditText et;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    tv = findViewById(R.id.studentsView);
    et = findViewById(R.id.urlView);
    et.setText(defaultUrl);
    startDownload(defaultUrl);
  }

  private void startDownload(String url) {
    Log.d(LOG_TAG, "url: " + url);

    RequestQueue queue = Volley.newRequestQueue(this);


// Request a string response from the provided URL.
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
        new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            // Display the first 500 characters of the response string.
            setContent(response);
          }
        }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        tv.setText("That didn't work!");
      }
    });

// Add the request to the RequestQueue.
    queue.add(stringRequest);
  }

  @Override
  public void onStart() {
    super.onStart();
//    setContent("<html><body>lksdjflkj<br><pre>code</pre><h1>h1</h1><br><h2>h2</h2><br>asdasd</body></html>");
  }

  public void setContent(String html) {
    tv.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT));
  }

  public void getUrl(View view) {
    startDownload(et.getText().toString());
  }
}
