package de.sopamo.triangula.android.musicProcessing;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.File;


public class MusicPlayer {
    private File file;
    private double beatTime;
    private MediaPlayer forwardMediaPlayer;

    public MusicPlayer(File file, MediaPlayer forwardMediaplayer){
        this.file = file;
        this.forwardMediaPlayer = forwardMediaplayer;
    }

/*
    Instantiate a new beatanalyzer, run the file analyze and save the number of frames to be on the beat in int move.
    Afterwards the song is played. a loop checks every frame if onset or not.
     */
    public void runMusicPlayer(){
        BeatAnalyzA beatAnalyzA = new BeatAnalyzA();
        beatAnalyzA.runFileProcessing(file);
        beatTime = beatAnalyzA.getMovementTime();


    }

    public void playMusic (){
        new Thread(new Runnable() {
            @Override
            public void run() {
                forwardMediaPlayer.start();
                forwardMediaPlayer.setLooping(true);
            }
        }).start();

    }

    public void pausePlayer (){
        forwardMediaPlayer.pause();
        Log.e("leFuc","La Pause grand a lieu");
    }

    public void destroyPlayer (){
        forwardMediaPlayer.stop();
        Log.e("leFuc","La déstruction grand a lieu");
    }

    public void resumePlayer(double pauseStartTime){
        forwardMediaPlayer.seekTo((int)(pauseStartTime));
        Log.e("leFuc","Le temps de la pause "+pauseStartTime);
        forwardMediaPlayer.start();
    }

    public double getCurrentPos (){
        Log.e("leFuc","La ");
        return forwardMediaPlayer.getCurrentPosition();

    }



    public double getBeatTime(){
        return beatTime;
    }

    public boolean isOnset(double initialSystemTime, double beatTime){
        double tmp = initialSystemTime - System.currentTimeMillis();
        System.out.println("Current time: "+System.currentTimeMillis());
        if(tmp%beatTime==0){
            return true;
        }else{
            return false;
        }
    }





}
