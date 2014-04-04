package de.sopamo.triangula.android.levels;

import android.util.Log;
import de.sopamo.triangula.android.LevelChooserActivity;

import java.io.Serializable;

public class OnlineLevel extends BaseLevel implements Level,Serializable {

    String levelString;
    String tag;
    boolean started;
    boolean ready;
    private long timestamp;

    public OnlineLevel(String tag) {
        this.tag = tag;
    }

    @Override
    public String getLevelString() {
        return levelString;
    }

    public void setLevelString(String levelString) {
        this.levelString = levelString;
    }

    public OnlineLevel load() {
        if(levelString != null && !levelString.equals("")) return this;
        new OnlineData(this).execute("http://triangula.papaya-fiction.com/levels/" +
                tag + ".txt");
        return this;
    }

    public void setReady() {
        ready = true;
        if(started) {
            start();
        }
    }

    public void start() {
        if(ready) {
            LevelChooserActivity.level = this;
            LevelChooserActivity.startGame();
        } else {
            started = true;
        }
    }
}
