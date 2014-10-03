package de.sopamo.triangula.android.levels.official;

import de.sopamo.triangula.android.GameActivity;
import de.sopamo.triangula.android.PGRenderer;
import de.sopamo.triangula.android.R;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.models.Image;
import de.sopamo.triangula.android.levels.BaseLevel;
import de.sopamo.triangula.android.levels.BaseOfficialLevel;
import de.sopamo.triangula.android.levels.Level;
import de.sopamo.triangula.android.tools.Hooks;
import org.jbox2d.common.Vec2;

import java.io.Serializable;
import java.util.concurrent.Callable;

public class Level1 extends BaseOfficialLevel implements Level,Serializable {



    String levelString = "{\"version\":4,\"name\":\"level1\",\"created_at\":\"2014/04/04 12:38:59\",\"triangles\":[{\"x\":1,\"y\":350,\"size\":217,\"angle\":0},{\"x\":218,\"y\":350,\"size\":100,\"angle\":0},{\"x\":318,\"y\":350,\"size\":49,\"angle\":0},{\"x\":367,\"y\":350,\"size\":112,\"angle\":0},{\"x\":128.5,\"y\":1,\"size\":100,\"angle\":\"90.00\"},{\"x\":147.0625,\"y\":40.5625,\"size\":100,\"angle\":\"143.44\"},{\"x\":190.4375,\"y\":46.9375,\"size\":100,\"angle\":\"197.14\"},{\"x\":238.90625,\"y\":33.40625,\"size\":100,\"angle\":\"16.91\"},{\"x\":282.359375,\"y\":39.859375,\"size\":100,\"angle\":\"-36.32\"},{\"x\":348.03125,\"y\":55.53125,\"size\":259,\"angle\":\"26.99\"},{\"x\":168.5,\"y\":236,\"size\":42,\"angle\":0},{\"x\":189.953125,\"y\":235.453125,\"size\":42,\"angle\":\"180.05\"},{\"x\":436.953125,\"y\":300.453125,\"size\":134,\"angle\":\"-52.85\"}],\"spikes\":[],\"doors\":[],\"bombs\":[],\"exits\":[{\"x\":405,\"y\":319}],\"colors\":[\"00748E\",\"E3DFBB\",\"F4BA4D\",\"E3753C\",\"DA3B3A\"]}";
    private Image image;

    public String getLevelString() {
        return levelString;
    }

    public Level1() {
        try {
            GameImpl.setNextLevel(Class.forName("de.sopamo.triangula.android.levels.official.Level2"));
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
        image = new Image(4,2,new Vec2(2.8f,-5), R.drawable.single_tap);
        image.loadGLTexture(PGRenderer.getInstance().getGl(), GameActivity.getInstance());
        Hooks.bind(Hooks.TAP, new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                image = null;
                return null;
            }
        });
    }

    @Override
    public void end() {
        achievments.add(GameActivity.getInstance().getString(R.string.achievement_level_1));
    }
}
