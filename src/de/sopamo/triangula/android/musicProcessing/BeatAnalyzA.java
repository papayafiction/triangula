package de.sopamo.triangula.android.musicProcessing;

import javazoom.jl.player.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import static java.lang.Math.floor;

/**
 * Created by Fabi on 31.03.14.
 */
public class BeatAnalyzA {
    public BeatAnalyzA(){
    }
    //Instantiate the classes needed to analyze MP3 file
    private BPM2SampleProcessor processor = new BPM2SampleProcessor();
    private EnergyOutputAudioDevice output = new EnergyOutputAudioDevice(processor);
    private int moveAllXFrames;


    private int frameFactor = 0;
/*
    read in file, process and call method getMovement()
 */
    public void runFileProcessing(File file){
        processor.setSampleSize(1024);
        output.setAverageLength(1024);

        //Read in file into file input stream
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Plays sound and gives it to the processor
        try {
            Player player = new Player(fileInputStream, output);
            player.play();
        }catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to play file in class BeatAnalyzA!");
        }
        //For debugging purposes
        /*
        System.out.println("BPM: "+processor.getBPM());
        System.out.println("Move all "+getMovement()+" Frames");*/

        //DEBUGGING START
        /*System.out.println("Move all "+getMovement()+" Frames");


        startTime = System.currentTimeMillis();
        double beat = 83;
        while(true){
            try {
                Thread.sleep((int)floor(beat));
                framesFromStart();
                System.out.println("Frames from start: "+ framesFromStart());

                //isOnset(frameNumber);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        //DEBUGGING END
    }
    /*
        reads in the bpm from the processor and returns the number of frames in which object must move to be onset
     */

    public int getMovement(){
        double BPM = processor.getBPM();
        double BPS;
        //time one beat needs
        double time;
        double toFrames;

        BPS = (BPM/60.0);
        time = (1/BPS);

        toFrames = (time*1000)/16.6d;

        //For debugging purposes
        System.out.println("BPS: "+BPS);
        System.out.println("time: "+time);
        System.out.println("toFrames: "+toFrames);

        moveAllXFrames = (int)floor(toFrames);

        return moveAllXFrames;
    }


    public int framesFromStart(double startTime) {
        double framesInTime;
        double deltaT;
        deltaT = (System.currentTimeMillis()-startTime);
        framesInTime = deltaT/16.6d;

        return (int)((deltaT/1000)*framesInTime);

    }


}

