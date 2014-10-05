package de.sopamo.triangula.android.levels.official;

import de.sopamo.triangula.android.GameActivity;
import de.sopamo.triangula.android.PGRenderer;
import de.sopamo.triangula.android.R;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.models.Image;
import de.sopamo.triangula.android.levels.BaseOfficialLevel;
import de.sopamo.triangula.android.levels.Level;
import org.jbox2d.common.Vec2;

import java.io.Serializable;

public class Level4 extends BaseOfficialLevel implements Level,Serializable {


    String levelString = "{\"version\":4,\"name\":\"level4\",\"created_at\":\"2014/04/04 13:15:31\",\"triangles\":[{\"x\":182.578125,\"y\":24.578125,\"size\":352,\"angle\":\"179.82\"},{\"x\":495,\"y\":302,\"size\":100,\"angle\":0},{\"x\":308.21875,\"y\":194.21875,\"size\":100,\"angle\":\"179.93\"},{\"x\":646.5,\"y\":367,\"size\":132,\"angle\":\"180.00\"},{\"x\":637.5,\"y\":217,\"size\":219,\"angle\":0},{\"x\":461.515625,\"y\":47.015625,\"size\":187,\"angle\":\"179.40\"},{\"x\":564.703125,\"y\":445.703125,\"size\":103,\"angle\":\"88.97\"},{\"x\":531.5,\"y\":-1,\"size\":100,\"angle\":0}],\"spikes\":[{\"x\":398.6875,\"y\":316,\"size\":79,\"angle\":\"180.50\",\"count\":1},{\"x\":214.359375,\"y\":569,\"size\":66,\"angle\":\"180.49\",\"count\":1},{\"x\":567.375,\"y\":-75,\"size\":98,\"angle\":\"-41.99\",\"count\":1},{\"x\":259.328125,\"y\":377,\"size\":98,\"angle\":\"-10.76\",\"count\":1}],\"doors\":[{\"door\":{\"x\":310,\"y\":0,\"angle\":0,\"size\":100},\"switch\":{\"x\":578,\"y\":293}}],\"bombs\":[],\"exits\":[{\"x\":444,\"y\":153}],\"colors\":[\"00748E\",\"E3DFBB\",\"F4BA4D\",\"E3753C\",\"DA3B3A\"]}";
    private Image image;

    public String getLevelString() {
        return levelString;
    }

    public Level4() {
    }

    @Override
    public void postDraw() {
        if(image != null) {
            image.draw();
        }
    }

    @Override
    public void postSurfaceCreated() {
        image = new Image(4,2,new Vec2(15.8f,-2), R.drawable.timetravel_info);
        image.loadGLTexture(PGRenderer.getInstance().getGl(), GameActivity.getInstance());
    }

    @Override
    public void end() {
        image = null;
        try {
            GameImpl.setNextLevel(Class.forName("de.sopamo.triangula.android.levels.official.Movethetime"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        achievments.add(GameActivity.getInstance().getString(R.string.achievement_level_4));
    }
}
