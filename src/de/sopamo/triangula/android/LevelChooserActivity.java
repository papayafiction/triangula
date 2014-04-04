package de.sopamo.triangula.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import de.sopamo.triangula.android.levels.OnlineLevel;

public class LevelChooserActivity extends Activity {

    public static OnlineLevel level;
    private static Button button;
    private static LevelChooserActivity that;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        that = this;
        setContentView(R.layout.level_chooser);

        final Button button = (Button)findViewById(R.id.startLevelButton);
        LevelChooserActivity.button = button;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setActivated(false);
                TextView textView = (TextView) findViewById(R.id.level_text);
                level = new OnlineLevel(textView.getText().toString());
            }
        });
    }

    public static void startGame() {
        if(level.getLevelString() == null ||
           level.getLevelString().equals("")) {
            Toast toast = Toast.makeText(that,"ERROR",Toast.LENGTH_SHORT);
            toast.show();
            button.setActivated(true);
            return;
        }
        button.setActivated(true);
        final Intent startLevel = new Intent(that,GameActivity.class);
        startLevel.putExtra("level",level);
        that.startActivity(startLevel);
    }

}
