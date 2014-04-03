package de.sopamo.triangula.android;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import de.sopamo.triangula.android.musicProcessing.MusicPlayer;

import java.io.File;

public class LoadingActivity extends Activity {


    private MediaPlayer backwardMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        final LoadingActivity that = this;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent toMain = new Intent(that, MainActivity.class);
                startActivity(toMain);
                finish();
            }
        }, 3000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }



}
