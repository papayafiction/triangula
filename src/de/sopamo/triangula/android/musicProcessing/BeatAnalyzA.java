package de.sopamo.triangula.android.musicProcessing;

import javazoom.jl.player.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import static java.lang.Math.floor;


public class BeatAnalyzA {
    public BeatAnalyzA(){
    }
    //Instantiate the classes needed to analyze MP3 file
    private BPM2SampleProcessor processor = new BPM2SampleProcessor();
    private EnergyOutputAudioDevice output = new EnergyOutputAudioDevice(processor);


/*
    read in file, process and call method getMovementTime()
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
    }
    /*
        reads in the bpm from the processor and returns the number of frames in which object must move to be onset
     */

    public double getMovementTime(){
        double BPM = processor.getBPM();
        double BPS;
        //time one beat needs
        double time;

        BPS = (BPM/60.0);
        time = (1/BPS);


        //For debugging purposes
        System.out.println("BPS: "+BPS);


        return time;
    }




}

