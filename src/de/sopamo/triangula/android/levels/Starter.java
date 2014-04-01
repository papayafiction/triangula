package de.sopamo.triangula.android.levels;

import de.sopamo.box2dbridge.IWorld;
import de.sopamo.triangula.android.geometry.GameShape;

import java.util.List;
import java.util.Map;

public class Starter extends BaseLevel implements Level {

    public String getLevelString() {
        return "{\"version\":2,\"name\":\"angle\",\"created_at\":\"2014/04/01 23:42:16\",\"triangles\":[{\"x\":124,\"y\":94,\"size\":100,\"angle\":0},{\"x\":216.88055419921875,\"y\":86.88055419921875,\"size\":100,\"angle\":\"-8.88\"},{\"x\":305.323486328125,\"y\":74.32350158691406,\"size\":100,\"angle\":\"-35.19\"}],\"colors\":[\"00748E\",\"E3DFBB\",\"F4BA4D\",\"E3753C\",\"DA3B3A\"]}";
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
