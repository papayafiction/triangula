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


import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.levels.Level;
import de.sopamo.triangula.android.levels.official.Level1;
import de.sopamo.triangula.android.musicProcessing.MusicPlayer;
import de.sopamo.triangula.android.tools.Hooks;
import org.jbox2d.common.Vec2;

import java.io.File;

public class GameActivity extends FragmentActivity implements SensorEventListener {

    /**
     * Called when the activity is first created.
     */
    GameGLSurfaceView mGameGlSurfaceView;
    private static TextView status;
    private static GameActivity instance;
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
        App.setActivityContext(this);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            level = (Level) b.get("level");
        } else {
            level = new Level1();
        }

        mGameGlSurfaceView = new GameGLSurfaceView(this);

        setContentView(R.layout.main);
        LinearLayout ll = (LinearLayout) findViewById(R.id.layout_main);
        ll.addView(mGameGlSurfaceView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

        status = (TextView) findViewById(R.id.tv_status);
        // Disable hiding for debug
        if(true) {
            status.setVisibility(View.GONE);
        }

        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
       
        mGameGlSurfaceView.init();

    }


    public static void setStatus(String text) {
        if (status == null)
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
            // Enable for debug
            //status.setText(text);
        }
    }

    @Override
    protected void onDestroy() {
        GameImpl.getInstance().getPhysicsTask().softCancel();
        GameImpl.getInstance().getWorld().setContactListener(null);
        super.onDestroy();
    	mGameGlSurfaceView.destroy();
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

        if (GameImpl.getInstance() == null) return false;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            gameInstance.getInputHandler().setTouchPosition(new Vec2(event.getX(), event.getY()));
            gameInstance.getInputHandler().setTouched();
            Hooks.call(Hooks.TAP);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            gameInstance.getInputHandler().reset();
        }

        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sp =  getSharedPreferences("play_services",MODE_PRIVATE);
        if(!sp.getBoolean("declined",false)) {
            App.connectToPlayServices();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Hooks.call(Hooks.RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Hooks.call(Hooks.STOP_ALL);
    }

    public static GameActivity getInstance() {
        return instance;
    }

    public Handler getHandler() {
        return mHandler;
    }
}