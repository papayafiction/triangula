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

        String qualitySetting = App.getSetting("quality");

        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(App.getSetting("muted").equals("true")) {
                    mute.setText("Mute");
                    App.unMuteAudio();
                    App.setSetting("muted","false");
                } else {
                    mute.setText("Unmute");
                    App.muteAudio();
                    App.setSetting("muted","true");
                }
            }
        });

        raycast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(App.getSetting("raycast").equals("true")) {
                    raycast.setText("Enable Shadows");
                    App.setSetting("raycast","false");
                } else {
                    raycast.setText("Disable Shadows");
                    App.setSetting("raycast","true");
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(App.getSetting("muted").equals("true")) mute.setText("Unmute");
        if(App.getSetting("raycast").equals("false")) raycast.setText("Enable Shadows");
    }
}
