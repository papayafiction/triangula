package de.sopamo.triangula.android.levels;

import de.sopamo.box2dbridge.IWorld;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.mechanics.Entity;
import de.sopamo.triangula.android.game.models.Spikes;
import de.sopamo.triangula.android.geometry.GameShape;
import org.jbox2d.common.Vec2;

import java.util.List;
import java.util.Map;

public class Starter extends BaseLevel implements Level {

    public String getLevelString() {
        return "{\"version\":3,\"name\":\"yolo\",\"created_at\":\"2014/04/02 19:56:25\",\"triangles\":[{\"x\":0,\"y\":0,\"size\":100,\"angle\":0}],\"spikes\":[{\"x\":0,\"y\":0,\"size\":100,\"angle\":0,\"count\":10},{\"x\":-550.890625,\"y\":207.6875,\"size\":99.71428571428571,\"angle\":\"186.76\",\"count\":7},{\"x\":662.03125,\"y\":-18.328125,\"size\":99.71428571428571,\"angle\":\"165.34\",\"count\":7},{\"x\":463,\"y\":352.28125,\"size\":62.142857142857146,\"angle\":\"179.85\",\"count\":7},{\"x\":810.171875,\"y\":324.84375,\"size\":99.33333333333333,\"angle\":\"201.33\",\"count\":3}],\"colors\":[\"00748E\",\"E3DFBB\",\"F4BA4D\",\"E3753C\",\"DA3B3A\"]}";
    }

    @Override
    public void make(GameImpl game) {
        this.world = game.getWorld();
        this.ground = world.getGroundBody();
        this.entities = game.getEntities();
        this.gsl = game.getGsl();
        this.game = game;

        super.make(world,gsl);

        parseLevel();
        makeSpikes((List)jsonData.get("spikes"));
        makeTriangles((List)jsonData.get("triangles"));
    }
}
