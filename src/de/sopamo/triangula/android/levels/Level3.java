package de.sopamo.triangula.android.levels;

import de.sopamo.triangula.android.GameActivity;
import de.sopamo.triangula.android.PGRenderer;
import de.sopamo.triangula.android.R;
import de.sopamo.triangula.android.game.models.Image;
import org.jbox2d.common.Vec2;

import java.io.Serializable;

public class Level3 extends BaseLevel implements Level,Serializable {

    String levelString = "{\"version\":4,\"name\":\"level3\",\"created_at\":\"2014/04/04 12:51:44\",\"triangles\":[{\"x\":0.5,\"y\":0,\"size\":151,\"angle\":0},{\"x\":150.5,\"y\":0,\"size\":124,\"angle\":0},{\"x\":273.5,\"y\":-1,\"size\":100,\"angle\":0},{\"x\":372.5,\"y\":0,\"size\":74,\"angle\":0},{\"x\":446.5,\"y\":0,\"size\":48,\"angle\":0},{\"x\":494.5,\"y\":0,\"size\":30,\"angle\":0},{\"x\":344.5,\"y\":399,\"size\":100,\"angle\":\"180.00\"},{\"x\":326.5,\"y\":202,\"size\":100,\"angle\":0},{\"x\":344.5,\"y\":302,\"size\":100,\"angle\":0},{\"x\":294.5,\"y\":400,\"size\":100,\"angle\":0},{\"x\":51.5,\"y\":297,\"size\":203,\"angle\":\"180.00\"},{\"x\":203.34375,\"y\":341.84375,\"size\":158,\"angle\":\"180.24\"},{\"x\":426.265625,\"y\":145.265625,\"size\":58,\"angle\":\"179.60\"},{\"x\":509.5,\"y\":30,\"size\":100,\"angle\":0},{\"x\":507.46875,\"y\":78.96875,\"size\":155,\"angle\":\"-52.68\"}],\"spikes\":[],\"doors\":[{\"door\":{\"x\":273,\"y\":96,\"angle\":0,\"size\":100},\"switch\":{\"x\":362,\"y\":391}}],\"bombs\":[{\"x\":359.5,\"y\":291},{\"x\":225.5,\"y\":421}],\"exits\":[{\"x\":484,\"y\":122}],\"colors\":[\"00748E\",\"E3DFBB\",\"F4BA4D\",\"E3753C\",\"DA3B3A\"]}";
    private Image image;

    @Override
    public Class<?> getNextLevel() {
        return Level4.class;
    }

    public String getLevelString() {
        return levelString;
    }

    @Override
    public void postDraw() {
        if(image != null) {
            image.draw();
        }
    }

    @Override
    public void postSurfaceCreated() {
        image = new Image(4,2,new Vec2(-6,0), R.drawable.door_info);
        image.loadGLTexture(PGRenderer.getInstance().getGl(), GameActivity.getInstance());
    }

    @Override
    public void end() {
        image = null;
    }
}
