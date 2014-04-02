package de.sopamo.triangula.android.levels;

import de.sopamo.box2dbridge.IWorld;
import de.sopamo.triangula.android.geometry.GameShape;

import java.util.List;
import java.util.Map;

public class Starter extends BaseLevel implements Level {

    public String getLevelString() {
        return "{\"version\":3,\"name\":\"starter\",\"created_at\":\"2014/04/02 14:07:44\",\"triangles\":[{\"x\":612,\"y\":74,\"size\":122,\"angle\":0},{\"x\":556,\"y\":147,\"size\":100,\"angle\":0},{\"x\":425,\"y\":299,\"size\":100,\"angle\":0},{\"x\":397,\"y\":356,\"size\":100,\"angle\":0},{\"x\":486,\"y\":156,\"size\":100,\"angle\":0},{\"x\":479,\"y\":85,\"size\":100,\"angle\":0},{\"x\":438,\"y\":34,\"size\":100,\"angle\":0},{\"x\":363,\"y\":73,\"size\":100,\"angle\":0},{\"x\":246.765625,\"y\":16.765625,\"size\":140,\"angle\":\"-16.78\"},{\"x\":111,\"y\":379,\"size\":319,\"angle\":0},{\"x\":43,\"y\":402,\"size\":100,\"angle\":0},{\"x\":0,\"y\":393.390625,\"size\":100,\"angle\":\"99.54\"},{\"x\":65,\"y\":32,\"size\":100,\"angle\":0},{\"x\":146.75,\"y\":0,\"size\":162,\"angle\":\"12.05\"},{\"x\":903,\"y\":394,\"size\":100,\"angle\":0},{\"x\":990,\"y\":305,\"size\":100,\"angle\":0},{\"x\":837,\"y\":135,\"size\":100,\"angle\":0},{\"x\":710,\"y\":423,\"size\":100,\"angle\":0},{\"x\":643.765625,\"y\":387.765625,\"size\":100,\"angle\":\"76.59\"},{\"x\":789,\"y\":412,\"size\":100,\"angle\":0},{\"x\":524,\"y\":317,\"size\":100,\"angle\":0},{\"x\":760,\"y\":256,\"size\":82,\"angle\":0},{\"x\":693,\"y\":97,\"size\":100,\"angle\":0},{\"x\":616,\"y\":187,\"size\":131,\"angle\":0},{\"x\":548.65625,\"y\":330.65625,\"size\":100,\"angle\":\"-47.03\"},{\"x\":686,\"y\":232,\"size\":123,\"angle\":0},{\"x\":0,\"y\":0,\"size\":100,\"angle\":0},{\"x\":0,\"y\":0,\"size\":100,\"angle\":0},{\"x\":0,\"y\":0,\"size\":100,\"angle\":0},{\"x\":0,\"y\":0,\"size\":100,\"angle\":0},{\"x\":201,\"y\":210,\"size\":100,\"angle\":\"-34.73\"},{\"x\":158.390625,\"y\":246.390625,\"size\":100,\"angle\":\"105.59\"},{\"x\":167.125,\"y\":211.125,\"size\":100,\"angle\":\"32.11\"},{\"x\":794,\"y\":389,\"size\":160,\"angle\":0},{\"x\":0,\"y\":0,\"size\":100,\"angle\":0},{\"x\":883,\"y\":7,\"size\":100,\"angle\":0},{\"x\":1006,\"y\":225,\"size\":100,\"angle\":0},{\"x\":990,\"y\":335,\"size\":100,\"angle\":0},{\"x\":935,\"y\":223,\"size\":100,\"angle\":0},{\"x\":1024,\"y\":98,\"size\":100,\"angle\":0},{\"x\":960,\"y\":43,\"size\":100,\"angle\":0},{\"x\":1063,\"y\":336,\"size\":100,\"angle\":0},{\"x\":978,\"y\":136,\"size\":100,\"angle\":0},{\"x\":977,\"y\":424,\"size\":169,\"angle\":0},{\"x\":736.078125,\"y\":0,\"size\":153,\"angle\":\"-29.83\"}],\"colors\":[\"00748E\",\"E3DFBB\",\"F4BA4D\",\"E3753C\",\"DA3B3A\"]}";
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
