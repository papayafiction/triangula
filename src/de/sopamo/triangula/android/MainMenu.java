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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;

public class MainMenu extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

    private static GoogleApiClient mGoogleApiClient;
    private boolean mAutoStartSignInFlow = true;
    private boolean mResolvingConnectionFailure = false;
    private boolean mSignInClicked = false;
    private boolean mAchievements = false;
    private static boolean mSignedIn = false;
    private SharedPreferences.Editor mSharedPreferences;

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        SharedPreferences sp =  getSharedPreferences("play_services",MODE_PRIVATE);
        mAutoStartSignInFlow = !sp.getBoolean("declined",false);
        mSharedPreferences = sp.edit();



        final MainMenu that = this;

        final Button playButton = (Button) findViewById(R.id.playbutton);
        final Button aboutButton = (Button) findViewById(R.id.aboutButton);
        final Button levelButton = (Button) findViewById(R.id.levelbutton);
        final Button achievementButton = (Button) findViewById(R.id.achievementbutton);


        achievementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mGoogleApiClient.isConnected()) {
                    mSignInClicked = true;
                    mAchievements = true;
                    mGoogleApiClient.connect();
                    return;
                }
                startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient),5001);
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES).build();

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
    public void onConnected(Bundle bundle) {
        mSignedIn = true;
        mSharedPreferences.putBoolean("declined",false);
        mSharedPreferences.commit();
        if(mAchievements) {
            mAchievements = false;
            startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient),5001);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mSignedIn = false;
        mSharedPreferences.putBoolean("declined",true);
        mSharedPreferences.commit();
        if (mResolvingConnectionFailure) {
            return;
        }
        if ( mAutoStartSignInFlow || mSignInClicked) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;
            if (!BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient, connectionResult,
                    9001, "Fehler beim einloggen")) {
                return;
            }
            mResolvingConnectionFailure = false;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 9001) {
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    public static boolean isSignedIn() {
        return mSignedIn;
    }


}