package de.sopamo.triangula.android.game.models;

import de.sopamo.box2dbridge.IBody;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.mechanics.Entity;
import de.sopamo.triangula.android.game.mechanics.UserData;
import de.sopamo.triangula.android.geometry.GLRectangle;
import de.sopamo.triangula.android.geometry.GameShape;
import de.sopamo.triangula.android.geometry.GameShapeRectangle;
import org.jbox2d.common.Vec2;

public class Switch  {
    private Door door;

    public Switch(GameImpl game,Vec2 pst) {
        GameShapeRectangle gameShapeRectangle = new GameShapeRectangle(new GLRectangle(0.1f,0.1f));
        IBody sw = gameShapeRectangle.attachToNewBody(game.getWorld(),null,0);
        sw.setPosition(pst);
        UserData data = new UserData();
        data.type = "switch";
        data.obj = this;
        sw.setUserData(data);
        game.getGsl().add(gameShapeRectangle);
    }

    public void attachToDoor(Door door) {
        this.door = door;
    }

    public void trigger() {
        if(door != null) door.open();
    }
}
