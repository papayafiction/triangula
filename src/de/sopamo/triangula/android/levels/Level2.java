package de.sopamo.triangula.android.levels;

import de.sopamo.triangula.android.GameActivity;
import de.sopamo.triangula.android.PGRenderer;
import de.sopamo.triangula.android.R;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.models.Image;
import de.sopamo.triangula.android.tools.Hooks;
import org.jbox2d.common.Vec2;

import java.io.Serializable;
import java.util.concurrent.Callable;

public class Level2 extends BaseLevel implements Level,Serializable {

    String levelString = "{\"version\":4,\"name\":\"level2\",\"created_at\":\"2014/04/04 12:44:39\",\"triangles\":[{\"x\":231.40625,\"y\":0.90625,\"size\":268,\"angle\":\"253.81\"},{\"x\":148.078125,\"y\":17.578125,\"size\":100,\"angle\":\"52.51\"},{\"x\":188.5,\"y\":0,\"size\":100,\"angle\":0},{\"x\":228.03125,\"y\":13.53125,\"size\":105,\"angle\":\"-53.39\"},{\"x\":-155.125,\"y\":342.84375,\"size\":215,\"angle\":\"-63.96\"},{\"x\":458.609375,\"y\":299.109375,\"size\":198,\"angle\":\"90.67\"},{\"x\":458.203125,\"y\":201.703125,\"size\":195,\"angle\":\"-89.37\"}],\"spikes\":[],\"doors\":[],\"bombs\":[{\"x\":97.5,\"y\":466},{\"x\":124.5,\"y\":467},{\"x\":149.5,\"y\":467},{\"x\":171.5,\"y\":467},{\"x\":201.5,\"y\":468},{\"x\":223.5,\"y\":469},{\"x\":241.5,\"y\":470},{\"x\":262.5,\"y\":471},{\"x\":292.5,\"y\":471},{\"x\":334.5,\"y\":402},{\"x\":335.5,\"y\":379},{\"x\":335.5,\"y\":360},{\"x\":335.5,\"y\":342},{\"x\":335.5,\"y\":320},{\"x\":336.5,\"y\":300},{\"x\":337.5,\"y\":277},{\"x\":338.5,\"y\":248},{\"x\":338.5,\"y\":225},{\"x\":338.5,\"y\":199}],\"exits\":[{\"x\":399,\"y\":453}],\"colors\":[\"00748E\",\"E3DFBB\",\"F4BA4D\",\"E3753C\",\"DA3B3A\"]}";
    private Image image;

    public String getLevelString() {
        return levelString;
    }

    public Level2() {
        try {
            GameImpl.setNextLevel(Class.forName("de.sopamo.triangula.android.levels.Level3"));
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
        image = new Image(4,2,new Vec2(2.8f,-5), R.drawable.beware_bombs);
        image.loadGLTexture(PGRenderer.getInstance().getGl(), GameActivity.getInstance());
    }

    @Override
    public void end() {
        image = null;
    }
}
