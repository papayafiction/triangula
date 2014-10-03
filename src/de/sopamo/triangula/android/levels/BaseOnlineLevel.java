package de.sopamo.triangula.android.levels;

/**
 * Created by moe on 03.10.14.
 */
public class BaseOnlineLevel extends BaseLevel implements Level {

    //this is a template for onlineLevels. Just set creatorTag and levelName in the constructor, and the JSON string later

    public BaseOnlineLevel(String creatorTag, String levelName, String levelUrl) {
        this.creatorTag=creatorTag;
        this.levelName = levelName;
        this.isOnlineLevel=true;
        this.levelUrl=levelUrl;
    }


}
