package de.sopamo.triangula.android.levels;

import de.sopamo.box2dbridge.IWorld;
import de.sopamo.triangula.android.geometry.GameShape;

import java.util.List;
import java.util.Map;

public class Starter extends BaseLevel implements Level {

    public String getLevelString() {
        return "{\"version\":2,\"name\":\"adgasdg\",\"created_at\":\"2014/04/02 02:14:53\",\"triangles\":[{\"x\":343,\"y\":128,\"size\":100,\"angle\":0},{\"x\":296.25,\"y\":149.25,\"size\":100,\"angle\":\"47.82\"},{\"x\":177,\"y\":202,\"size\":100,\"angle\":0},{\"x\":405.28125,\"y\":147.28125,\"size\":100,\"angle\":\"-35.35\"},{\"x\":0,\"y\":0,\"size\":100,\"angle\":0}],\"colors\":[\"00748E\",\"E3DFBB\",\"F4BA4D\",\"E3753C\",\"DA3B3A\"]}";
    }

    @Override
    public void make(IWorld world,List<GameShape> gsl) {
        this.ground = world.getGroundBody();
        this.world = world;
        this.gsl = gsl;

        super.make(world,gsl);

        parseLevel();

        makeTriangles((List)jsonData.get("triangles"));
    }
}
