package de.sopamo.triangula.android.levels;

import de.sopamo.triangula.android.levels.BaseLevel;

/**
 * Created by moe on 03.10.14.
 */
public class BaseOnlineLevel extends BaseLevel {

    //this is a template for onlineLevels. Just set creatorTag and levelName in the constructor, and the JSON string later

    public BaseOnlineLevel(String creatorTag, String levelName) {
        this.creatorTag=creatorTag;
        this.levelName = levelName;
        this.isOnlineLevel=true;
    }


    //nothing else to do here!

}
