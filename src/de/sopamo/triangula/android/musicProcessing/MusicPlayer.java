package de.sopamo.triangula.android.musicProcessing;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.io.File;
import java.io.FileInputStream;


public class MusicPlayer {
    private File file;
    private FileInputStream fis;
    private Player playMP3;
    private double initialSystemTime;
    private double beatTime;

    //move all x frames
    private int move ;

    public MusicPlayer(File file){
        this.file = file;
    }
/*
    method to play music
     */
    public void play(){
        try{
            fis = new FileInputStream(file);

            playMP3 = new Player(fis);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        initialSystemTime = System.currentTimeMillis();
                        playMP3.play();
                    } catch (JavaLayerException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
        catch(Exception exc){
            exc.printStackTrace();
            System.out.println("Failed to play the file.");
        }
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                play();
            }
        }).start();
    }

    public double getInitialSystemTime(){
        return initialSystemTime;
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
