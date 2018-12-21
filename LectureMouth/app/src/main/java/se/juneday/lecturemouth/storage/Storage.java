package se.juneday.lecturemouth.storage;

import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import se.juneday.lecturemouth.domain.AudioButton;
import se.juneday.lecturemouth.net.Theme;
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
          public void onAudioButtonsChangeList(List<AudioButton> audioButtons) {
            Log.d(LOG_TAG, "onAudioChangeList() " + audioButtons);
            for (AudioButton ab : audioButtons) {
              Log.d(LOG_TAG, " * " + ab + "    --- getting (in e near future) audio file");
              VolleyAudio.getInstance(context).getAudioFile(ab);
            }
          }

          @Override
          public void onThemeChangeList(List<Theme> themes) {
            Log.d(LOG_TAG, "onThemeChangeList() " + themes);
            for (Theme t : themes) {
              Log.d(LOG_TAG, " * " + t + "    --- getting audio theme");
              VolleyAudio.getInstance(context).getAudioTheme(t);
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

  public List<AudioButton> buttons() {
    List<AudioButton> buttons = new ArrayList<>();
    buttons.add(new AudioButton("Laugh", "kkk", "laugh.mp3"));
    return buttons;
  }

  public List<Theme> themes() {
    return themes;
  }

  public void themesUpdate() {
    VolleyAudio.getInstance(context).getThemes();
  }

}
