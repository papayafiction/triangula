package de.sopamo.triangula.android.levels;

import android.util.Log;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.mechanics.Entity;
import de.sopamo.triangula.android.game.models.Bomb;
import de.sopamo.triangula.android.game.models.Door;
import de.sopamo.triangula.android.game.models.Switch;
import org.jbox2d.common.Vec2;
import org.json.JSONException;


public class Starter extends BaseLevel implements Level {

    public String getLevelString() {
        return "{\"version\":4,\"name\":\"hallo\",\"created_at\":\"2014/04/03 18:39:51\",\"triangles\":[{\"x\":0,\"y\":297,\"size\":100,\"angle\":0},{\"x\":87,\"y\":297,\"size\":100,\"angle\":0},{\"x\":0,\"y\":62.15625,\"size\":100,\"angle\":\"179.03\"},{\"x\":85.1875,\"y\":61.1875,\"size\":100,\"angle\":\"178.48\"},{\"x\":131,\"y\":360,\"size\":100,\"angle\":0},{\"x\":282,\"y\":355,\"size\":100,\"angle\":0},{\"x\":210,\"y\":360,\"size\":100,\"angle\":0},{\"x\":124.609375,\"y\":0,\"size\":100,\"angle\":\"179.55\"},{\"x\":186.796875,\"y\":0,\"size\":100,\"angle\":\"178.61\"},{\"x\":258.125,\"y\":2.125,\"size\":100,\"angle\":\"176.62\"},{\"x\":281,\"y\":273,\"size\":100,\"angle\":0},{\"x\":267.328125,\"y\":83.328125,\"size\":100,\"angle\":\"175.64\"}],\"spikes\":[{\"x\":138,\"y\":32,\"size\":101,\"angle\":0,\"count\":2},{\"x\":336.28125,\"y\":455.28125,\"size\":99,\"angle\":\"180.18\",\"count\":2}],\"doors\":[{\"door\":{\"x\":264,\"y\":180,\"angle\":0,\"size\":100},\"switch\":{\"x\":144,\"y\":232}}],\"bombs\":[{\"x\":247,\"y\":221}],\"exits\":[],\"colors\":[\"00748E\",\"E3DFBB\",\"F4BA4D\",\"E3753C\",\"DA3B3A\"]}";
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

        try {
            makeDoors(jsonData.getJSONArray("doors"));
            makeBombs(jsonData.getJSONArray("bombs"));
            makeSpikes(jsonData.getJSONArray("spikes"));
            makeTriangles(jsonData.getJSONArray("triangles"));
        } catch (JSONException e) {
            Log.e("json", "Could not parse level String");
            System.exit(2);
        }
    }
}
