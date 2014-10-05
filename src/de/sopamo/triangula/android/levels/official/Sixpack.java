package de.sopamo.triangula.android.levels.official;

import com.google.android.gms.games.Games;
import de.sopamo.triangula.android.App;
import de.sopamo.triangula.android.R;
import de.sopamo.triangula.android.levels.BaseLevel;
import de.sopamo.triangula.android.levels.BaseOfficialLevel;
import de.sopamo.triangula.android.levels.Level;

import java.io.Serializable;

public class Sixpack extends BaseOfficialLevel implements Level,Serializable{

    String levelString = "{\"version\":5,\"name\":\"yolo4\",\"created_at\":\"2014/10/05 16:30:27\",\"triangles\":[{\"x\":3.5,\"y\":277.453125,\"size\":100,\"angle\":0},{\"x\":103.5,\"y\":277.453125,\"size\":100,\"angle\":0},{\"x\":225.5,\"y\":277,\"size\":100,\"angle\":0},{\"x\":26.875,\"y\":81.375,\"size\":100,\"angle\":\"45.00\"},{\"x\":106.15625,\"y\":60.65625,\"size\":100,\"angle\":\"-57.32\"},{\"x\":223.5,\"y\":84,\"size\":100,\"angle\":0},{\"x\":292.5,\"y\":147,\"size\":100,\"angle\":0},{\"x\":308.5,\"y\":245,\"size\":100,\"angle\":0},{\"x\":281.5,\"y\":1,\"size\":100,\"angle\":0},{\"x\":196.5,\"y\":616,\"size\":100,\"angle\":0},{\"x\":125.5,\"y\":451,\"size\":100,\"angle\":0},{\"x\":225.5,\"y\":451,\"size\":100,\"angle\":0},{\"x\":37.5,\"y\":477,\"size\":100,\"angle\":0},{\"x\":1.296875,\"y\":384.796875,\"size\":100,\"angle\":\"29.69\"},{\"x\":307.5,\"y\":414,\"size\":100,\"angle\":0},{\"x\":815,\"y\":338,\"size\":100,\"angle\":0},{\"x\":750.5,\"y\":4,\"size\":276,\"angle\":0},{\"x\":413.5,\"y\":-8.140625,\"size\":100,\"angle\":\"34.52\"},{\"x\":559.0625,\"y\":38.5625,\"size\":100,\"angle\":\"27.47\"},{\"x\":636,\"y\":67,\"size\":100,\"angle\":\"-90.00\"},{\"x\":483.515625,\"y\":273.015625,\"size\":399,\"angle\":\"32.95\"},{\"x\":877.5,\"y\":280,\"size\":100,\"angle\":0},{\"x\":743.5,\"y\":375,\"size\":100,\"angle\":0}],\"spikes\":[{\"x\":27,\"y\":338.5625,\"size\":98,\"angle\":\"45.00\",\"count\":1},{\"x\":307.5625,\"y\":264,\"size\":98,\"angle\":\"-45.00\",\"count\":1}],\"doors\":[{\"door\":{\"x\":712.78125,\"y\":131.28125,\"angle\":\"-90.00\",\"size\":100},\"switch\":{\"x\":379,\"y\":80}}],\"bombs\":[{\"x\":101.5,\"y\":438},{\"x\":102.5,\"y\":454},{\"x\":87.5,\"y\":427},{\"x\":100.5,\"y\":427},{\"x\":84.5,\"y\":413},{\"x\":87.5,\"y\":441},{\"x\":89.5,\"y\":455},{\"x\":99.5,\"y\":411},{\"x\":368.5,\"y\":55},{\"x\":357.5,\"y\":91},{\"x\":384.5,\"y\":110},{\"x\":405.5,\"y\":72},{\"x\":431.5,\"y\":83},{\"x\":458.5,\"y\":95},{\"x\":411.5,\"y\":124},{\"x\":442.5,\"y\":135},{\"x\":426.5,\"y\":109},{\"x\":453.5,\"y\":117},{\"x\":401.5,\"y\":95}],\"exits\":[{\"x\":795,\"y\":338}],\"bubbles\":[{\"x\":738.5,\"y\":80,\"size\":15},{\"x\":744.5,\"y\":101,\"size\":42}],\"colors\":[\"00748E\",\"DA3B3A\",\"E3753C\",\"E3DFBB\",\"F4BA4D\"]}";

    @Override
    public String getLevelString() {
        return levelString;
    }

    @Override
    public void end() {
        Games.Achievements.unlock(App.getGoogleApiClient(),App.getContext().getString(R.string.achievement_impossibru));
    }
}
