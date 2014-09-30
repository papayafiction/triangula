/*
 *
 * (c)2010 Lein-Mathisen Digital
 * http://lmdig.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.  
 *
 */




package de.sopamo.triangula.android;


import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.levels.Level;
import de.sopamo.triangula.android.levels.Level1;
import de.sopamo.triangula.android.musicProcessing.MusicPlayer;
import de.sopamo.triangula.android.tools.Hooks;
import de.sopamo.triangula.android.wifi.WifiConnection;
import org.jbox2d.common.Vec2;

import java.io.File;

public class GameActivity extends FragmentActivity implements SensorEventListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

    /** Called when the activity is first created. */

    private static GoogleApiClient mGoogleApiClient;
    private boolean mAutoStartSignInFlow = true;
    private boolean mResolvingConnectionFailure = false;
    private boolean mSignInClicked = false;

	GameGLSurfaceView mGameGlSurfaceView;
	private static TextView status;
	private static GameActivity instance;
    private WifiConnection wifiConnection;
    private WifiP2pManager wifiP2pManager;
    private IntentFilter intentFilter;
    private WifiP2pManager.Channel channel;
    //Music Handling
    public MediaPlayer forwardMediaPlayer;
    public MediaPlayer backwardMediaPlayer;
    public MusicPlayer musicPlayer;
    private double pauseStartTime;

    private GameImpl gameInstance;



    //TextView test;

	public GameActivity() {
        instance = this;
	}

    public static Level level;
	
	Handler mHandler = new Handler();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(this).addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this).addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN).
                addApi(Games.API).addScope(Games.SCOPE_GAMES).build();

        Bundle b = getIntent().getExtras();
        if(b!= null) {
            level = (Level) b.get("level");
        } else {
            level = new Level1();
        }

        mGameGlSurfaceView = new GameGLSurfaceView(this);
        
        setContentView(R.layout.main);
        LinearLayout ll = (LinearLayout)findViewById(R.id.layout_main);
        ll.addView(mGameGlSurfaceView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        
        status = (TextView)findViewById(R.id.tv_status);
        
        SensorManager sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        
        intentFilter = new IntentFilter();
        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(this, getMainLooper(), null);
        wifiConnection = new WifiConnection(wifiP2pManager, channel, intentFilter, this);

        //Music Handling on create
        // Files of the song in the right order and reverse
        File fileF = new File("raw/ingame.mp3");
        File fileB = new File("raw/ingame_reverse_double_pitch.mp3");

        //Instantiate players and start playing music forward
        forwardMediaPlayer = MediaPlayer.create(this, R.raw.ingame);
        backwardMediaPlayer = MediaPlayer.create(this, R.raw.ingame_reverse_double_pitch);

        musicPlayer = new MusicPlayer(fileF, fileB, forwardMediaPlayer, backwardMediaPlayer, 156);
        musicPlayer.playMusic();
    }
    
    
    public static void setStatus(String text) {
    	if(status == null)
    		return;
    	
    	instance.runOnUiThread(new StatusUpdate(text));
    }
    
    private static class StatusUpdate implements Runnable {
    	private String text;
    	public StatusUpdate(String text) {
    		this.text = text;
    	}
		@Override
		public void run() {
    		status.setText(text);
		}
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	
    	mGameGlSurfaceView.destroy();
        //Music handling on destroy
        musicPlayer.destroyPlayer();

    }


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	public static float x, y, z;

	@Override
	public void onSensorChanged(SensorEvent event) {
//		System.out.println(event.values[0]+","+ event.values[1]+","+ event.values[1]);
//		status.setText(event.values[0]+",	\n"+ event.values[1]+",	\n"+ event.values[2]);
		x = -event.values[0];
		y = -event.values[1];
		z = -event.values[2];
	}

    // Get touch event positions
    public static int touch_x, touch_y;
    public static boolean touched = false;
    public static boolean longTouched = false;

    private static long lastClick = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gameInstance = GameImpl.getInstance();

        if(GameImpl.getInstance() == null) return false;

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            gameInstance.getInputHandler().setTouchPosition(new Vec2(event.getX(),event.getY()));
            gameInstance.getInputHandler().setTouched();
            Hooks.call(Hooks.TAP);
        } else if(event.getAction() == MotionEvent.ACTION_UP) {
            gameInstance.getInputHandler().reset();
        }

        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Music resuming after pause is ended
        musicPlayer.resumePlayerForward(pauseStartTime);

        registerReceiver(wifiConnection, intentFilter);
        wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {

            public void onSuccess() {
                //test.setText("WUHU!");
            }

            public void onFailure(int reason) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(wifiConnection);

        //Music pausing, saving current pos
        pauseStartTime = musicPlayer.getCurrentPosForward();
        musicPlayer.pausePlayerForward();
    }

    public static GameActivity getInstance() {
        return instance;
    }

    public Handler getHandler() {
        return mHandler;
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            Log.e("yolo", "onConnectionFailed(): already resolving");
            return;
        }
        Log.e("yolo",""+connectionResult.toString());
        if ( mAutoStartSignInFlow || mSignInClicked) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;
            if (!BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient, connectionResult,
                    9001, "Fehler beim einloggen")) {
                mResolvingConnectionFailure = false;
            }
        }

    }

    public static GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }
}