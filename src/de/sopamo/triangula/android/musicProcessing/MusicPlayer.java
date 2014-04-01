package de.sopamo.triangula.android.musicProcessing;

import javazoom.jl.player.Player;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by Fabi on 01.04.14.
 */
public class MusicPlayer {
    private File file;
    private FileInputStream fis;
    private Player playMP3;

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
            playMP3.play();
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
        //move all x frames
        int move = beatAnalyzA.getMovement();


        System.out.println("Move all " + move + " frames");
        //while song is not completed check if Frame is onset

        play();
        while(true){
            System.out.println("current frameposition " + playMP3.getPosition());
        }

        /*
        double startTime = System.currentTimeMillis();

        while(!playMP3.isComplete()){
            beatAnalyzA.framesFromStart(startTime);
            //DEBUGGING START
            System.out.println("StartTime: "+startTime);
            System.out.println("Current time: "+System.currentTimeMillis());
            //DEBUGGING END
        }
        */
    }

}
