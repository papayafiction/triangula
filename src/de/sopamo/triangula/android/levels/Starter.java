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
        return "{\"version\":4,\"name\":\"bombs!!!\",\"created_at\":\"2014/04/03 22:57:47\",\"triangles\":[],\"spikes\":[],\"doors\":[],\"bombs\":[{\"x\":297.5,\"y\":146},{\"x\":355.5,\"y\":145},{\"x\":450.5,\"y\":193},{\"x\":503.5,\"y\":233},{\"x\":605.5,\"y\":207},{\"x\":607.5,\"y\":115},{\"x\":740.5,\"y\":131},{\"x\":768.5,\"y\":226},{\"x\":811.5,\"y\":249},{\"x\":820.5,\"y\":248},{\"x\":824.5,\"y\":135},{\"x\":849.5,\"y\":110},{\"x\":911.5,\"y\":170},{\"x\":896.5,\"y\":226},{\"x\":881.5,\"y\":267},{\"x\":761.5,\"y\":314},{\"x\":688.5,\"y\":331},{\"x\":552.5,\"y\":359},{\"x\":471.5,\"y\":330},{\"x\":571.5,\"y\":250},{\"x\":704.5,\"y\":153},{\"x\":609.5,\"y\":131},{\"x\":784.5,\"y\":307},{\"x\":842.5,\"y\":98},{\"x\":790.5,\"y\":245},{\"x\":960.5,\"y\":126},{\"x\":924.5,\"y\":310},{\"x\":753.5,\"y\":260},{\"x\":1003.5,\"y\":237},{\"x\":1057.5,\"y\":176},{\"x\":976.5,\"y\":136},{\"x\":941.5,\"y\":176},{\"x\":788.5,\"y\":284},{\"x\":601.5,\"y\":296},{\"x\":599.5,\"y\":296},{\"x\":407.5,\"y\":296},{\"x\":325.5,\"y\":263},{\"x\":235.5,\"y\":195},{\"x\":141.5,\"y\":139},{\"x\":81.5,\"y\":279},{\"x\":242.5,\"y\":331},{\"x\":240.5,\"y\":238},{\"x\":172.5,\"y\":178},{\"x\":258.5,\"y\":87},{\"x\":118.5,\"y\":74},{\"x\":71.5,\"y\":256},{\"x\":222.5,\"y\":342},{\"x\":280.5,\"y\":397},{\"x\":434.5,\"y\":367},{\"x\":375.5,\"y\":320},{\"x\":635.5,\"y\":398},{\"x\":773.5,\"y\":398},{\"x\":905.5,\"y\":429},{\"x\":971.5,\"y\":305},{\"x\":1106.5,\"y\":345},{\"x\":950.5,\"y\":435},{\"x\":845.5,\"y\":367},{\"x\":861.5,\"y\":242},{\"x\":748.5,\"y\":171},{\"x\":663.5,\"y\":250},{\"x\":585.5,\"y\":153},{\"x\":505.5,\"y\":116},{\"x\":529.5,\"y\":172},{\"x\":693.5,\"y\":63},{\"x\":377.5,\"y\":47},{\"x\":414.5,\"y\":55},{\"x\":734.5,\"y\":53},{\"x\":480.5,\"y\":53},{\"x\":624.5,\"y\":60},{\"x\":837.5,\"y\":56},{\"x\":96.5,\"y\":328},{\"x\":292.5,\"y\":357},{\"x\":199.5,\"y\":444},{\"x\":207.5,\"y\":412},{\"x\":416.5,\"y\":454},{\"x\":108.5,\"y\":370},{\"x\":66.5,\"y\":431},{\"x\":225.5,\"y\":304},{\"x\":158.5,\"y\":318},{\"x\":110.5,\"y\":146},{\"x\":43.5,\"y\":96},{\"x\":182.5,\"y\":87},{\"x\":167.5,\"y\":107},{\"x\":441.5,\"y\":5},{\"x\":650.5,\"y\":-27},{\"x\":650.5,\"y\":-27}],\"exits\":[],\"colors\":[\"00748E\",\"E3DFBB\",\"F4BA4D\",\"E3753C\",\"DA3B3A\"]}";
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
