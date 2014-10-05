package de.sopamo.triangula.android.musicProcessing;


import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import de.sopamo.triangula.android.App;
import de.sopamo.triangula.android.GameActivity;
import de.sopamo.triangula.android.R;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.mechanics.Rewindable;
import de.sopamo.triangula.android.tools.Hooks;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class MusicPlayer implements Rewindable {

    private GameImpl game;
    private boolean rewinding = false;
    public long sysPauseStart = 0;
    public long initTimePause = 0;
    public long sysPauseEnd = 0;
    public long diffTime = 0;

    private MediaPlayer forwardMediaPlayer = null;
    private MediaPlayer backwardMediaPlayer = null;
    private MediaPlayer explosionMediaPlayer = null;
    private MediaPlayer switchMediaPlayer = null;
    private MediaPlayer bounceMediaPlayer = null;
    private MediaPlayer exitMediaPlayer = null;
    private MediaPlayer menuMediaPlayer = null;

    private static List<MediaPlayer> players = new ArrayList<MediaPlayer>();

    public MusicPlayer() {
    }

    public void init(){


        forwardMediaPlayer =  MediaPlayer.create(App.getContext(),R.raw.ingame);
        backwardMediaPlayer = MediaPlayer.create(App.getContext(),R.raw.ingame_reverse_double_pitch);
        forwardMediaPlayer.setLooping(true);
        backwardMediaPlayer.setLooping(true);

        players.add(forwardMediaPlayer);
        players.add(backwardMediaPlayer);

        explosionMediaPlayer = MediaPlayer.create(App.getContext(),R.raw.bomb);
        switchMediaPlayer = MediaPlayer.create(App.getContext(),R.raw.switches);
        bounceMediaPlayer = MediaPlayer.create(App.getContext(),R.raw.bounce);
        exitMediaPlayer = MediaPlayer.create(App.getContext(),R.raw.exit);
        menuMediaPlayer = MediaPlayer.create(App.getContext(),R.raw.menu);
        menuMediaPlayer.setLooping(true);

        players.add(explosionMediaPlayer);
        players.add(switchMediaPlayer);
        players.add(bounceMediaPlayer);
        players.add(exitMediaPlayer);
        players.add(menuMediaPlayer);

        //start rewinding
        Hooks.bind(Hooks.REWINDING,new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                sysPauseStart = System.currentTimeMillis();
                initTimePause = players.get(0).getCurrentPosition();
                players.get(0).pause();
                players.get(1).seekTo((int)(players.get(0).getDuration()-initTimePause)/2);
                players.get(1).start();
                Log.e("music","Rewinding");
                return null;
            }
        });

        //end rewinding
        Hooks.bind(Hooks.STOP_REWINDING,new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                sysPauseEnd = System.currentTimeMillis();
                diffTime = (initTimePause-(sysPauseEnd-sysPauseStart)*2);
                players.get(1).pause();

                players.get(0).setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mp) {
                        mp.start();
                    }
                });
                players.get(0).seekTo((int) diffTime);
                Log.e("music","Stop Rewinding");
                return null;
            }
        });

        //playing bouncing sound
        Hooks.bind(Hooks.BOUNCE,new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
               bounceMediaPlayer.start();
                Log.e("music","Bouncing");
                return null;
            }
        });

        //playing bouncing sound
        Hooks.bind(Hooks.EXIT,new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                exitMediaPlayer.start();
                Log.e("music","Exiting");
                return null;
            }
        });

        //playing switching sound
        Hooks.bind(Hooks.SWITCH,new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                switchMediaPlayer.start();
                Log.e("music","Switching");
                return null;
            }
        });

        //playing exploding sound
        Hooks.bind(Hooks.EXPLODE,new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                explosionMediaPlayer.start();
                Log.e("music","Exploding");
                return null;
            }
        });

        //music muted
        Hooks.bind(Hooks.MUTE,new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                if (players != null){
                    for (MediaPlayer player: players){
                        player.setVolume(0,0);
                    }
                    Log.e("music","All music muted");
                }
                return null;
            }
        });

        //music unmute
        Hooks.bind(Hooks.UNMUTE,new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                if (players != null){
                    for (MediaPlayer player: players){
                        player.setVolume(1,1);
                    }
                    Log.e("music","All music unmuted");
                }
                return null;
            }
        });

        //stop all players
        Hooks.bind(Hooks.STOP_ALL,new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                for (MediaPlayer player: players){
                    if(player.isPlaying()) {
                        player.pause();
                    }
                }
                Log.e("music","All music stopped");
                return null;
            }
        });

        //destroy all players
        Hooks.bind(Hooks.DESTROY_ALL,new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                for (MediaPlayer player: players){
                    player.stop();
                    player.release();
                }
                Log.e("music","All players destroyed");
                return null;
            }
        });

        //resume playing
        Hooks.bind(Hooks.RESUME,new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                players.get(0).seekTo(0);
                players.get(0).start();
                return null;
            }
        });

        //start menu music
        Hooks.bind(Hooks.MENU_START,new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Log.e("music","menu start called");
                menuMediaPlayer.start();

                return null;
            }
        });

        //stop menu music
        Hooks.bind(Hooks.MENU_STOP,new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Log.e("music","menu stop called");
                if (menuMediaPlayer != null){
                    menuMediaPlayer.pause();
                    Log.e("music","paused!");
                }
                return null;
            }
        });

    }

    @Override
    public void startRewind() {
        Hooks.call(Hooks.REWINDING);
        rewinding = true;
    }

    @Override
    public void stopRewind() {
        Hooks.call(Hooks.STOP_REWINDING);
        rewinding = false;
    }

    @Override
    public void updateRewindable() {

    }

    @Override
    public boolean isRewinding() {
        return rewinding;
    }

}
