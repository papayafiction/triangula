package de.sopamo.triangula.android.game.models;

import android.graphics.Color;
import de.sopamo.box2dbridge.IBody;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.mechanics.Entity;
import de.sopamo.triangula.android.game.mechanics.UserData;
import de.sopamo.triangula.android.geometry.*;
import de.sopamo.triangula.android.levels.BaseLevel;
import de.sopamo.triangula.android.tools.Util;
import org.jbox2d.common.Vec2;

/**
 * Created by Fabi on 02.10.14.
 */
public class Bubble implements Entity {
    private GameImpl game;
    private GameShape shape;
    private IBody body;

    public Bubble(Vec2 pst, float radius) {
        this.game = GameImpl.getInstance();
        GameShapeBubble bubbleShape;
        bubbleShape = new GameShapeBubble(new GLCircle(radius));
        float[] colors = Util.getColorParts(BaseLevel.getTriangleColor());
        bubbleShape.setColor(colors[0], colors[1], colors[2], 1);
        IBody body = bubbleShape.attachToNewBody(game.getWorld(), null, .01f);
        UserData data = new UserData();
        data.type = "bubble";
        data.color = Color.rgb((int) (colors[0] * 255), (int) (colors[1] * 255), (int) (colors[2] * 255));
        body.setUserData(data);
        body.setPosition(pst);
        game.getGsl().add(bubbleShape);
        game.getEntities().add(this);
        this.shape = bubbleShape;
        this.body = body;
    }

    @Override
    public void update() {

    }
}
