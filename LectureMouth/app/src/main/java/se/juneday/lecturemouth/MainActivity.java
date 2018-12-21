package se.juneday.lecturemouth;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.File;
import java.util.List;

import se.juneday.lecturemouth.domain.AudioClip;
import se.juneday.lecturemouth.domain.Theme;
import se.juneday.lecturemouth.net.VolleyAudio;
import se.juneday.lecturemouth.storage.Storage;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      Storage.getInstance(this).themesUpdate();

    }

    private void audioPlayer(String path, String fileName){
        //set up MediaPlayer
        MediaPlayer mp = new MediaPlayer();


        try {
            mp.setDataSource(path + File.separator + fileName);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Storage.getInstance(this).registerStorageUpdateListener(new Storage.StorageUpdateListener() {
            @Override
            public void onStorageUpdate() {
                Log.d(LOG_TAG, "onStorageUpdate()");
                updateListView();
            }
        });
        Storage.getInstance(this).themesUpdate();
    }


    private void updateListView() {
        Log.d(LOG_TAG, "updateListView()");
        LinearLayout layout = findViewById(R.id.button_layout);

        List<Theme> themes = Storage.getInstance(this).themes();
        if (themes==null || themes.size() == 0) {
            return ;
        }
        Theme theme = themes.get(0);
        Log.d(LOG_TAG, "updateListView()  theme: " + theme);
        for (final AudioClip clip : theme.audioClips()) {
            Log.d(LOG_TAG, " * " + clip);
            Button b = new Button(this);
            b.setText(clip.text());
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG_TAG, " playing file " + clip.path());
                    Uri uri = Uri.fromFile(new File(VolleyAudio.audioFileDir(MainActivity.this) +
                            VolleyAudio.FILE_SEP + clip.path()));
                    Log.d(LOG_TAG, " playing file in mp: " + uri);
                   MediaPlayer mp = MediaPlayer.create(MainActivity.this, uri);
                    Log.d(LOG_TAG, " playing file with mp: " +
                            mp);
                    if (mp!=null) {
                        mp.start();
                    }
                }
            });
            layout.addView(b);
        }

    }

}
