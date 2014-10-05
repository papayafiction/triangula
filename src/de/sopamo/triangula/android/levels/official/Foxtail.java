package de.sopamo.triangula.android.levels.official;

import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.levels.BaseLevel;
import de.sopamo.triangula.android.levels.BaseOfficialLevel;
import de.sopamo.triangula.android.levels.Level;

import java.io.Serializable;

public class Foxtail extends BaseOfficialLevel implements Level,Serializable {

    String levelString = "{\"version\":5,\"name\":\"yolo5\",\"created_at\":\"2014/10/05 17:03:14\",\"triangles\":[{\"x\":3.5,\"y\":300,\"size\":100,\"angle\":0},{\"x\":103.5,\"y\":300,\"size\":100,\"angle\":0},{\"x\":203.5,\"y\":300,\"size\":100,\"angle\":0},{\"x\":323.78125,\"y\":251.28125,\"size\":100,\"angle\":\"45.00\"},{\"x\":373.5,\"y\":230.5625,\"size\":100,\"angle\":0},{\"x\":573.5,\"y\":230.5625,\"size\":100,\"angle\":0},{\"x\":269.5,\"y\":67,\"size\":100,\"angle\":0},{\"x\":269.5,\"y\":-33,\"size\":100,\"angle\":0},{\"x\":369.5,\"y\":37.1875,\"size\":100,\"angle\":\"-58.34\"},{\"x\":473.5625,\"y\":70.25,\"size\":100,\"angle\":\"240.57\"},{\"x\":621.5,\"y\":23,\"size\":100,\"angle\":0},{\"x\":226.78125,\"y\":67,\"size\":100,\"angle\":\"45.00\"},{\"x\":169.5,\"y\":67,\"size\":100,\"angle\":\"67.04\"},{\"x\":79.4375,\"y\":30.484375,\"size\":100,\"angle\":\"248.20\"},{\"x\":23.8125,\"y\":47.3125,\"size\":100,\"angle\":\"-53.37\"},{\"x\":-42.21875,\"y\":0.265625,\"size\":108,\"angle\":\"243.54\"},{\"x\":473.5,\"y\":230.5625,\"size\":100,\"angle\":0},{\"x\":576.5,\"y\":126.5625,\"size\":100,\"angle\":0},{\"x\":708.5,\"y\":-1,\"size\":100,\"angle\":0}],\"spikes\":[{\"x\":366,\"y\":103,\"size\":13,\"angle\":0,\"count\":2}],\"doors\":[{\"door\":{\"x\":463,\"y\":112,\"angle\":\"180.00\",\"size\":118},\"switch\":{\"x\":620,\"y\":98}}],\"bombs\":[{\"x\":163.5,\"y\":218},{\"x\":198.5,\"y\":221},{\"x\":234.5,\"y\":216},{\"x\":260.5,\"y\":194},{\"x\":287.5,\"y\":181},{\"x\":319.5,\"y\":177},{\"x\":348.5,\"y\":191},{\"x\":370.5,\"y\":221},{\"x\":409.5,\"y\":209},{\"x\":421.5,\"y\":162},{\"x\":427.5,\"y\":122}],\"exits\":[{\"x\":583,\"y\":193}],\"bubbles\":[],\"colors\":[\"00748E\",\"DA3B3A\",\"E3753C\",\"E3DFBB\",\"F4BA4D\"]}";

    @Override
    public String getLevelString() {
        return levelString;
    }
    @Override
    public void end() {
        try {
            GameImpl.setNextLevel(Class.forName("de.sopamo.triangula.android.levels.official.Sixpack"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
