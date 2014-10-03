package de.sopamo.triangula.android.levels;

/**
 * Created by moe on 03.10.14.
 */
public class BaseOfficialLevel extends BaseLevel {

    //this is a template for official levels.

    public BaseOfficialLevel () {

        this.creatorTag="Official";
        this.levelName = getClass().getSimpleName();
        this.isOnlineLevel=false;
    }

    //nothing else to do here!

}
