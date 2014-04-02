package de.sopamo.triangula.android.musicProcessing;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.*;


public class MusicPlayer {
    private File file;
    private FileInputStream forwardFileInputStream;
    private FileInputStream rewindFileInputStream;
    private OutputStream outputStream;
    private AdvancedPlayer advancedPlayerForward;
    private AdvancedPlayer advancedPlayerRewind;
    private double initialPlayTime;
    private double beatTime;
    private byte[] bytesIn;
    private byte[] bytesOut;


    public MusicPlayer(File file){
        this.file = file;
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

        try {
            forwardFileInputStream = new FileInputStream(file);
        /*
            bytesIn = new byte[(int)file.length()+1];
            rewindFileInputStream = new FileInputStream(file);
            rewindFileInputStream.read(bytesIn);

            for(int i = (int)file.length()+1; i > -1; i--){
                bytesOut[i-(int)file.length()] = bytesIn[i];
            }


        */


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playMusic (){
        new Thread(new Runnable() {
            @Override
            public void run() {
                play();
            }
        }).start();

    }

    public void rewindMusic (){
        new Thread(new Runnable() {
            @Override
            public void run() {
                rewind();
            }
        }).start();

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

    /*
    method to play music
     */
    private void play(){
        try{

            advancedPlayerForward = new AdvancedPlayer(forwardFileInputStream);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        initialPlayTime = System.currentTimeMillis();
                        advancedPlayerForward.play();
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
    private void rewind(){
        advancedPlayerForward.stop();


    }



}
