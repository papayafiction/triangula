package de.sopamo.triangula.android.levels.official;

import de.sopamo.triangula.android.GameActivity;
import de.sopamo.triangula.android.R;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.levels.BaseOfficialLevel;
import de.sopamo.triangula.android.levels.Level;

import java.io.Serializable;

public class Ascending extends BaseOfficialLevel implements Level, Serializable {

    String levelString = "{\"version\":5,\"name\":\"ascending\",\"created_at\":\"2014/10/05 21:38:11\",\"triangles\":[{\"x\":1,\"y\":280,\"size\":856,\"angle\":0},{\"x\":-187.578125,\"y\":-548.078125,\"size\":1058,\"angle\":\"-61.27\"},{\"x\":857,\"y\":280,\"size\":1121,\"angle\":0},{\"x\":1074.734375,\"y\":-718.703125,\"size\":926,\"angle\":\"178.09\"},{\"x\":1978,\"y\":219,\"size\":460,\"angle\":\"90.00\"},{\"x\":857,\"y\":226,\"size\":54,\"angle\":\"180.00\"},{\"x\":945.5,\"y\":237,\"size\":43,\"angle\":\"180.00\"}],\"spikes\":[{\"x\":733,\"y\":280,\"size\":41.666666666666664,\"angle\":\"180.00\",\"count\":3},{\"x\":1534,\"y\":206,\"size\":38,\"angle\":\"-2.26\",\"count\":2}],\"doors\":[{\"door\":{\"x\":450.78125,\"y\":210.765625,\"angle\":\"2.60\",\"size\":69},\"switch\":{\"x\":363,\"y\":269}},{\"door\":{\"x\":515.15625,\"y\":133.15625,\"angle\":\"180.07\",\"size\":74},\"switch\":{\"x\":293,\"y\":270}},{\"door\":{\"x\":1750,\"y\":280,\"angle\":0,\"size\":100},\"switch\":{\"x\":1265,\"y\":270}}],\"bombs\":[{\"x\":664.5,\"y\":247},{\"x\":962.5,\"y\":233},{\"x\":881.5,\"y\":223},{\"x\":1070.5,\"y\":278},{\"x\":1079.5,\"y\":268},{\"x\":1091.5,\"y\":259},{\"x\":1102.5,\"y\":254},{\"x\":1119.5,\"y\":250},{\"x\":1137.5,\"y\":248},{\"x\":1155.5,\"y\":251},{\"x\":1172.5,\"y\":259},{\"x\":1187.5,\"y\":271},{\"x\":1130.5,\"y\":195},{\"x\":1165.5,\"y\":202},{\"x\":1193.5,\"y\":204},{\"x\":1215.5,\"y\":201},{\"x\":1235.5,\"y\":198},{\"x\":441.5,\"y\":217}],\"exits\":[{\"x\":1942,\"y\":253}],\"bubbles\":[{\"x\":917.5,\"y\":242,\"size\":11},{\"x\":936.5,\"y\":238,\"size\":10},{\"x\":934.5,\"y\":260,\"size\":10},{\"x\":921.5,\"y\":258,\"size\":10},{\"x\":902.5,\"y\":226,\"size\":10},{\"x\":921.5,\"y\":229,\"size\":10},{\"x\":908.5,\"y\":254,\"size\":10},{\"x\":936.5,\"y\":250,\"size\":10},{\"x\":913.5,\"y\":265,\"size\":10},{\"x\":903.5,\"y\":239,\"size\":12},{\"x\":952.5,\"y\":232,\"size\":11},{\"x\":936.5,\"y\":224,\"size\":11},{\"x\":948.5,\"y\":243,\"size\":11},{\"x\":910.5,\"y\":213,\"size\":15},{\"x\":927.5,\"y\":204,\"size\":15},{\"x\":988.5,\"y\":263,\"size\":15}],\"colors\":[\"ae8f60\",\"efd961\",\"a6635d\",\"945954\",\"744150\"]}";

    public String getLevelString() {
        return levelString;
    }

    public Ascending() {
    }

    @Override
    public void end() {
        try {
            GameImpl.setNextLevel(Class.forName("de.sopamo.triangula.android.levels.official.Waypoint"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        achievments.add(GameActivity.getInstance().getString(R.string.achievement_ascend_to_the_top));
    }
}
