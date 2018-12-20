package se.juneday.lecturemouth;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.File;

import se.juneday.lecturemouth.domain.AudioButton;
import se.juneday.lecturemouth.storage.Storage;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        LinearLayout layout = findViewById(R.id.button_layout);


        for (AudioButton button : Storage.getInstance(this).buttons()) {
            Log.d(LOG_TAG, " * " + button);
            Button b = new Button(this);
            b.setText(button.text());
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG_TAG, " playing file");
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(),R.raw.collect5);
                    mp.start();
                }
            });
            layout.addView(b);
        }

    }

}
