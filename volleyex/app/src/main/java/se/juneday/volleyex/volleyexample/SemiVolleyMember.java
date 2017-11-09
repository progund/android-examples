package se.juneday.volleyex.volleyexample;

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
import org.json.JSONException;
import org.json.JSONObject;
import se.juneday.volleyex.volleyexample.VolleyMember.MemberChangeListener;

public class SemiVolleyMember {

  private static final String LOG_TAG = SemiVolleyMember.class.getName();

  private static SemiVolleyMember volleyMember;
  private Context context;
  MemberChangeListener listener;

  public static synchronized SemiVolleyMember getInstance(Context context, MemberChangeListener listener) {
    if (volleyMember == null) {
      volleyMember = new SemiVolleyMember(context, listener);
    }
    return volleyMember;
  }

  private SemiVolleyMember(Context context, MemberChangeListener listener) {
    this.listener = listener;
    this.context = context;
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
  public void getMembers() {
    RequestQueue queue = Volley.newRequestQueue(context);

    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
        Request.Method.GET,
        Settings.url,
        null,
        new Response.Listener<JSONArray>() {

          @Override
          public void onResponse(JSONArray array) {
            List<Member> members = jsonToMembers(array);
            listener.onMemberChangeList(members);
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
   MemberChangeListener
   ******************************************/

  public interface MemberChangeListener {
    void onMemberChangeList(List<Member> members);
  }

}
