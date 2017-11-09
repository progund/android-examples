package se.juneday.volleyex.volleyexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by hesa on 2017-11-09.
 */

public class ActivitySwitcher {

  private static final String LOG_TAG = ActivitySwitcher.class.getSimpleName();

  public static void switchActivity(Class c, Activity context) {
    Intent intent = new Intent(context, c);
    context.startActivity(intent);
  }

  public static void switchToSemiSeparateActivity(Activity context) {
    switchActivity(SemiSeparateActivity.class, context);
  }

  public static void switchToSeparateActivity(Activity context) {
    switchActivity(SeparateActivity.class, context);
  }

  public static void switchToVolleyActivity(Activity context) {
    switchActivity(VolleyActivity.class, context);
  }

  public static void showToast(Context context, String msg) {
    Log.d(LOG_TAG, " showToast: " + msg);
    int duration = Toast.LENGTH_SHORT;
    Toast toast = Toast.makeText(context, msg, duration);
    toast.show();
  }


}
