package de.sopamo.triangula.android.levels;

import de.sopamo.box2dbridge.IWorld;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.mechanics.Entity;
import de.sopamo.triangula.android.game.models.Spikes;
import de.sopamo.triangula.android.geometry.GameShape;

import java.util.List;
import java.util.Map;

public class Starter extends BaseLevel implements Level {

    public String getLevelString() {
        return "{\"version\":2,\"name\":\"adgasdg\",\"created_at\":\"2014/04/02 02:14:53\",\"triangles\":[{\"x\":343,\"y\":128,\"size\":100,\"angle\":0},{\"x\":296.25,\"y\":149.25,\"size\":100,\"angle\":\"47.82\"},{\"x\":177,\"y\":202,\"size\":100,\"angle\":0},{\"x\":405.28125,\"y\":147.28125,\"size\":100,\"angle\":\"-35.35\"},{\"x\":0,\"y\":0,\"size\":100,\"angle\":0}],\"colors\":[\"00748E\",\"E3DFBB\",\"F4BA4D\",\"E3753C\",\"DA3B3A\"]}";
    }

    @Override
    public void make(GameImpl game) {
        this.world = game.getWorld();
        this.ground = world.getGroundBody();
        this.entities = game.getEntities();
        this.gsl = game.getGsl();

        super.make(world,gsl);

        new Spikes(game,0,-2);

        parseLevel();

        makeTriangles((List)jsonData.get("triangles"));
    }
}
