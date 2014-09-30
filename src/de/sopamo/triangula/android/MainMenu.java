package de.sopamo.triangula.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.google.example.games.basegameutils.GameHelper;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class MainMenu extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

    private static GoogleApiClient mGoogleApiClient;
    private boolean mAutoStartSignInFlow = true;
    private boolean mResolvingConnectionFailure = false;
    private boolean mSignInClicked = false;

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);

        final MainMenu that = this;

        final Button playButton = (Button) findViewById(R.id.playbutton);
        final Button aboutButton = (Button) findViewById(R.id.aboutButton);
        final Button levelButton = (Button) findViewById(R.id.levelbutton);
        final Button wifiButton = (Button) findViewById(R.id.wifibutton);
        final Button loginButton = (Button) findViewById(R.id.login);
        final Button logoutButton = (Button) findViewById(R.id.logout);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignInClicked = true;
                mGoogleApiClient.connect();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAchievement(getString(R.string.achievement_level_1));
            }
        });

        wifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mGoogleApiClient.isConnected()) {
                    startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient),5001);
                }
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

    }

    @Override
    public void onConnectionSuspended(int i) {
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

    public void resetAchievement(String id)
    {
        if( mGoogleApiClient.isConnected() )
        {
            String accountName = Plus.AccountApi.getAccountName(mGoogleApiClient);
            String scopes = "oauth2:https://www.googleapis.com/auth/games";

            new ResetterTask(this, accountName, scopes,id).execute((Void) null);
        }
    }

    private class ResetterTask extends AsyncTask<Void, Void, Void>
    {
        public String mAccountName;
        public String mScope;
        public String id;
        public Context mContext;

        public ResetterTask(Context con, String name, String sc,String id)
        {
            this.id = id;
            mContext = con;
            mAccountName = name;
            mScope = sc;
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                String accesstoken = GoogleAuthUtil.getToken(mContext, mAccountName, mScope);

                HttpClient client = new DefaultHttpClient();
                //Reset leader board:
            /*String leaderboardid = "theleaderboardid";
            HttpPost post = new HttpPost
                    (
                        "https://www.googleapis.com"+
                        "/games/v1management"+
                        "/leaderboards/"+
                        leaderboardid+
                        "/scores/reset?access_token="+accesstoken
                    );*/

                //Reset a single achievement like this:
            /*
            String acheivementid = "acheivementid";
            HttpPost post = new HttpPost
                    (
                        "https://www.googleapis.com"+
                        "/games/v1management"+
                        "/achievements/"+
                        acheivementid+
                        "/reset?access_token="+accesstoken
                    );*/

                //This resets all achievements:
                HttpPost post = new HttpPost
                        (
                                "https://www.googleapis.com"+
                                        "/games/v1management"+
                                        "/achievements"+
                                        "/"+id+
                                        "/reset?access_token="+accesstoken
                        );


                client.execute(post);
                Log.w("yolo", "Reset achievements done.");
            }
            catch(Exception e)
            {
                Log.e("yolo", "Failed to reset: " + e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            //Launch activity to refresh data on client.
            //NOTE: Incremental achievements will look like they are not reset.
            //However, next time you and some steps it will start from 0 and
            //gui will look ok.
            startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), 0);
        }
    }


}