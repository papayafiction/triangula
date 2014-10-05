package de.sopamo.triangula.android.levels.official;

import de.sopamo.triangula.android.GameActivity;
import de.sopamo.triangula.android.R;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.levels.BaseOfficialLevel;
import de.sopamo.triangula.android.levels.Level;

import java.io.Serializable;

public class NextLevel extends BaseOfficialLevel implements Level, Serializable {

    String levelString = "{\"version\":5,\"name\":\"nextlevel\",\"created_at\":\"2014/10/04 19:36:26\",\"triangles\":[{\"x\":53,\"y\":0,\"size\":177,\"angle\":0},{\"x\":276,\"y\":216,\"size\":100,\"angle\":0},{\"x\":206,\"y\":-947,\"size\":969,\"angle\":\"180.00\"},{\"x\":428.796875,\"y\":22,\"size\":167,\"angle\":\"55.33\"},{\"x\":619,\"y\":171,\"size\":100,\"angle\":\"-90.00\"},{\"x\":667,\"y\":123,\"size\":100,\"angle\":\"29.42\"},{\"x\":719,\"y\":71,\"size\":100,\"angle\":\"34.80\"},{\"x\":726,\"y\":22,\"size\":100,\"angle\":0},{\"x\":443.5,\"y\":196.28125,\"size\":100,\"angle\":\"-45.00\"},{\"x\":399,\"y\":223,\"size\":100,\"angle\":0},{\"x\":145,\"y\":92,\"size\":153,\"angle\":0},{\"x\":230,\"y\":177,\"size\":100,\"angle\":0},{\"x\":0,\"y\":0,\"size\":100,\"angle\":0},{\"x\":330,\"y\":253,\"size\":100,\"angle\":0},{\"x\":491.5625,\"y\":160.5,\"size\":100,\"angle\":\"19.38\"},{\"x\":-14,\"y\":481,\"size\":1084,\"angle\":0},{\"x\":-14,\"y\":381,\"size\":100,\"angle\":\"180.00\"},{\"x\":47,\"y\":381,\"size\":100,\"angle\":\"180.00\"},{\"x\":110,\"y\":381,\"size\":100,\"angle\":\"180.00\"},{\"x\":173,\"y\":381,\"size\":100,\"angle\":\"180.00\"},{\"x\":237,\"y\":381,\"size\":100,\"angle\":\"180.00\"},{\"x\":469,\"y\":381,\"size\":100,\"angle\":\"180.00\"},{\"x\":569,\"y\":304,\"size\":177,\"angle\":\"180.00\"},{\"x\":477.796875,\"y\":228.421875,\"size\":100,\"angle\":\"29.80\"},{\"x\":1047,\"y\":-69,\"size\":617,\"angle\":\"90.00\"}],\"spikes\":[{\"x\":793.40625,\"y\":-95,\"size\":427,\"angle\":\"-31.90\",\"count\":1}],\"doors\":[{\"door\":{\"x\":553.03125,\"y\":170.03125,\"angle\":\"77.67\",\"size\":100},\"switch\":{\"x\":389,\"y\":384}}],\"bombs\":[],\"exits\":[{\"x\":981,\"y\":405},{\"x\":1008,\"y\":416},{\"x\":816,\"y\":423}],\"bubbles\":[{\"x\":656,\"y\":33,\"size\":21},{\"x\":695,\"y\":42,\"size\":18},{\"x\":636,\"y\":48,\"size\":17},{\"x\":647,\"y\":62,\"size\":21},{\"x\":620,\"y\":88,\"size\":27},{\"x\":674,\"y\":64,\"size\":15},{\"x\":697,\"y\":62,\"size\":10},{\"x\":674,\"y\":26,\"size\":15},{\"x\":653,\"y\":93,\"size\":13},{\"x\":658,\"y\":110,\"size\":23},{\"x\":603,\"y\":129,\"size\":23},{\"x\":676,\"y\":88,\"size\":16},{\"x\":633,\"y\":172,\"size\":21},{\"x\":607,\"y\":57,\"size\":27},{\"x\":583,\"y\":152,\"size\":23},{\"x\":580,\"y\":103,\"size\":23},{\"x\":562,\"y\":61,\"size\":29},{\"x\":591,\"y\":78,\"size\":22},{\"x\":586,\"y\":27,\"size\":26},{\"x\":564,\"y\":97,\"size\":17},{\"x\":545,\"y\":39,\"size\":21},{\"x\":526,\"y\":21,\"size\":19},{\"x\":614,\"y\":26,\"size\":21},{\"x\":563,\"y\":24,\"size\":20},{\"x\":635,\"y\":123,\"size\":16},{\"x\":606,\"y\":107,\"size\":17}],\"colors\":[\"ffffff\",\"a1a1a1\",\"ff001e\",\"ffe500\",\"000000\"]}";

    public String getLevelString() {
        return levelString;
    }

    public NextLevel() {
        try {
            GameImpl.setNextLevel(Class.forName("de.sopamo.triangula.android.levels.official.Steps"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
