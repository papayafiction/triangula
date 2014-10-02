package de.sopamo.triangula.android.musicProcessing;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.File;

import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.mechanics.Rewindable;


public class MusicPlayer implements Rewindable {
    private File fileF;
    private File fileB;
    private int beatTime;
    private MediaPlayer mediaPlayerForward;
    private MediaPlayer mediaPlayerBackwards;

    private int msPerBeat;
    private GameImpl game;
    private double initTime;
    private double initSysTime;
    private double endSysTime;
    private double diffTime;
    private boolean rewinding = false;


    public MusicPlayer(File fileF, File fileB, MediaPlayer mediaPlayerForward, MediaPlayer mediaPlayerBackwards, int beatTime){
        this.fileF = fileF;
        this.fileB = fileB;
        this.mediaPlayerForward = mediaPlayerForward;
        this.mediaPlayerBackwards = mediaPlayerBackwards;
        this.beatTime = beatTime;
        msPerBeat = 1000 / beatTime;
        this.game = GameImpl.getInstance();
        game.getRewindables().add(this);
    }

    /**
     * TODO
     * BackwardsPlayer not able to loop
     */

    public void playMusic (){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mediaPlayerForward.start();
                mediaPlayerForward.setLooping(true);
            }
        }).start();

    }

    public void pausePlayerForward(){
        mediaPlayerForward.pause();
    }
    public void pausePlayerBackward(){
        mediaPlayerBackwards.pause();
    }

    public void resumePlayerForward(double pauseStartTime){
        mediaPlayerForward.seekTo((int) (pauseStartTime));
        mediaPlayerForward.start();
    }

    public void resumePlayerBackward(double pauseStartTime){
        mediaPlayerBackwards.seekTo((int) (pauseStartTime));
        mediaPlayerBackwards.start();
        mediaPlayerBackwards.setLooping(true);
    }

    public void destroyPlayer (){
        mediaPlayerForward.stop();
        mediaPlayerBackwards.stop();
    }

    public double getCurrentPosForward(){
        return mediaPlayerForward.getCurrentPosition();
    }

    public boolean isOnset(double initialSystemTime){
        int tmp = (int)initialSystemTime - (int)System.currentTimeMillis();
        if(tmp%beatTime==0){
            return true;
        }else{
            return false;
        }
    }


    @Override
    public void startRewind() {
        //when rewinding is initiated
        rewinding = true;
        pausePlayerForward();
        initTime = getCurrentPosForward();
        initSysTime = System.currentTimeMillis();

        resumePlayerBackward((mediaPlayerForward.getDuration()-initTime)/2);
    }

    @Override
    public void stopRewind() {
        //when rewinding is ended
        rewinding = false;
        endSysTime = System.currentTimeMillis();
        diffTime = (initTime-(endSysTime-initSysTime)*2);
        pausePlayerBackward();
        resumePlayerForward(diffTime);
    }

    @Override
    public void run() {

    }

    @Override
    public boolean isRewinding() {
        return rewinding;
    }
}
