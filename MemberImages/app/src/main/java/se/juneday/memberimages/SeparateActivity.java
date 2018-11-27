package se.juneday.memberimages;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import se.juneday.memberimages.domain.Member;


public class SeparateActivity extends AppCompatActivity {

  private static final String LOG_TAG = SeparateActivity.class.getSimpleName();

  private MemberAdapter adapter;
  private ListView listview;
  private List<Member> members;
  private SeparateActivity me;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_volley);

    members = new ArrayList<>();
    resetListView(members);

    me = this;

    // register to listen to member updates in VolleyMember
    VolleyMember.getInstance(this).addMemberChangeListener(new VolleyMember.MemberChangeListener() {
      @Override
      public void onMemberChangeList(List<Member> members) {
        resetListView(members);
        SeparateActivity.this.members = members;
        ActivityHelper.showToast(me, "Members updated");
      }


      @Override
      public void onAvatarChange(Member member, Bitmap bitmap) {
        Log.d(LOG_TAG, "avatar Change on: " + bitmap);
          try {
              File f = Utils.createImageFile(SeparateActivity.this, member, bitmap);
              Log.d(LOG_TAG, " created file: " + f);

              updateImageView(member, bitmap);

          } catch (IOException e) {
              Log.d(LOG_TAG, " failed created file: " + e);
              e.printStackTrace();
          }
      }

    });

    Log.d(LOG_TAG, " onCreate()");
  }

    private void updateImageView(Member member, Bitmap bitmap) {
        if ( (member==null) || (bitmap==null) ) {
            return;
        }
        int index = members.indexOf(member);
        Log.d(LOG_TAG, "  index of: " + index + "  of " + members.size() + " members");

        View child = listview.getChildAt(index);
        if (child==null) {
            Log.d(LOG_TAG, " child null");
            return;
        }
        ImageView iv = child.findViewById(R.id.avatar);
        if (iv != null) {
            iv.setImageBitmap(bitmap);
        }
    }


    private void resetListView(List<Member> members) {
      Log.d(LOG_TAG, " resetListView() : " + members);
    listview = (ListView) findViewById(R.id.volley_list);
    adapter = new MemberAdapter(members, this);

    listview.setAdapter(adapter);
  }


    private void resetListView(){
      Log.d(LOG_TAG, " resetListView()");
    listview = (ListView) findViewById(R.id.volley_list);
    adapter = new MemberAdapter(members, this);
    listview.setAdapter(adapter);
  }

  @Override
  public void onStart() {
    super.onStart();
      ActivityHelper.showToast(this, "Updating members");
    VolleyMember.getInstance(this).getMembers();

      AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              Member member= (Member) members.get((int)l);
              Log.d(LOG_TAG, "Member " + member.name() + " clicked  (" + i + "|" + l + ")");
          }
      };
    listview.setOnItemClickListener(listener);

  }

  public void updateClick(View view) {
    ActivityHelper.showToast(this, "Updating members");
    VolleyMember.getInstance(this).getMembers();
  }



}
