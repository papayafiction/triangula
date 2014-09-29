package de.sopamo.triangula.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.levels.Level;
import de.sopamo.triangula.android.levels.Level1;

public class SuccessScreenActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_screen);
        final Intent menu = new Intent(this,MainMenu.class);
        final Button button = (Button) findViewById(R.id.menu_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(menu);
            }
        });
        final SuccessScreenActivity that = this;
        final Button next_level_button = (Button) findViewById(R.id.next_level);
        next_level_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent startLevel = new Intent(that,GameActivity.class);
                Level level = null;
                try {
                    level = (Level) GameImpl.getNextLevel().newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                startLevel.putExtra("level", (java.io.Serializable) level);
                that.startActivity(startLevel);
            }
        });
    }
}
