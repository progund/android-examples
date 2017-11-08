package se.juneday.volleyex.volleyexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import se.juneday.volleyex.volleyexample.VolleyMember.MemberChangeListener;


public class SeparateActivity extends AppCompatActivity {

  private static final String LOG_TAG = SeparateActivity.class.getName();

  private ArrayAdapter<Member> adapter;
  private ListView listview;
  private List<Member> members;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_volley);

    members = new ArrayList<>();
    resetListView(members);

    // register to listen to member updates in VolleyMember
    VolleyMember.getInstance(this).addMemberChangeListener(new MemberChangeListener() {
      @Override
      public void onMemberChangeList(List<Member> members) {
        resetListView(members);

      }
    });

  }

  private void resetListView(List<Member> members) {
    listview = (ListView) findViewById(R.id.volley_list);
    adapter = new ArrayAdapter<>(this,
        android.R.layout.simple_list_item_1, members);

    listview.setAdapter(adapter);
  }

  private void resetListView(){
    listview = (ListView) findViewById(R.id.volley_list);
    adapter = new ArrayAdapter<>(this,
        android.R.layout.simple_list_item_1, members);
    listview.setAdapter(adapter);
  }

  @Override
  public void onStart() {
    super.onStart();
  }

  public void switchClick(View view) {
    Intent intent = new Intent(this, VolleyActivity.class);
    startActivity(intent);
  }

  public void updateClick(View view) {
    VolleyMember.getInstance(this).getMembers();
  }



}
