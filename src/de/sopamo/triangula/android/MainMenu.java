package de.sopamo.triangula.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by Benjamin on 03.04.14.
 */
public class MainMenu extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);

        final MainMenu that = this;

        final Button playButton = (Button) findViewById(R.id.playbutton);


        playButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Intent toMain = new Intent(that, MainActivity.class);
                        startActivity(toMain);
                        finish();
                    }
                }, 0);
            }
        });
    }
}