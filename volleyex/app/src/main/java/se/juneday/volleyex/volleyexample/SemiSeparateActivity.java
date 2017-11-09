package se.juneday.volleyex.volleyexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import se.juneday.volleyex.volleyexample.SemiVolleyMember.MemberChangeListener;


public class SemiSeparateActivity extends AppCompatActivity {

  private static final String LOG_TAG = SemiSeparateActivity.class.getSimpleName();

  private ArrayAdapter<Member> adapter;
  private ListView listview;
  private List<Member> members;
  private MemberChangeListener listener;
  private SemiVolleyMember semiVolleyMember;
  private SemiSeparateActivity me;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_volley);

    me = this;

    listener = new MemberChangeListener() {
      @Override
      public void onMemberChangeList(List<Member> members) {
        resetListView(members);
        ActivitySwitcher.showToast(me, "Members updated");
      }
    };

    semiVolleyMember = SemiVolleyMember.getInstance(this, listener);
    members = new ArrayList<>();
    resetListView(members);

    ((TextView)findViewById(R.id.label)).setText(LOG_TAG);
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

  public void updateClick(View view) {
    semiVolleyMember.getMembers();
    ActivitySwitcher.showToast(this, "Updating members");
  }

  @Override
  public void onStart() {
    super.onStart();
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



}
