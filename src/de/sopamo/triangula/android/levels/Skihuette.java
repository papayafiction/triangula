package de.sopamo.triangula.android.levels;

import de.sopamo.triangula.android.GameActivity;
import de.sopamo.triangula.android.PGRenderer;
import de.sopamo.triangula.android.R;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.models.Image;
import org.jbox2d.common.Vec2;

import java.io.Serializable;

public class Skihuette extends BaseLevel implements Level,Serializable {

    String levelString = "{\"version\":4,\"name\":\"skihuette\",\"created_at\":\"2014/04/19 18:08:32\",\"triangles\":[{\"x\":89,\"y\":264,\"size\":100,\"angle\":\"194.78\"},{\"x\":170.546875,\"y\":339.546875,\"size\":100,\"angle\":\"207.50\"},{\"x\":287,\"y\":411,\"size\":100,\"angle\":0},{\"x\":395.53125,\"y\":396.53125,\"size\":100,\"angle\":\"13.76\"},{\"x\":499.90625,\"y\":355.90625,\"size\":100,\"angle\":\"29.36\"},{\"x\":588.890625,\"y\":289.890625,\"size\":100,\"angle\":\"43.83\"},{\"x\":700,\"y\":288,\"size\":100,\"angle\":0},{\"x\":740.03125,\"y\":308.03125,\"size\":100,\"angle\":\"-53.31\"},{\"x\":702.15625,\"y\":286.15625,\"size\":222,\"angle\":\"91.58\"},{\"x\":699.234375,\"y\":71.234375,\"size\":219,\"angle\":\"180.28\"},{\"x\":728.34375,\"y\":290.34375,\"size\":193,\"angle\":\"-88.15\"},{\"x\":793,\"y\":287,\"size\":126,\"angle\":0},{\"x\":716.25,\"y\":282.25,\"size\":219,\"angle\":\"179.55\"},{\"x\":659.640625,\"y\":218.640625,\"size\":100,\"angle\":\"46.58\"},{\"x\":212.328125,\"y\":41.328125,\"size\":281,\"angle\":\"38.31\"},{\"x\":293.84375,\"y\":-46.140625,\"size\":293,\"angle\":\"-24.96\"},{\"x\":368.046875,\"y\":40.046875,\"size\":268,\"angle\":\"-27.49\"}],\"spikes\":[],\"doors\":[],\"bombs\":[{\"x\":731,\"y\":55},{\"x\":765,\"y\":26},{\"x\":801,\"y\":15},{\"x\":840,\"y\":32},{\"x\":869,\"y\":62},{\"x\":887,\"y\":86},{\"x\":903,\"y\":113},{\"x\":918,\"y\":145},{\"x\":936,\"y\":178},{\"x\":953,\"y\":197},{\"x\":982,\"y\":220},{\"x\":1009,\"y\":226},{\"x\":1037,\"y\":226}],\"exits\":[],\"colors\":[\"00748E\",\"E3DFBB\",\"F4BA4D\",\"E3753C\",\"DA3B3A\"]}";
    private Image image;

    public String getLevelString() {
        return levelString;
    }

    public Skihuette() {
        try {
            GameImpl.setNextLevel(Class.forName("de.sopamo.triangula.android.levels.Movethetime"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postDraw() {
        if(image != null) {
            image.draw();
        }
    }

    @Override
    public void postSurfaceCreated() {
    }
}
