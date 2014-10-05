package de.sopamo.triangula.android.levels.official;

import de.sopamo.triangula.android.GameActivity;
import de.sopamo.triangula.android.R;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.levels.BaseOfficialLevel;
import de.sopamo.triangula.android.levels.Level;

import java.io.Serializable;

public class Steps extends BaseOfficialLevel implements Level, Serializable {

    String levelString = "{\"version\":5,\"name\":\"steps\",\"created_at\":\"2014/10/05 21:33:24\",\"triangles\":[{\"x\":157.546875,\"y\":92.546875,\"size\":100,\"angle\":\"17.02\"},{\"x\":241,\"y\":81,\"size\":177,\"angle\":0},{\"x\":359.625,\"y\":100.625,\"size\":100,\"angle\":\"-51.63\"},{\"x\":392.296875,\"y\":151.296875,\"size\":100,\"angle\":\"-69.72\"},{\"x\":398.859375,\"y\":220.859375,\"size\":100,\"angle\":\"260.05\"},{\"x\":318.1875,\"y\":211.1875,\"size\":151,\"angle\":\"200.22\"},{\"x\":329.15625,\"y\":396.15625,\"size\":100,\"angle\":\"-17.70\"},{\"x\":242.5625,\"y\":-174.375,\"size\":267,\"angle\":\"68.53\"},{\"x\":497,\"y\":8,\"size\":100,\"angle\":0},{\"x\":523.1875,\"y\":115.1875,\"size\":100,\"angle\":\"-21.41\"},{\"x\":508.59375,\"y\":222.59375,\"size\":100,\"angle\":\"-40.77\"},{\"x\":543.8125,\"y\":283.8125,\"size\":100,\"angle\":\"22.20\"},{\"x\":679,\"y\":402,\"size\":100,\"angle\":\"22.98\"},{\"x\":770.484375,\"y\":366.484375,\"size\":100,\"angle\":\"20.81\"},{\"x\":856.953125,\"y\":264.953125,\"size\":100,\"angle\":\"78.44\"},{\"x\":841.8125,\"y\":140.8125,\"size\":100,\"angle\":\"116.80\"},{\"x\":500.609375,\"y\":93.609375,\"size\":203,\"angle\":\"-30.35\"},{\"x\":527,\"y\":160,\"size\":214,\"angle\":0},{\"x\":754,\"y\":62,\"size\":158,\"angle\":0},{\"x\":690.640625,\"y\":-44.265625,\"size\":100,\"angle\":\"59.92\"},{\"x\":1217.75,\"y\":334.75,\"size\":100,\"angle\":\"268.55\"},{\"x\":1240.765625,\"y\":247.765625,\"size\":100,\"angle\":\"37.27\"},{\"x\":1251,\"y\":133,\"size\":96,\"angle\":0},{\"x\":1387,\"y\":181,\"size\":100,\"angle\":0},{\"x\":1268.921875,\"y\":18.921875,\"size\":145,\"angle\":\"33.80\"},{\"x\":455,\"y\":330,\"size\":37,\"angle\":0},{\"x\":35,\"y\":258,\"size\":206,\"angle\":\"-45.00\"}],\"spikes\":[],\"doors\":[{\"door\":{\"x\":573,\"y\":379,\"angle\":\"-29.90\",\"size\":100},\"switch\":{\"x\":411,\"y\":399}},{\"door\":{\"x\":941,\"y\":0,\"angle\":0,\"size\":100},\"switch\":{\"x\":867,\"y\":157}},{\"door\":{\"x\":1090,\"y\":298,\"angle\":0,\"size\":100},\"switch\":{\"x\":881,\"y\":157}},{\"door\":{\"x\":1040,\"y\":198,\"angle\":0,\"size\":100},\"switch\":{\"x\":895,\"y\":157}},{\"door\":{\"x\":990,\"y\":99,\"angle\":0,\"size\":100},\"switch\":{\"x\":909,\"y\":157}},{\"door\":{\"x\":1140,\"y\":397,\"angle\":0,\"size\":100},\"switch\":{\"x\":923,\"y\":157}}],\"bombs\":[{\"x\":111,\"y\":235},{\"x\":117,\"y\":219},{\"x\":120,\"y\":207},{\"x\":135,\"y\":200},{\"x\":147,\"y\":196},{\"x\":156,\"y\":204},{\"x\":167,\"y\":220},{\"x\":174,\"y\":233},{\"x\":182,\"y\":241}],\"exits\":[{\"x\":1420,\"y\":134}],\"bubbles\":[],\"colors\":[\"00748E\",\"E3DFBB\",\"F4BA4D\",\"E3753C\",\"19e331\"]}";

    public String getLevelString() {
        return levelString;
    }

    public Steps() {
    }

    @Override
    public void end() {
        try {
            GameImpl.setNextLevel(Class.forName("de.sopamo.triangula.android.levels.official.Starter"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        achievments.add(GameActivity.getInstance().getString(R.string.achievement_level_waypoint));
    }
}
