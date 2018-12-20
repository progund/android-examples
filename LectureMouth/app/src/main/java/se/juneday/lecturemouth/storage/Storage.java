package se.juneday.lecturemouth.storage;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import se.juneday.lecturemouth.domain.AudioButton;

public class Storage {

    private static Storage instance;
    private Context context;

    private Storage(Context context) {
        this.context = context;
    }

    public static synchronized Storage getInstance(Context context) {
        if (instance==null) {
            instance = new Storage(context);
        }
        return instance;
    }

    public List<AudioButton> buttons() {
        List<AudioButton> buttons = new ArrayList<>();
        buttons.add(new AudioButton("Antheil", "path-to-antheil"));
        buttons.add(new AudioButton("Scream", "path-to-slakj"));
        buttons.add(new AudioButton("Sigh", "path-heil"));
        buttons.add(new AudioButton("Laugh", "path-to-antheil"));
        return buttons;
    }

}
