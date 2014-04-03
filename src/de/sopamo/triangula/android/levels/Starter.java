package de.sopamo.triangula.android.levels;

import android.util.Log;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.models.Door;
import de.sopamo.triangula.android.game.models.Switch;
import org.jbox2d.common.Vec2;
import org.json.JSONException;


public class Starter extends BaseLevel implements Level {

    public String getLevelString() {
        return "{\"version\":4,\"name\":\"level1\",\"created_at\":\"2014/04/03 16:25:02\",\"triangles\":[{\"x\":210,\"y\":181,\"size\":192,\"angle\":\"90.00\"},{\"x\":97,\"y\":378,\"size\":122,\"angle\":\"6.54\"},{\"x\":0,\"y\":392,\"size\":100,\"angle\":0},{\"x\":156.890625,\"y\":22.890625,\"size\":100,\"angle\":\"-60.62\"},{\"x\":262.796875,\"y\":63.796875,\"size\":100,\"angle\":\"5.71\"},{\"x\":66.25,\"y\":0,\"size\":100,\"angle\":\"179.92\"},{\"x\":336,\"y\":95,\"size\":100,\"angle\":0},{\"x\":212,\"y\":372,\"size\":100,\"angle\":0},{\"x\":480,\"y\":417,\"size\":100,\"angle\":0},{\"x\":353,\"y\":412,\"size\":100,\"angle\":0},{\"x\":529,\"y\":340,\"size\":100,\"angle\":0},{\"x\":506,\"y\":253,\"size\":100,\"angle\":0},{\"x\":506,\"y\":156,\"size\":100,\"angle\":0},{\"x\":362.515625,\"y\":20.515625,\"size\":216,\"angle\":\"-39.63\"},{\"x\":0,\"y\":0,\"size\":100,\"angle\":\"-84.21\"},{\"x\":266,\"y\":393,\"size\":100,\"angle\":0}],\"spikes\":[],\"doors\":[],\"bombs\":[],\"exits\":[{\"x\":449,\"y\":368}],\"colors\":[\"00748E\",\"E3DFBB\",\"F4BA4D\",\"E3753C\",\"DA3B3A\"]}";
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

        /** Debugging Door **/
        Door door = new Door(game,new Vec2(0,-3),1,20);
        new Switch(game,new Vec2(0,-4)).attachToDoor(door);

        try {
            makeSpikes(jsonData.getJSONArray("spikes"));
            makeTriangles(jsonData.getJSONArray("triangles"));
        } catch (JSONException e) {
            Log.e("json", "Could not parse level String");
            System.exit(2);
        }
    }
}
