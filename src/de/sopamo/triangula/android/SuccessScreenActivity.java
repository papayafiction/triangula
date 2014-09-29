package de.sopamo.triangula.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.levels.Level;
import de.sopamo.triangula.android.levels.Level1;
import de.sopamo.triangula.android.levels.Level;

public class SuccessScreenActivity extends Activity {

    private SuccessScreenActivity that;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        that = this;
        Bundle bundle = getIntent().getExtras();
        Level level = null;
        if(bundle != null) {
            level = (Level) bundle.get("level");
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

        if(level != null && level.getNextLevel() != null) {
            final Button nextLevelButton = (Button) findViewById(R.id.next_level);
            nextLevelButton.setVisibility(View.VISIBLE);
            final Intent nextGame = new Intent(this,GameActivity.class);
            try {
                nextGame.putExtra("level",(Level)level.getNextLevel().newInstance());
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
}
