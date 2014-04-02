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


    public MediaPlayer forwardMediaPlayer;
    private MediaPlayer backwardMediaPlayer;
    public LoadingActivity that;
    public MusicPlayer musicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        that = this;
        File file = new File("raw/xoxo.mp3");

        forwardMediaPlayer = MediaPlayer.create(this, R.raw.xoxo);
        musicPlayer = new MusicPlayer(file,forwardMediaPlayer);
        musicPlayer.playMusic();

        Intent toMain = new Intent(this, MainActivity.class);
        startActivity(toMain);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        musicPlayer.destroyPlayer();
    }



    public MediaPlayer getForwardMP(){
        return forwardMediaPlayer;
    }
}
