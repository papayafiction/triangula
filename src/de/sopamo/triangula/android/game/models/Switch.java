package de.sopamo.triangula.android.game.models;

import android.util.Log;

import de.sopamo.box2dbridge.IBody;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.mechanics.UserData;
import de.sopamo.triangula.android.geometry.GLRectangle;
import de.sopamo.triangula.android.geometry.GameShape;
import de.sopamo.triangula.android.geometry.GameShapeRectangle;
import org.jbox2d.common.Vec2;

public class Switch  {
    private Door door;
    private GameShape shape;
    private boolean alreadyActivated = false;

    public boolean isAlreadyActivated() {
        return alreadyActivated;
    }

    public void setAlreadyActivated(boolean alreadyActivated) {
        this.alreadyActivated = alreadyActivated;
    }

    public Switch(Vec2 pst) {
        GameImpl game = GameImpl.getInstance();
        GameShapeRectangle gameShapeRectangle = new GameShapeRectangle(new GLRectangle(0.1f,0.1f));
        IBody sw = gameShapeRectangle.attachToNewBody(game.getWorld(),null,0);
        sw.setPosition(pst);
        UserData data = new UserData();
        data.type = "switch";
        data.obj = this;
        sw.setUserData(data);
        game.getGsl().add(gameShapeRectangle);
        this.shape =  gameShapeRectangle;
    }

    public void attachToDoor(Door door) {
        this.door = door;
        float[] colors = door.getColors();
        shape.setColor(colors[0],colors[1],colors[2],1);
    }

    public void trigger() {
        if(door != null) door.open();
    }
}
