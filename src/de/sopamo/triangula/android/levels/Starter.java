package de.sopamo.triangula.android.levels;

import de.sopamo.box2dbridge.IWorld;
import de.sopamo.triangula.android.geometry.GameShape;

import java.util.List;
import java.util.Map;

public class Starter extends BaseLevel implements Level {

    public String getLevelString() {
        return "{\"version\":1,\"name\":\"colors!!!\",\"created_at\":\"2014/04/01 21:59:52\",\"triangles\":[{\"x\":460,\"y\":329,\"size\":100},{\"x\":218,\"y\":316,\"size\":100},{\"x\":13,\"y\":261,\"size\":100},{\"x\":95,\"y\":136,\"size\":100},{\"x\":200.79296875,\"y\":19.79296875,\"size\":378},{\"x\":674,\"y\":79,\"size\":100},{\"x\":315,\"y\":11,\"size\":100},{\"x\":397,\"y\":159,\"size\":100},{\"x\":581,\"y\":4,\"size\":100},{\"x\":403,\"y\":7,\"size\":100},{\"x\":415,\"y\":390,\"size\":100},{\"x\":293,\"y\":401,\"size\":100},{\"x\":0,\"y\":425,\"size\":100},{\"x\":158,\"y\":395,\"size\":100},{\"x\":132,\"y\":278,\"size\":100},{\"x\":14,\"y\":111,\"size\":100},{\"x\":97,\"y\":326,\"size\":100},{\"x\":0,\"y\":251,\"size\":100},{\"x\":112,\"y\":100,\"size\":100},{\"x\":228,\"y\":69,\"size\":100},{\"x\":187,\"y\":163,\"size\":100},{\"x\":333,\"y\":128,\"size\":100},{\"x\":222,\"y\":174,\"size\":100},{\"x\":155,\"y\":9,\"size\":100}],\"colors\":[\"fabcff\",\"E3DFBB\",\"abfe5c\",\"E3753C\",\"cccccc\"]}";
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
