package de.sopamo.triangula.android.game.models;

import de.sopamo.box2dbridge.IBody;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.mechanics.Entity;
import de.sopamo.triangula.android.game.mechanics.UserData;
import de.sopamo.triangula.android.geometry.GLCircle;
import de.sopamo.triangula.android.geometry.GameShape;
import de.sopamo.triangula.android.geometry.GameShapeCircle;
import org.jbox2d.common.Vec2;

import static android.opengl.GLES10.glColor4f;

public class Bomb implements Entity{

    public final static float CIRCLE = 0.22f;
    public final static long TIME_TO_BLINK = 500;

    private GameImpl game;
    private GameShape shape;
    private GameShape backGround;
    private IBody body;

    private long time;

    public Bomb(GameImpl game,Vec2 pst) {
        this.game = game;
        GameShapeCircle circle = new GameShapeCircle(new GLCircle(0.08f));
        IBody circleBody = circle.attachToNewBody(game.getWorld(),null,0);
        circleBody.setPosition(pst);
        circle.setColor(1, 0, 0, 1);
        UserData data = new UserData();
        data.type = "bomb";
        circleBody.setUserData(data);
        game.getGsl().add(circle);
        game.getEntities().add(this);
        this.shape = circle;
        this.body = circleBody;


        /**
         * Setting backgroundCircle
         */
        this.backGround = new CircleBackground(new GLCircle(0.08f),pst);
        this.backGround.setColor(1,0,0,0.2f);
        game.getGsl().add(this.backGround);
    }

    public void explode() {
        game.getGsl().remove(shape);
        game.getWorld().destroyBody(body);
    }

    @Override
    public void update() {
        time+=1000l/60;
        if((int)(((float)time) / TIME_TO_BLINK) % 2 == 0) {
            ((CircleBackground)backGround).setRadius(0.08f +
                    (CIRCLE - 0.08f) * (float)(time % TIME_TO_BLINK)/TIME_TO_BLINK);
        } else {
            ((CircleBackground)backGround).setRadius(0.08f +
                    (CIRCLE - 0.08f) * (1 - (float)(time % TIME_TO_BLINK)/TIME_TO_BLINK));
        }
    }
}


/**
 * CircleBackground for moving BombBackground
 *
 */
class CircleBackground extends GameShapeCircle {

    private Vec2 pst;
    private float radius;

    public CircleBackground(GLCircle r, Vec2 pst) {
        super(r);
        this.pst = pst;
    }

    public void setRadius(float r) {
        radius = r;
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public void draw() {
        glColor4f(red, green, blue, alpha);
        ((GLCircle)glShape).draw(pst.x,pst.y,0,
                radius);
    }
}
