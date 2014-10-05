package de.sopamo.triangula.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import com.google.android.gms.games.Games;
import de.sopamo.triangula.android.tools.Hooks;

public class MainMenu extends FragmentActivity {

    private static boolean mAutoStartSignInFlow;
    private static boolean mMuted;

    private ImageButton muteButton;

    protected void onStart() {
        super.onStart();
        if(mAutoStartSignInFlow) App.connectToPlayServices();
    }

    private void mute() {
        Hooks.call(Hooks.MUTE);
        muteButton.setImageResource(R.drawable.sound_off);
        mMuted = true;
    }

    private void unmute() {
        Hooks.call(Hooks.UNMUTE);
        muteButton.setImageResource(R.drawable.sound_on);
        mMuted = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Hooks.call(Hooks.MENU_STOP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Hooks.call(Hooks.MENU_START);
        mMuted = App.getSetting("muted").equals("true");
        if(mMuted) mute();
        else unmute();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.setActivityContext(this);
        setContentView(R.layout.mainmenu);
        final SharedPreferences sp =  getSharedPreferences("play_services",MODE_PRIVATE);
        mAutoStartSignInFlow = !sp.getBoolean("declined",false);

        final MainMenu that = this;

        final Button playButton = (Button) findViewById(R.id.playbutton);
        final Button aboutButton = (Button) findViewById(R.id.aboutButton);
        final Button levelButton = (Button) findViewById(R.id.levelbutton);
        final Button achievementButton = (Button) findViewById(R.id.achievementbutton);
        muteButton = (ImageButton) findViewById(R.id.mutebutton);
        final ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsbutton);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(that,SettingsActivity.class);
                startActivity(intent);
            }
        });

        muteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMuted) unmute();
                else mute();
                App.setSetting("muted", mMuted ? "true" : "false");
            }
        });

        achievementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(App.connectedToPlayServices()) startActivityForResult(Games.Achievements.getAchievementsIntent(App.getGoogleApiClient()),5001);
                else App.connectToPlayServices(true);
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(that);

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage("Triangula is made by Papaya Fiction. Make your own levels at: bit.ly/triangula-editor. All music by Simanfication")
                        .setTitle("About Triangula");

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                        // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        playButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Intent toMain = new Intent(that, GameActivity.class);
                        startActivity(toMain);
                        //finish();
                    }
                }, 0);
            }
        });

        levelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent levelChooser = new Intent(that,LevelChooserActivity.class);
                startActivity(levelChooser);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 9001) {
            App.getContext().onResult(resultCode);
        }
    }
}