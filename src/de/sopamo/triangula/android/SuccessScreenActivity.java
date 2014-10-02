package de.sopamo.triangula.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.levels.Level;

import java.util.ArrayList;
import java.util.List;

public class SuccessScreenActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks {

    private SuccessScreenActivity that;
    private Handler mHandler;
    private GoogleApiClient mGoogleApiClient;
    private boolean mAutoStartSignInFlow = true;
    private boolean mResolvingConnectionFailure = false;
    private List<String> achievements = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_screen);
        that = this;
        Bundle bundle = getIntent().getExtras();

        mGoogleApiClient = new GoogleApiClient.Builder(this).addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this).addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN).
                        addApi(Games.API).addScope(Games.SCOPE_GAMES).build();
        Level level = null;
        mHandler = new Handler();
        if(bundle != null) {
            level = (Level) bundle.get("level");
            List<String> achievements = (List<String>) bundle.getSerializable("achievements");
            if(achievements != null) {
                this.achievements = achievements;
            }
        }
        final Button menuButton = (Button) findViewById(R.id.menu_button);

        final Intent menu = new Intent(this,MainMenu.class);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(menu);
            }
        });

        Class nextLevel = GameImpl.getNextLevel();
        if(level != null && nextLevel != null) {
            final Button nextLevelButton = (Button) findViewById(R.id.next_level);
            nextLevelButton.setVisibility(View.VISIBLE);
            final Intent nextGame = new Intent(this,GameActivity.class);
            try {
                nextGame.putExtra("level",(Level)nextLevel.newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            nextLevelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(nextGame);
                    that.finish();
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(MainMenu.isSignedIn()) mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mGoogleApiClient.isConnected()) unlock();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                unlock();
            }
        },200);
    }

    private void unlock() {
        if(achievements != null) {
            for(int i=0;i<achievements.size(); i++) {
                Games.Achievements.unlockImmediate(mGoogleApiClient,achievements.get(i));
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            return;
        }
        if ( mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mResolvingConnectionFailure = true;
            if (!BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient, connectionResult,
                    9001, "Fehler beim einloggen")) {
                return;
            }
            mResolvingConnectionFailure = false;
        }

    }
}
