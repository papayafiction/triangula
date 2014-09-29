package de.sopamo.triangula.android.levels;

import de.sopamo.triangula.android.GameActivity;
import de.sopamo.triangula.android.PGRenderer;
import de.sopamo.triangula.android.R;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.models.Image;
import org.jbox2d.common.Vec2;

import java.io.Serializable;

/**
 * Created by paulmohr on 05.04.14.
 */
public class Movethetime {

    String levelString = "{\"version\":4,\"name\":\"movethetime\",\"created_at\":\"2014/04/04 12:58:32\",\"triangles\":[{\"x\":-0.5,\"y\":0,\"size\":194,\"angle\":0},{\"x\":196.5,\"y\":196,\"size\":100,\"angle\":0},{\"x\":265.765625,\"y\":214.265625,\"size\":113,\"angle\":\"63.28\"},{\"x\":306.515625,\"y\":185.015625,\"size\":112,\"angle\":\"10.25\"},{\"x\":357.5,\"y\":77,\"size\":219,\"angle\":0},{\"x\":374.546875,\"y\":5.046875,\"size\":100,\"angle\":\"63.78\"},{\"x\":-1.5,\"y\":416,\"size\":587,\"angle\":0},{\"x\":493.5,\"y\":232,\"size\":189,\"angle\":0}],\"spikes\":[],\"doors\":[{\"door\":{\"x\":146,\"y\":96,\"angle\":0,\"size\":100},\"switch\":{\"x\":532,\"y\":406}}],\"bombs\":[],\"exits\":[{\"x\":338,\"y\":167}],\"colors\":[\"00748E\",\"E3DFBB\",\"F4BA4D\",\"E3753C\",\"DA3B3A\"]}";

    public String getLevelString() {
        return levelString;
    }

    public Movethetime() {
        try {
            GameImpl.setNextLevel(Class.forName("de.sopamo.triangula.android.levels.Doorception"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
