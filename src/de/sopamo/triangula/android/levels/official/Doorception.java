package de.sopamo.triangula.android.levels.official;


import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.levels.BaseLevel;
import de.sopamo.triangula.android.levels.BaseOfficialLevel;
import de.sopamo.triangula.android.levels.Level;

import java.io.Serializable;

public class Doorception extends BaseOfficialLevel implements Level,Serializable {

    public String getLevelString() {
        return "{\"version\":4,\"name\":\"doors1\",\"created_at\":\"2014/04/03 23:34:38\",\"triangles\":[{\"x\":2,\"y\":394,\"size\":100,\"angle\":0},{\"x\":103.5,\"y\":395,\"size\":100,\"angle\":0},{\"x\":210.5,\"y\":361,\"size\":100,\"angle\":\"34.70\"},{\"x\":264.953125,\"y\":320.453125,\"size\":100,\"angle\":\"48.92\"},{\"x\":109.578125,\"y\":37.078125,\"size\":255,\"angle\":\"113.03\"},{\"x\":48.5,\"y\":-1,\"size\":100,\"angle\":0},{\"x\":161,\"y\":135,\"size\":100,\"angle\":0},{\"x\":319.671875,\"y\":149.671875,\"size\":122,\"angle\":\"49.88\"},{\"x\":357,\"y\":0,\"size\":206,\"angle\":0},{\"x\":456.5,\"y\":301,\"size\":100,\"angle\":0},{\"x\":441.5,\"y\":389,\"size\":130,\"angle\":0},{\"x\":484.5,\"y\":158,\"size\":100,\"angle\":0},{\"x\":560.5,\"y\":8,\"size\":243,\"angle\":0},{\"x\":576.5,\"y\":360,\"size\":100,\"angle\":0},{\"x\":676.5,\"y\":360,\"size\":100,\"angle\":0},{\"x\":727.5,\"y\":260,\"size\":100,\"angle\":0}],\"spikes\":[],\"doors\":[{\"door\":{\"x\":350,\"y\":399,\"angle\":0,\"size\":100},\"switch\":{\"x\":198,\"y\":95}},{\"door\":{\"x\":245,\"y\":10,\"angle\":0,\"size\":100},\"switch\":{\"x\":80,\"y\":89}},{\"door\":{\"x\":406,\"y\":204,\"angle\":0,\"size\":100},\"switch\":{\"x\":298,\"y\":486}}],\"bombs\":[{\"x\":487.5,\"y\":381},{\"x\":59.5,\"y\":41},{\"x\":399.5,\"y\":339},{\"x\":561.5,\"y\":296},{\"x\":579.5,\"y\":299},{\"x\":599.5,\"y\":299},{\"x\":619.5,\"y\":300},{\"x\":640.5,\"y\":302},{\"x\":660.5,\"y\":302},{\"x\":683.5,\"y\":308},{\"x\":701.5,\"y\":305},{\"x\":704.5,\"y\":286},{\"x\":697.5,\"y\":268},{\"x\":685.5,\"y\":253}],\"exits\":[{\"x\":515,\"y\":126}],\"colors\":[\"00748E\",\"E3DFBB\",\"F4BA4D\",\"E3753C\",\"DA3B3A\"]}";
    }

    public Doorception() {
    }

    @Override
    public void end() {
        try {
            GameImpl.setNextLevel(Class.forName("de.sopamo.triangula.android.levels.official.NextLevel"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
