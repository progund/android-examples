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

import se.juneday.ObjectCache;
import se.juneday.android.AndroidObjectCacheHelper;
import se.juneday.android.AndroidObjectCacheHelper.AndroidObjectCacheHelperException;
import se.juneday.memberimages.domain.Member;


public class MemberActivity extends AppCompatActivity {

  // String tag for logging
  private static final String LOG_TAG = MemberActivity.class.getSimpleName();

  // Adapter to create Member Views for us
  private MemberAdapter adapter;

  // ListView to display the Views
  private ListView listview;

  // Our model or data, a list of members
  private List<Member> members;

  /* ObjectCache
  private ObjectCache<Member> cache;
  */

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_volley);

    // Empty aray
    members = new ArrayList<>();

    /* ObjectCache
    String fileName =
        null;
    try {
      fileName = AndroidObjectCacheHelper.objectCacheFileName(this, Member.class);
    } catch (AndroidObjectCacheHelperException e) {
      e.printStackTrace();
    }
    cache = new ObjectCache<>(fileName);
    members = (List<Member>) cache.readObjects();
    Log.d(LOG_TAG, "cache:  reading objects: " + members.size());
   */

    resetListView();

    Log.d(LOG_TAG, " onCreate()");

  }

  private void updateImageView(Member member, Bitmap bitmap) {
    // if invalid in data, return
    if ((member == null) || (bitmap == null)) {
      return;
    }

    // Find index in the list for the member
    int index = members.indexOf(member);
    Log.d(LOG_TAG, "  index of: " + index + "  of " + members.size() + " members");

    // Get the coresponding view
    View child = listview.getChildAt(index);
    if (child == null) {
      // view null, return
      Log.d(LOG_TAG, " child null");
      return;
    }

    // Get the avatar view (in the specific Member view)
    ImageView iv = child.findViewById(R.id.avatar);
    if (iv == null) {
      // view null, return
      return;
    }

    // Set the bitmap in the imageview
    iv.setImageBitmap(bitmap);
  }

  // Method to refresh the listview
  private void resetListView() {
    Log.d(LOG_TAG, " resetListView() : " + members);
    listview = (ListView) findViewById(R.id.volley_list);
    adapter = new MemberAdapter(members, this);
    listview.setAdapter(adapter);
  }

  @Override
  public void onStart() {
    super.onStart();

    // register to listen to member updates in VolleyMember
    VolleyMember.getInstance(this).addMemberChangeListener(new VolleyMember.MemberChangeListener() {

      @Override
      public void onMemberChangeList(List<Member> members) {
        // reset listview with new members, update member (instance variable)
        MemberActivity.this.members = members;
        resetListView();

        /* ObjectCache
        Log.d(LOG_TAG, "cache: Storing objects: " + members.size() + " who called?");
        cache.storeObjects(members);
        */

        // Show toast to inform new data is displayed
        ActivityHelper.showToast(MemberActivity.this, "Members updated");
      }


      @Override
      public void onAvatarChange(Member member, Bitmap bitmap) {
        Log.d(LOG_TAG, "avatar Change on: " + bitmap);

        // From the bitmap we should create a file
        try {
          // Create a file from the bitmap
          File f = Utils.createImageFile(MemberActivity.this, member, bitmap);
          Log.d(LOG_TAG, " created file: " + f);
        } catch (IOException e) {
          // Since we failed creating the file, we don't need to remove any
          Log.d(LOG_TAG, " failed created file: " + e);
          e.printStackTrace();
          return;
        }

        // if we failed creating a file e should not display the bitmap
        //    (and give the use the impression all is fine)
        // No need to check since we have already returned if the creation failed
        updateImageView(member, bitmap);
      }

    });
    // finished adding listener

    // Inform user we're trying to get a fresh list of Members
    ActivityHelper.showToast(this, "Updating members");
    // .. and then actually do try to get a fresh list of Members
    VolleyMember.getInstance(this).getMembers();

    // for the fun of (and to show you it is possible), register a listener
    // if the user clicks on any of the member views in the listview
    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Member member = (Member) members.get((int) l);
        Log.d(LOG_TAG, "Member " + member.name() + " clicked  (" + i + "|" + l + ")");
      }
    });

  }

}
