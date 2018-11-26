package se.juneday.volleyex.volleyexample;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class VolleyActivity extends AppCompatActivity {

  private static final String LOG_TAG = VolleyActivity.class.getSimpleName();

  private ArrayAdapter<Member> adapter;
  private ListView listview;
  private List<Member> members;
  private VolleyActivity me;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_volley);

    members = new ArrayList<>();
    listview = (ListView) findViewById(R.id.volley_list);
    adapter = new ArrayAdapter<>(this,
        android.R.layout.simple_list_item_1, members);

    listview.setAdapter(adapter);

    me = this;
    ((TextView) findViewById(R.id.label)).setText(LOG_TAG);
  }

  private void resetListView() {
    listview = (ListView) findViewById(R.id.volley_list);
    adapter = new ArrayAdapter<>(this,
        android.R.layout.simple_list_item_1, members);
    listview.setAdapter(adapter);
  }

  @Override
  public void onStart() {
    super.onStart();

    getMembers();

  }


  private List<Member> jsonToMembers(JSONArray array) {
    List<Member> memberList = new ArrayList<>();
    for (int i = 0; i < array.length(); i++) {
      try {
        JSONObject row = array.getJSONObject(i);
        String name = row.getString("name");
        String email = row.getString("email");

        Member m = new Member(name, email);
        memberList.add(m);
      } catch (JSONException e) {
        ; // is ok since this is debug
      }
    }
    return memberList;
  }

  // The code below is "slightly" (nudge nudge) based on:
  //   https://developer.android.com/training/volley/request.html
  private void getMembers() {

    RequestQueue queue = Volley.newRequestQueue(this);

    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
        Request.Method.GET,
        Settings.url,
        null,
        new Response.Listener<JSONArray>() {

          @Override
          public void onResponse(JSONArray array) {
            members = jsonToMembers(array);
            resetListView();
            ActivitySwitcher.showToast(me, "Members updated");

          }
        }, new Response.ErrorListener() {

      @Override
      public void onErrorResponse(VolleyError error) {
        Log.d(LOG_TAG, " cause: " + error.getCause().getMessage());
        ActivitySwitcher.showToast(me, "Members update failed");
      }
    });

    // Add the request to the RequestQueue.
    queue.add(jsonArrayRequest);

  }


  /* Common */
  public void volleyActivityClick(View view) {
    ActivitySwitcher.switchToVolleyActivity(this);
  }

  public void semiSeparateClick(View view) {
    ActivitySwitcher.switchToSemiSeparateActivity(this);
  }

  public void separateClick(View view) {
    ActivitySwitcher.switchToSeparateActivity(this);
  }

  public void updateClick(View view) {
    ActivitySwitcher.showToast(this, "Updating members");
    getMembers();
  }

}
