package de.sopamo.triangula.android.game.models;

import de.sopamo.box2dbridge.IBody;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.mechanics.UserData;
import de.sopamo.triangula.android.geometry.GLCircle;
import de.sopamo.triangula.android.geometry.GameShape;
import de.sopamo.triangula.android.geometry.GameShapeCircle;
import org.jbox2d.common.Vec2;

public class Bomb {
    GameImpl game;
    GameShape shape;
    IBody body;

    public Bomb(GameImpl game,Vec2 pst) {
        this.game = game;
        GameShapeCircle circle = new GameShapeCircle(new GLCircle(0.08f));
        IBody circleBody = circle.attachToNewBody(game.getWorld(),null,0);
        circleBody.setPosition(pst);
        UserData data = new UserData();
        data.type = "bomb";
        circleBody.setUserData(data);
        game.getGsl().add(circle);
        this.shape = circle;
        this.body = circleBody;
    }

    public void explode() {
        game.getGsl().remove(shape);
        game.getWorld().destroyBody(body);
    }
}
