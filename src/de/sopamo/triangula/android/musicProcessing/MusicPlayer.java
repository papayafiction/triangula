package de.sopamo.triangula.android.musicProcessing;

import android.media.MediaPlayer;

import java.io.File;


public class MusicPlayer {
    private File file;
    private double initialPlayTime;
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

        System.out.println("Move all " + beatTime + " seconds");


    }

    public void playMusic (){
        new Thread(new Runnable() {
            @Override
            public void run() {
                forwardMediaPlayer.start();
            }
        }).start();

    }

    public void destroyPlayer (){
        forwardMediaPlayer.stop();
    }


    public double getInitialPlayTime(){
        return initialPlayTime;
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
