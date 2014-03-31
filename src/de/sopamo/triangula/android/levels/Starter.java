package de.sopamo.triangula.android.levels;

import de.sopamo.box2dbridge.IWorld;
import de.sopamo.triangula.android.geometry.GameShape;

import java.util.List;
import java.util.Map;

public class Starter extends BaseLevel implements Level {

    public String getLevelString() {
        return "{\"version\":1,\"name\":\"test\",\"created_at\":\"2014/03/31 12:18:02\",\"triangles\":[{\"x\":0,\"y\":0,\"size\":100},{\"x\":101,\"y\":0,\"size\":498},{\"x\":0,\"y\":399,\"size\":100},{\"x\":0,\"y\":299,\"size\":100},{\"x\":0,\"y\":199,\"size\":100},{\"x\":0,\"y\":100,\"size\":100}],\"colors\":[\"00748E\",\"E3DFBB\",\"F4BA4D\",\"E3753C\",\"DA3B3A\"]}";
    }

    @Override
    public void make(IWorld world,List<GameShape> gsl) {
        this.ground = world.getGroundBody();
        this.world = world;
        this.gsl = gsl;

        super.make(world,gsl);

        Map levelJson = parseLevel();

        makeTriangles((List)levelJson.get("triangles"));
    }
}
