package se.juneday.memberimages;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class ActivityHelper {

  private static final String LOG_TAG = ActivityHelper.class.getSimpleName();

  public static void showToast(Context context, String msg) {
    Log.d(LOG_TAG, " showToast: " + msg);
    int duration = Toast.LENGTH_SHORT;
    Toast toast = Toast.makeText(context, msg, duration);
    toast.show();
  }


}
