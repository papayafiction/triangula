package de.sopamo.triangula.android.levels;

import de.sopamo.box2dbridge.IWorld;
import de.sopamo.triangula.android.geometry.GameShape;

import java.util.List;
import java.util.Map;

public class Starter extends BaseLevel implements Level {

    public String getLevelString() {
        return "{\"version\":1,\"name\":\"foo\",\"created_at\":\"2014/03/31 12:29:41\",\"triangles\":[{\"x\":1,\"y\":370,\"size\":166},{\"x\":135,\"y\":386,\"size\":128},{\"x\":278,\"y\":107,\"size\":100},{\"x\":203,\"y\":117,\"size\":82},{\"x\":0,\"y\":85,\"size\":100},{\"x\":60,\"y\":71,\"size\":100},{\"x\":95,\"y\":126,\"size\":100},{\"x\":139,\"y\":162,\"size\":129},{\"x\":248,\"y\":317,\"size\":155},{\"x\":229,\"y\":404,\"size\":100},{\"x\":319,\"y\":397,\"size\":114},{\"x\":391,\"y\":362,\"size\":118},{\"x\":500,\"y\":148,\"size\":119},{\"x\":438,\"y\":168,\"size\":89},{\"x\":388,\"y\":175,\"size\":118},{\"x\":341,\"y\":130,\"size\":89}],\"colors\":[\"00748E\",\"E3DFBB\",\"F4BA4D\",\"E3753C\",\"DA3B3A\"]}";
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
