package se.juneday.lecturemouth.storage;

import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import se.juneday.lecturemouth.domain.AudioClip;
import se.juneday.lecturemouth.domain.Theme;
import se.juneday.lecturemouth.net.VolleyAudio;
import se.juneday.lecturemouth.net.VolleyAudio.AudioChangeListener;

public class Storage {

  private static final String LOG_TAG = Storage.class.getSimpleName();
  private static Storage instance;
  private Context context;

  private List<Theme> themes;

  private Storage(final Context context) {
        this.context = context;
        this.themes = new ArrayList<>();
        VolleyAudio.getInstance(context).addAudioChangeListener(new AudioChangeListener() {

          @Override
          public void onAudioClipsChange(Theme theme, List<AudioClip> audioButtons) {
            Log.d(LOG_TAG, "onAudioChangeList() " + audioButtons);
            for (AudioClip ab : audioButtons) {
              Log.d(LOG_TAG, " * " + ab + "    --- getting (in e near future) audio file");
              VolleyAudio.getInstance(context).getAudioFile(theme, ab);
              theme.addAudionClip(ab);
            }
            for (StorageUpdateListener listener : listeners) {
                Log.d(LOG_TAG, " inform listener: " + listener.getClass().getSimpleName());
                listener.onStorageUpdate();
            }
          }

          @Override
          public void onThemeChange(List<Theme> themes) {
            Log.d(LOG_TAG, "onThemeChangeList() " + themes);
            for (Theme t : themes) {
              Log.d(LOG_TAG, " * " + t + "    --- getting audio theme");
              VolleyAudio.getInstance(context).getAudioTheme(t);
              Storage.instance.themes = themes;
            }
            for (StorageUpdateListener listener : listeners) {
                Log.d(LOG_TAG, " inform listener: " + listener.getClass().getSimpleName());
                listener.onStorageUpdate();
            }
          }
        });
    }

    public static synchronized Storage getInstance(Context context) {
        if (instance==null) {
            instance = new Storage(context);
        }
        return instance;
    }

  public List<Theme> themes() {
    return themes;
  }

  public void themesUpdate() {
    VolleyAudio.getInstance(context).getThemes();
  }


  public static List<StorageUpdateListener> listeners = new ArrayList<>();
  public void registerStorageUpdateListener(StorageUpdateListener listener) {
        Log.d(LOG_TAG, "registerStorageUpdateListener: " + listener.getClass().getSimpleName());
      this.listeners.add(listener);
  }
  public static interface StorageUpdateListener {
      public void onStorageUpdate();
  }

}
