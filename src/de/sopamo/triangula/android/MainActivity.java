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
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.musicProcessing.MusicPlayer;
import de.sopamo.triangula.android.tools.Hooks;
import de.sopamo.triangula.android.wifi.WifiConnection;
import org.jbox2d.common.Vec2;

import javax.microedition.khronos.opengles.GL10;
import java.io.File;

public class MainActivity extends Activity implements SensorEventListener {
    /** Called when the activity is first created. */

	GameGLSurfaceView mGameGlSurfaceView;
	private static TextView status;
	private static MainActivity instance;
    private WifiConnection wifiConnection;
    private WifiP2pManager wifiP2pManager;
    private IntentFilter intentFilter;
    private WifiP2pManager.Channel channel;
    public MediaPlayer forwardMediaPlayer;
    public MusicPlayer musicPlayer;
    private double pauseStartTime;

    //TextView test;

	public MainActivity() {
        instance = this;
	}
	
	Handler mHandler = new Handler();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        //setContentView(R.layout.main);
        //test = (TextView) findViewById(R.id.tv_status);

        File file = new File("raw/ingame.mp3");

        forwardMediaPlayer = MediaPlayer.create(this, R.raw.ingame);
        musicPlayer = new MusicPlayer(file,forwardMediaPlayer);
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
        GameImpl gameInstance = GameImpl.getInstance();

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
        registerReceiver(wifiConnection, intentFilter);
        musicPlayer.resumePlayer(pauseStartTime);
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
        pauseStartTime = musicPlayer.getCurrentPos();

        musicPlayer.pausePlayer();

    }
}