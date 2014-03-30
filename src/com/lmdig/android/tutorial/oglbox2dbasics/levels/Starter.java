package com.lmdig.android.tutorial.oglbox2dbasics.levels;

import com.kristianlm.robotanks.box2dbridge.IBody;
import com.kristianlm.robotanks.box2dbridge.IWorld;
import com.lmdig.android.tutorial.oglbox2dbasics.geometry.GameShape;

import java.util.List;
import java.util.Map;

public class Starter extends BaseLevel implements Level {

    public String getLevelString() {
        return "{\"version\":1,\"name\":\"asdasd\",\"created_at\":\"2014/03/30 16:44:45\",\"triangles\":[{\"x\":109,\"y\":226,\"size\":100},{\"x\":234,\"y\":102,\"size\":100},{\"x\":93,\"y\":102,\"size\":100},{\"x\":157,\"y\":133,\"size\":100},{\"x\":204,\"y\":226,\"size\":100}],\"colors\":[\"00748E\",\"E3DFBB\",\"F4BA4D\",\"E3753C\",\"DA3B3A\"]}";
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
