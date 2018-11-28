package se.juneday.memberimages;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.juneday.memberimages.domain.Member;

public class VolleyMember {

  // String tag for logging
  private static final String LOG_TAG = VolleyMember.class.getName();

  // This is a singleton class, so this is THE one and only instance
  private static VolleyMember volleyMember;

  // Context, needed to find views etc
  private Context context;

  /**
   * Method to get hold of the only instance
   * @param context - used to create (if needed) the only instance
   * @return the one and only instance
   */
  public static synchronized VolleyMember getInstance(Context context) {
    if (volleyMember == null) {
      volleyMember = new VolleyMember(context);
    }
    Log.d(LOG_TAG, "getInstance()");
    return volleyMember;
  }

  // Private constructor to prevent intantiation
  private VolleyMember(Context context) {
    listeners = new ArrayList<>();
    this.context = context;
  }

  // parses a JSON array and returns a list of Members
  private List<Member> jsonToMembers(JSONArray array) {
    Log.d(LOG_TAG, "jsonToMembers: " + array);

    // Create an empty arraylist
    List<Member> memberList = new ArrayList<>();

    // Loop through the elements in the array
    for (int i = 0; i < array.length(); i++) {
      Log.d(LOG_TAG, " parse JSON i: " + i);
      try {
        // Extract name, email and avatarUrl
        JSONObject row = array.getJSONObject(i);
        String name = row.getString("name");
        String email = row.getString("email");
        String avatarUrl = row.getString("avatar");
        Log.d(LOG_TAG, name +  " " + email  + "   : " + avatarUrl);
        //Create a new Member and add to the list
        Member m = new Member(name, email, avatarUrl);
        memberList.add(m);
      } catch (JSONException e) {
        ; // is ok since this is debug
        Log.d(LOG_TAG, "JSON parse: " + e);
      }
    }
    return memberList;
  }


  /**
   * Get members from server. Inform listeners when finished.
  * The code below is "slightly" (nudge nudge) based on:
  *   https://developer.android.com/training/volley/request.html
   */
  public void getMembers() {
    Log.d(LOG_TAG, "getMembers()");
    RequestQueue queue = Volley.newRequestQueue(context);

    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
        Request.Method.GET,
        Settings.url,
        null,
        new Response.Listener<JSONArray>() {

          @Override
          public void onResponse(JSONArray array) {
            // Convert JSON array to List of Members
            List<Member> members = jsonToMembers(array);

            Log.d(LOG_TAG, " response from server: " + members.size());

            // For each listeners inform about new List of members
            for (MemberChangeListener m : listeners) {
              m.onMemberChangeList(members);
            }

            // For each member, manage avatar
            for (Member m : members) {
              // Check if avatar file exists (already downloaded and stored)
              if (!Utils.avatarExists(context, m)) {
                // avatar does not exist, invoke volley code to download it
                Log.d(LOG_TAG, "  download avatar for " + m.name());
                VolleyMember.getInstance(context).fetchAvatar(m);
              } else {
                // already exists, skip download
                Log.d(LOG_TAG, "  avatar already exists for " + m.name());
              }
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
    Log.d(LOG_TAG, " json request queued");
  }

  /**
   * Downloads avatar for a Member
   * @param member - member  to download avatar for
   */
  public void fetchAvatar(final Member member) {
    Log.d(LOG_TAG, "getAvatars()");
    RequestQueue queue = Volley.newRequestQueue(context);
    Log.d(LOG_TAG, "  url: " + member.avatarUrl());
    String url = member.avatarUrl();

    // if no url for member, simply do nada
    if ( (url == null) || url.equals ("null") ) {
//      url = "https://avatars0.githubusercontent.com/u/19474334?s=400&u=1ade95c4770d096ec33107c05b51c99cfdd6ab01&v=4";
      return;
    }
    Log.d(LOG_TAG, "download url: " + url);

    ImageRequest imageRequest = new ImageRequest(url,
            new Response.Listener<Bitmap>() {
              @Override
              public void onResponse(Bitmap bitmap) {
                Log.d(LOG_TAG, "onResponse ok: " + bitmap.toString());
                for (MemberChangeListener m : listeners) {
                  m.onAvatarChange(member, bitmap);
                }
              }
            }, 0, 0, ImageView.ScaleType.CENTER,
            Bitmap.Config.RGB_565,
            new Response.ErrorListener() {
              public void onErrorResponse(VolleyError error) {
                Log.d(LOG_TAG, "onResponse fail");
              }
            });

    // Add the request to the RequestQueue.
    queue.add(imageRequest);
  }

  /******************************************
   MemberChangeListener
   ******************************************/

  // internal list of listeners
  private List<MemberChangeListener> listeners;

  public interface MemberChangeListener {
    // Invoked when VolleyMember has a new List of Member
    void onMemberChangeList(List<Member> members);

    // Invoked when VolleyMember has a new avatar (as a bitmap)
    void onAvatarChange(Member member, Bitmap m);
  }

  // Listener should call this method to get notified on change (se above methods)
  public void addMemberChangeListener(MemberChangeListener l) {
    listeners.add(l);
  }

}
