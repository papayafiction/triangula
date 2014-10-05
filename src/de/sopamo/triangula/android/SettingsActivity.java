package de.sopamo.triangula.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends Activity {
    private Button quality;
    private Button mute;
    private Button raycast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_screen);

        quality = (Button) findViewById(R.id.settings_quality_button);
        mute = (Button) findViewById(R.id.settings_mute_button);
        raycast = (Button) findViewById(R.id.settings_raycast_button);

        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(App.getSetting("muted").equals("true")) {
                    mute.setText("Sound Is On");
                    App.unMuteAudio();
                    App.setSetting("muted","false");
                } else {
                    mute.setText("Sound Is off");
                    App.muteAudio();
                    App.setSetting("muted","true");
                }
            }
        });

        raycast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(App.getSetting("raycast").equals("true")) {
                    raycast.setText("No Shadows");
                    App.setSetting("raycast","false");
                } else {
                    raycast.setText("Full Shadows");
                    App.setSetting("raycast","true");
                }
            }
        });

        quality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(App.getSetting("quality").equals("true")) {
                    quality.setText("Low Quality");
                    App.setSetting("quality","false");
                } else {
                    quality.setText("High Quality");
                    App.setSetting("quality","true");
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(App.getSetting("muted").equals("true")) mute.setText("Sound Is Off");
        if(App.getSetting("raycast").equals("false")) raycast.setText("No Shadows");
        if(App.getSetting("quality").equals("false")) quality.setText("Low Quality");
    }
}
