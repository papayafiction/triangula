package de.sopamo.triangula.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.sopamo.triangula.android.adapters.LevelAdapter;
import de.sopamo.triangula.android.levels.*;

import java.util.ArrayList;
import java.util.List;

public class LevelChooserActivity extends Activity {

    public static Level level;
    private static Button button;
    private static LevelChooserActivity that;

    private List<Level> levels = new ArrayList<Level>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        that = this;
        setContentView(R.layout.level_chooser);

        levels.add(new Skihuette());
        levels.add(new Level1());
        levels.add(new Level2());
        levels.add(new Level3());
        levels.add(new Level4());
        levels.add(new Starter());
        levels.add(new OnlineLevel("movethetime"));

        ListView view = (ListView) findViewById(R.id.level_list);
        view.setAdapter(new LevelAdapter(this,levels));

        final Button button = (Button)findViewById(R.id.startLevelButton);
        LevelChooserActivity.button = button;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setActivated(false);
                TextView textView = (TextView) findViewById(R.id.level_text);
                new OnlineLevel(textView.getText().toString()).load().start();
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
        startLevel.putExtra("level", (java.io.Serializable) level);
        that.startActivity(startLevel);
    }

}
