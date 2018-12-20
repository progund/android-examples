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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.juneday.memberimages.domain.Member;

public class VolleyMember {

  private static final String LOG_TAG = VolleyMember.class.getName();

  private static VolleyMember volleyMember;
  private Context context;

  public static synchronized VolleyMember getInstance(Context context) {
    if (volleyMember == null) {
      volleyMember = new VolleyMember(context);
    }
    Log.d(LOG_TAG, "getInstance()");
    return volleyMember;
  }

  private VolleyMember(Context context) {
    listeners = new ArrayList<>();
    this.context = context;
  }

  private List<Member> jsonToMembers(JSONArray array) {
    Log.d(LOG_TAG, "jsonToMembers: " + array);
    List<Member> memberList = new ArrayList<>();
    for (int i = 0; i < array.length(); i++) {
      Log.d(LOG_TAG, " parse JSON i: " + i);
      try {
        JSONObject row = array.getJSONObject(i);
        String name = row.getString("name");
        String email = row.getString("email");
        String avatarUrl = row.getString("avatar");
        Log.d(LOG_TAG, name +  " " + email  + "   : " + avatarUrl);
        Member m = new Member(name, email, avatarUrl);
        memberList.add(m);
      } catch (JSONException e) {
        ; // is ok since this is debug
        Log.d(LOG_TAG, "JSON parse: " + e);
      }
    }
    return memberList;
  }

  // The code below is "slightly" (nudge nudge) based on:
  //   https://developer.android.com/training/volley/request.html
  public void getMembers() {
    Log.d(LOG_TAG, "getMembers() from url: " + Settings.url);
    RequestQueue queue = Volley.newRequestQueue(context);

    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
        Request.Method.GET,
        Settings.url,
        null,
        new Response.Listener<JSONArray>() {

          @Override
          public void onResponse(JSONArray array) {
            Log.d(LOG_TAG, "getMembers() from url: " + Settings.url + " JSON:" + array);

            List<Member> members = jsonToMembers(array);
            for (MemberChangeListener m : listeners) {
              m.onMemberChangeList(members);
            }
            for (Member m : members) {
              if (!Utils.avatarExists(context, m)) {
                Log.d(LOG_TAG, "  download avatar for " + m.name());
                VolleyMember.getInstance(context).getAvatar(m);
              } else {
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
  }

  public void getAvatar(final Member member) {
    Log.d(LOG_TAG, "getAvatars()");
    RequestQueue queue = Volley.newRequestQueue(context);
    Log.d(LOG_TAG, "  url: " + member.avatarUrl());
    String url = member.avatarUrl();

    // if no url for member, use default
    if ( (url == null) || url.equals ("null") ) {
      url = "https://avatars0.githubusercontent.com/u/19474334?s=400&u=1ade95c4770d096ec33107c05b51c99cfdd6ab01&v=4";
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

  private List<MemberChangeListener> listeners;

  public interface MemberChangeListener {
    void onMemberChangeList(List<Member> members);
    void onAvatarChange(Member member, Bitmap m);
  }

  public void addMemberChangeListener(MemberChangeListener l) {
    listeners.add(l);
  }

}
