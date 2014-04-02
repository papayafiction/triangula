package de.sopamo.triangula.android;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import de.sopamo.triangula.android.musicProcessing.MusicPlayer;

import java.io.File;

/**
 * Created by Fabi on 02.04.14.
 */
public class LoadingActivity extends Activity {


    private MediaPlayer backwardMediaPlayer;
    public LoadingActivity that;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        that = this;


        Intent toMain = new Intent(this, MainActivity.class);
        startActivity(toMain);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }



}
