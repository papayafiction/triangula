package de.sopamo.triangula.android.levels.official;

import android.util.Log;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.levels.BaseLevel;
import de.sopamo.triangula.android.levels.BaseOfficialLevel;
import de.sopamo.triangula.android.levels.Level;
import org.json.JSONException;

import java.io.Serializable;


public class Starter extends BaseOfficialLevel implements Level,Serializable {


    String levelString = "{\"version\":4,\"name\":\"geil\",\"created_at\":\"2014/04/03 20:39:35\",\"triangles\":[{\"x\":0,\"y\":276,\"size\":100,\"angle\":0},{\"x\":80,\"y\":276,\"size\":100,\"angle\":0},{\"x\":0,\"y\":122.3125,\"size\":100,\"angle\":\"179.03\"},{\"x\":89.875,\"y\":123.875,\"size\":100,\"angle\":\"178.48\"},{\"x\":98,\"y\":379,\"size\":100,\"angle\":0},{\"x\":273,\"y\":374,\"size\":100,\"angle\":0},{\"x\":188,\"y\":379,\"size\":100,\"angle\":0},{\"x\":111.21875,\"y\":25.609375,\"size\":100,\"angle\":\"179.55\"},{\"x\":193.59375,\"y\":26.796875,\"size\":100,\"angle\":\"178.85\"},{\"x\":267.25,\"y\":29.25,\"size\":100,\"angle\":\"176.62\"},{\"x\":267.796875,\"y\":276.796875,\"size\":100,\"angle\":\"181.39\"},{\"x\":266.28125,\"y\":79.28125,\"size\":100,\"angle\":\"180.13\"},{\"x\":372,\"y\":374,\"size\":100,\"angle\":0},{\"x\":468,\"y\":364,\"size\":100,\"angle\":0},{\"x\":358,\"y\":162,\"size\":100,\"angle\":0},{\"x\":398.265625,\"y\":180.265625,\"size\":100,\"angle\":\"-51.96\"},{\"x\":464.5625,\"y\":147.5625,\"size\":100,\"angle\":\"13.38\"},{\"x\":525.078125,\"y\":245.078125,\"size\":100,\"angle\":\"89.23\"},{\"x\":526.34375,\"y\":197.34375,\"size\":101,\"angle\":\"269.07\"},{\"x\":508.921875,\"y\":157.921875,\"size\":100,\"angle\":\"-39.22\"},{\"x\":525.328125,\"y\":294.328125,\"size\":100,\"angle\":\"-90.00\"}],\"spikes\":[{\"x\":138,\"y\":32,\"size\":113,\"angle\":0,\"count\":2},{\"x\":336.28125,\"y\":455.28125,\"size\":99,\"angle\":\"180.18\",\"count\":2}],\"doors\":[{\"door\":{\"x\":266,\"y\":179,\"angle\":0,\"size\":100},\"switch\":{\"x\":225,\"y\":340}}],\"bombs\":[{\"x\":247,\"y\":221},{\"x\":214,\"y\":309},{\"x\":246,\"y\":308},{\"x\":215,\"y\":266},{\"x\":248,\"y\":264},{\"x\":216,\"y\":223}],\"exits\":[{\"x\":468,\"y\":266}],\"colors\":[\"00748E\",\"E3DFBB\",\"F4BA4D\",\"E3753C\",\"DA3B3A\"]}";


    public Starter() {
    }
    public String getLevelString() {
        return levelString;
    }

    @Override
    public void end() {
        try {
            GameImpl.setNextLevel(Class.forName("de.sopamo.triangula.android.levels.official.Ascending"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
