package se.juneday.lecturemouth.net;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import se.juneday.lecturemouth.domain.AudioClip;
import se.juneday.lecturemouth.domain.Theme;

public class VolleyAudio {

  private static final String LOG_TAG = VolleyAudio.class.getName();

  private static VolleyAudio volleyAudio;
  private static final String themesUrl = "https://raw.githubusercontent.com/progund/android-examples/master/common-data/lecturemouth/themes.json";

  private Context context;
  private RequestQueue queue;

  // String constants for files and dirs
  public static final String FILE_SEP = "/" ;
  private static final String FIELD_SEP = "_" ;
  private static final Object AUDIO_DIR = "audio";
  private static final Object JSON_DIR = "json";

  public static synchronized VolleyAudio getInstance(Context context) {
    if (volleyAudio == null) {
      volleyAudio = new VolleyAudio(context);
    }
    return volleyAudio;
  }

  private VolleyAudio(Context context) {
    listeners = new ArrayList<>();
    this.context = context;
    queue = Volley.newRequestQueue(context);
  }

  public void getThemes() {
    Log.d(LOG_TAG, " getThemes() from url: " + themesUrl);
    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
        Request.Method.GET,
        themesUrl,
        null,
        new Response.Listener<JSONArray>() {

          @Override
          public void onResponse(JSONArray array) {
            Log.d(LOG_TAG, " getThemes(): " + array);
            storeJsonToFileUrl(array.toString(), themesUrl);
            List<Theme> themes = jsonToThemesList(array);
            for (AudioChangeListener m : listeners) {
              Log.d(LOG_TAG, " getThemes(), informing " + m.getClass().getSimpleName());
              m.onThemeChange(themes);
            }
          }
        }, new Response.ErrorListener() {

      @Override
      public void onErrorResponse(VolleyError error) {
        Log.d(LOG_TAG, " cause: " + error.getCause().getMessage());
      }
    });
    queue.add(jsonArrayRequest);
  }

  public void getAudioTheme(final Theme theme) {
    Log.d(LOG_TAG, " getAudio() from url: " + theme);
    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
        Request.Method.GET,
        theme.url(),
        null,
        new Response.Listener<JSONArray>() {

          @Override
          public void onResponse(JSONArray array) {
            storeJsonToFileUrl(array.toString(), theme.url());
            List<AudioClip> audios = jsonToAudios(array);
            for (AudioChangeListener m : listeners) {
              Log.d(LOG_TAG, " getAudio(), informing " + m.getClass().getSimpleName());
              m.onAudioClipsChange(theme, audios);
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


  public File getAudioFile(Theme theme, final AudioClip ab) {
    final String fileName = audioFileDir(context)+ urlToName(ab.url());
    File file = new File(fileName);
    Log.d(LOG_TAG, " getAudio() from : " + ab.url() + "  --->" + fileName + "  --  " + file.getName());
    if (file.exists()) {
      Log.d(LOG_TAG, " getAudio() file aready exists " + file.getName());
      return file;
    } else {
      Log.d(LOG_TAG, " getAudio() file not there, downloading   " + file.getName());
    }
    Request<byte[]> request = new AudioRequest(ab.url(),
        new Response.Listener<byte[]>() {
          @Override
          public void onResponse(byte[] response) {
            try {
              if (response!=null) {
                Log.d(LOG_TAG, " got audio: " + response.toString());
                createAudioFile(context, response, urlToName(ab.url()));
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        } ,
        new Response.ErrorListener() {

      @Override
      public void onErrorResponse(VolleyError error) {
        Log.d(LOG_TAG , "onErrorResponse()" + error);
        error.printStackTrace();
      }
    });
    queue.add(request);
    return null;
  }

  private List<AudioClip> jsonToAudios(JSONArray array) {
    Log.d(LOG_TAG,"jsonToAudios() : " + array);
    List<AudioClip> AudioList = new ArrayList<>();
    for (int i = 0; i < array.length(); i++) {
      try {
        JSONObject row = array.getJSONObject(i);
        String name = row.getString("name");
        String url = row.getString("url");

        AudioClip m = new AudioClip(name, urlToName(url), url);
        AudioList.add(m);
        Log.d(LOG_TAG," * " + m);
      } catch (JSONException e) {
        Log.d(LOG_TAG, e.getMessage());
      }
    }
    return AudioList;
  }

  private List<Theme> jsonToThemesList(JSONArray array) {
    Log.d(LOG_TAG,"jsonToThemesList() : " + array);
    List<Theme> themes = new ArrayList<>();
    for (int i = 0; i < array.length(); i++) {
      try {
        JSONObject row = array.getJSONObject(i);
        String name = row.getString("name");
        String url = row.getString("url");
        Theme t = new Theme(name, url);
        themes.add(t);
        Log.d(LOG_TAG," * " + t);
//        getAudio(url);
      } catch (JSONException e) {
        Log.d(LOG_TAG, e.getMessage());
      }
    }
    return themes;
  }

  public String urlToName(String url) {
    String parts[] = url.split("/");
    String fileName = parts[parts.length-1];
    return Uri.encode(fileName);
  }

  private File storeJsonToFileUrl(String data, String url) {
    return storeJsonToFile(data, urlToName(url));
  }

  private File storeJsonToFile(String data, String name) {
    // Directory
    String dirName = jsonFileDir(context);
    File dir = new File(dirName);
    if(!dir.exists()) {
      dir.mkdirs();
    }

    // Fileame
    String fileName = dir + FILE_SEP + name;

    // Write to file
    PrintWriter out;
    try {
      out = new PrintWriter(fileName);
      out.print(data);
      out.flush();
    } catch (Exception e) {
      e.printStackTrace();
      Log.d(LOG_TAG, e.getMessage());
      return null;
    }
    Log.d(LOG_TAG, "Created file: " + fileName);
    return new File(fileName);
  }


  private static File createAudioFile(Context context, byte[] data, String name) throws IOException {
    // Directory
    String dirName = audioFileDir(context);
    File dir = new File(dirName);
    if(!dir.exists()) {
      dir.mkdirs();
    }

    // Fileame
    String fileName = dir + FILE_SEP + name;

    // Write to file
    FileOutputStream outputStream;
    try {
      outputStream = new FileOutputStream (fileName);
      outputStream.write(data);
      outputStream.flush();
    } catch (Exception e) {
      e.printStackTrace();
      Log.d(LOG_TAG, e.getMessage());
      return null;
    }
    Log.d(LOG_TAG, "Created file: " + fileName);
    return new File(fileName);
  }

  public static String audioFileDir(Context c) {
    return c.getFilesDir().getAbsoluteFile()+
        FILE_SEP + AUDIO_DIR + FILE_SEP;
  }

  public static String jsonFileDir(Context c) {
    return c.getFilesDir().getAbsoluteFile()+
        FILE_SEP + JSON_DIR + FILE_SEP;
  }


  /******************************************
   AudioChangeListener
   ******************************************/
  private List<AudioChangeListener> listeners;

  public interface AudioChangeListener {
    void onAudioClipsChange(Theme theme, List<AudioClip> audioButtons);
    void onThemeChange(List<Theme> themes);
  }

  public void addAudioChangeListener(AudioChangeListener l) {
    listeners.add(l);
  }

}
