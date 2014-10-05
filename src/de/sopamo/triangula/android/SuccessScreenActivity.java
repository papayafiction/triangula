package de.sopamo.triangula.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.levels.BaseOnlineLevel;
import de.sopamo.triangula.android.levels.Level;

import java.util.ArrayList;
import java.util.List;

public class SuccessScreenActivity extends FragmentActivity implements App.ConnectionCallback{

    private SuccessScreenActivity that;
    private Handler mHandler;
    private List<String> achievements = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.setActivityContext(this);
        setContentView(R.layout.success_screen);
        that = this;
        Bundle bundle = getIntent().getExtras();

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

        if(level instanceof BaseOnlineLevel) {
            final int next = ((BaseOnlineLevel) level).getNextLevel();
            if(next <= App.getLevelList().size()-1) {
                final Button nextLevelButton = (Button) findViewById(R.id.next_level);
                nextLevelButton.setVisibility(View.VISIBLE);
                nextLevelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Level Onlinelevel = App.getLevelList().get(next);
                        Intent online = new Intent(that,StartLevelService.class);
                        online.putExtra("level",Onlinelevel);
                        that.startService(online);
                    }
                });
                return;
            }
        }
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
    protected void onStart() {
        super.onStart();
        SharedPreferences sp =  getSharedPreferences("play_services",MODE_PRIVATE);
        if(!sp.getBoolean("declined",false)) {
            App.connectToPlayServices(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(App.connectedToPlayServices()) unlock();
    }

    private void unlock() {
        if(achievements != null) {
            for(int i=0;i<achievements.size(); i++) {
                Games.Achievements.unlockImmediate(App.getGoogleApiClient(),achievements.get(i));
            }
        }
    }

    @Override
    public void onConnected(GoogleApiClient client) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                unlock();
            }
        },200);
    }

    @Override
    public void onConnectionFailed() {

    }
}
