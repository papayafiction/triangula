package de.sopamo.triangula.android.levels.official;

import de.sopamo.triangula.android.GameActivity;
import de.sopamo.triangula.android.R;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.levels.BaseOfficialLevel;
import de.sopamo.triangula.android.levels.Level;

import java.io.Serializable;

public class Waypoint extends BaseOfficialLevel implements Level, Serializable {

    String levelString = "{\"version\":5,\"name\":\"waypoint\",\"created_at\":\"2014/10/05 13:04:09\",\"triangles\":[{\"x\":158.5,\"y\":232,\"size\":268,\"angle\":\"180.00\"},{\"x\":202.5,\"y\":-1,\"size\":181,\"angle\":0},{\"x\":634.40625,\"y\":47.40625,\"size\":100,\"angle\":\"27.92\"},{\"x\":638.640625,\"y\":308.140625,\"size\":100,\"angle\":\"-31.85\"},{\"x\":702.84375,\"y\":206.34375,\"size\":92,\"angle\":\"142.79\"},{\"x\":339.5,\"y\":326,\"size\":345,\"angle\":0},{\"x\":755.5,\"y\":342,\"size\":100,\"angle\":0},{\"x\":875.78125,\"y\":293.28125,\"size\":100,\"angle\":\"45.00\"},{\"x\":863.5,\"y\":184,\"size\":196,\"angle\":0},{\"x\":660.140625,\"y\":16.140625,\"size\":214,\"angle\":\"-45.00\"},{\"x\":723.234375,\"y\":137.734375,\"size\":81,\"angle\":\"18.12\"},{\"x\":1009.5,\"y\":84,\"size\":100,\"angle\":0},{\"x\":890.78125,\"y\":34.28125,\"size\":100,\"angle\":\"-45.00\"}],\"spikes\":[],\"doors\":[{\"door\":{\"x\":517,\"y\":27,\"angle\":\"90.00\",\"size\":100},\"switch\":{\"x\":586,\"y\":318}}],\"bombs\":[{\"x\":332.5,\"y\":231},{\"x\":321.5,\"y\":250},{\"x\":348.5,\"y\":213},{\"x\":361.5,\"y\":199},{\"x\":380.5,\"y\":184},{\"x\":405.5,\"y\":173},{\"x\":425.5,\"y\":169},{\"x\":450.5,\"y\":170},{\"x\":474.5,\"y\":186},{\"x\":494.5,\"y\":208},{\"x\":311.5,\"y\":169},{\"x\":332.5,\"y\":155},{\"x\":358.5,\"y\":140},{\"x\":382.5,\"y\":130},{\"x\":409.5,\"y\":121},{\"x\":438.5,\"y\":117},{\"x\":469.5,\"y\":127},{\"x\":501.5,\"y\":138},{\"x\":531.5,\"y\":158},{\"x\":550.5,\"y\":183},{\"x\":568.5,\"y\":223},{\"x\":512.5,\"y\":234},{\"x\":544.5,\"y\":278},{\"x\":531.5,\"y\":256},{\"x\":564.5,\"y\":204},{\"x\":582.5,\"y\":252},{\"x\":604.5,\"y\":264},{\"x\":628.5,\"y\":255},{\"x\":642.5,\"y\":234},{\"x\":655.5,\"y\":215},{\"x\":664.5,\"y\":198},{\"x\":549.5,\"y\":295},{\"x\":551.5,\"y\":315},{\"x\":619.5,\"y\":313},{\"x\":635.5,\"y\":303},{\"x\":656.5,\"y\":298},{\"x\":676.5,\"y\":250},{\"x\":677.5,\"y\":226},{\"x\":677.5,\"y\":211}],\"exits\":[{\"x\":964,\"y\":159}],\"bubbles\":[{\"x\":610.5,\"y\":54,\"size\":15},{\"x\":629.5,\"y\":44,\"size\":15},{\"x\":648.5,\"y\":36,\"size\":15},{\"x\":595.5,\"y\":42,\"size\":15},{\"x\":577.5,\"y\":38,\"size\":15},{\"x\":558.5,\"y\":30,\"size\":15},{\"x\":542.5,\"y\":25,\"size\":15},{\"x\":534.5,\"y\":15,\"size\":15},{\"x\":551.5,\"y\":5,\"size\":15},{\"x\":567.5,\"y\":20,\"size\":15},{\"x\":589.5,\"y\":29,\"size\":15},{\"x\":612.5,\"y\":32,\"size\":15},{\"x\":634.5,\"y\":27,\"size\":15},{\"x\":668.5,\"y\":28,\"size\":15},{\"x\":655.5,\"y\":19,\"size\":15},{\"x\":634.5,\"y\":11,\"size\":15},{\"x\":622.5,\"y\":20,\"size\":15},{\"x\":600.5,\"y\":16,\"size\":15},{\"x\":585.5,\"y\":10,\"size\":15},{\"x\":575.5,\"y\":5,\"size\":15},{\"x\":610.5,\"y\":0,\"size\":15},{\"x\":633.5,\"y\":0,\"size\":15},{\"x\":764.5,\"y\":323,\"size\":19},{\"x\":785.5,\"y\":322,\"size\":11},{\"x\":803.5,\"y\":321,\"size\":19},{\"x\":827.5,\"y\":323,\"size\":15}],\"colors\":[\"00748E\",\"DA3B3A\",\"E3753C\",\"E3DFBB\",\"F4BA4D\"]}";

    public String getLevelString() {
        return levelString;
    }

    public Waypoint() {
        try {
            GameImpl.setNextLevel(Class.forName("de.sopamo.triangula.android.levels.official.Level1"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void end() {
        achievments.add(GameActivity.getInstance().getString(R.string.achievement_level_waypoint));
    }
}
