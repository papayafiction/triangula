package de.sopamo.triangula.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.levels.Level;
import de.sopamo.triangula.android.levels.Level1;
import de.sopamo.triangula.android.levels.Level;

import java.util.ArrayList;
import java.util.List;

public class SuccessScreenActivity extends Activity implements GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks {

    private SuccessScreenActivity that;
    private GoogleApiClient mGoogleApiClient;
    private boolean mAutoStartSignInFlow = true;
    private boolean mResolvingConnectionFailure = false;
    private List<String> achievements = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        that = this;
        Bundle bundle = getIntent().getExtras();

        mGoogleApiClient = new GoogleApiClient.Builder(this).addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this).addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN).
                        addApi(Games.API).addScope(Games.SCOPE_GAMES).build();
        Level level = null;
        if(bundle != null) {
            level = (Level) bundle.get("level");
            List<String> achievements = (List<String>) bundle.getSerializable("achievements");
            if(achievements != null) {
                this.achievements = achievements;
            }
        }
        setContentView(R.layout.success_screen);
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
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if(achievements != null) {
            for(int i=0;i<achievements.size(); i++) {
                Games.Achievements.unlock(mGoogleApiClient,achievements.get(i));
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
                mResolvingConnectionFailure = false;
            }
        }

    }
}
