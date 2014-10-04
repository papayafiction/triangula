package de.sopamo.triangula.android.game.models;

import android.graphics.Color;
import android.util.Log;
import de.sopamo.box2dbridge.IBody;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.mechanics.Entity;
import de.sopamo.triangula.android.game.mechanics.UserData;
import de.sopamo.triangula.android.geometry.*;
import de.sopamo.triangula.android.levels.BaseLevel;
import de.sopamo.triangula.android.tools.Util;
import org.jbox2d.common.Vec2;

public class Triangle implements Entity {

    private final TriangleShadow shadow;
    private GameImpl game;
    private GameShape shape;
    private IBody body;

    public Triangle(Vec2 pst, float size, float angle, int color) {
        this.game = GameImpl.getInstance();
        GameShape triangleShape;
        triangleShape = new GameShapeTriangle(new GLTriangle(size,angle));
        float[] colors = Util.getColorParts(color);
        triangleShape.setColor(colors[0], colors[1], colors[2], 1);
        IBody body = triangleShape.attachToNewBody(game.getWorld(), null, 0);
        UserData data = new UserData();
        data.type = "triangle";
        data.color = Color.rgb((int) (colors[0] * 255), (int) (colors[1] * 255), (int) (colors[2] * 255));
        body.setUserData(data);
        body.setPosition(pst);
        game.getGsl().add(triangleShape);
        game.getEntities().add(this);
        this.shape = triangleShape;
        this.body = body;


        /**
         * Setting shadow
         */
        this.shadow = new TriangleShadow(new GLTriangle(size,angle), pst, angle);
        this.shadow.setColor(Math.max(0,colors[0]-100), Math.max(0,colors[1]-100), Math.max(0,colors[2]-100), .3f);
        game.getGsl().add(this.shadow);
    }

    @Override
    public void updateEntity() {
        Vec2 playerPosition = GameImpl.getMainPlayer().getBody().getWorldCenter();
        Vec2 direction = this.shape.getPosition().sub(playerPosition);
        direction.normalize();
        direction.mulLocal(0.1f);
        Vec2 realPosition = this.shape.getPosition().add(direction);

        this.shadow.setDrawPosition(realPosition);
    }
}