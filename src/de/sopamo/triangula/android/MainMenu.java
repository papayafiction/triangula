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
import com.google.android.gms.games.Games;

public class MainMenu extends FragmentActivity {

    private static boolean mAutoStartSignInFlow;

    @Override
    protected void onStart() {
        super.onStart();
        if(mAutoStartSignInFlow) App.connectToPlayServices();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.setActivityContext(this);
        setContentView(R.layout.mainmenu);
        SharedPreferences sp =  getSharedPreferences("play_services",MODE_PRIVATE);
        mAutoStartSignInFlow = !sp.getBoolean("declined",false);

        final MainMenu that = this;

        final Button playButton = (Button) findViewById(R.id.playbutton);
        final Button aboutButton = (Button) findViewById(R.id.aboutButton);
        final Button levelButton = (Button) findViewById(R.id.levelbutton);
        final Button achievementButton = (Button) findViewById(R.id.achievementbutton);


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
                Log.e("CLICKED", "About wurde geklickt");
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(that);

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage("triangula is made by papaya fiction.")
                        .setTitle("About");

                builder.setNegativeButton("Thank you, guys!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("I love you, guys!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("http://papaya-fiction.com/"));
                        startActivity(i);
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