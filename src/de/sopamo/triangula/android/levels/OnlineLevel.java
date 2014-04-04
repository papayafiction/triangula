package de.sopamo.triangula.android.levels;

import android.util.Log;

import java.io.Serializable;

public class OnlineLevel extends BaseLevel implements Level,Serializable {

    String levelString;

    public OnlineLevel(String tag) {
            new OnlineData(this).execute("http://triangula.papaya-fiction.com/levels/" +
                    tag + ".txt");
    }

    @Override
    public String getLevelString() {
        return levelString;
    }

    public void setLevelString(String levelString) {
        this.levelString = levelString;
    }
}
