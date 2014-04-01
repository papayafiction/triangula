package de.sopamo.triangula.android.levels;

import de.sopamo.box2dbridge.IWorld;
import de.sopamo.triangula.android.geometry.GameShape;

import java.util.List;
import java.util.Map;

public class Starter extends BaseLevel implements Level {

    public String getLevelString() {
        return "{\"version\":1,\"name\":\"foo\",\"created_at\":\"2014/04/01 16:07:44\",\"triangles\":[{\"x\":247,\"y\":180,\"size\":100},{\"x\":187,\"y\":139,\"size\":100},{\"x\":442,\"y\":218,\"size\":100},{\"x\":477,\"y\":289,\"size\":100},{\"x\":347,\"y\":76,\"size\":100},{\"x\":288,\"y\":40,\"size\":100},{\"x\":439,\"y\":354,\"size\":100},{\"x\":332,\"y\":179,\"size\":100},{\"x\":288,\"y\":139,\"size\":100},{\"x\":387,\"y\":139,\"size\":100},{\"x\":234,\"y\":72,\"size\":100},{\"x\":127,\"y\":298,\"size\":100},{\"x\":76,\"y\":292,\"size\":100},{\"x\":106,\"y\":305,\"size\":100},{\"x\":510,\"y\":400,\"size\":100},{\"x\":327,\"y\":0,\"size\":100}],\"colors\":[\"00748E\",\"E3DFBB\",\"F4BA4D\",\"E3753C\",\"DA3B3A\"]}";
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
