package de.sopamo.triangula.android.game.models;

import android.graphics.Color;
import de.sopamo.box2dbridge.IBody;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.mechanics.Entity;
import de.sopamo.triangula.android.game.mechanics.UserData;
import de.sopamo.triangula.android.geometry.CircleBackground;
import de.sopamo.triangula.android.geometry.GLCircle;
import de.sopamo.triangula.android.geometry.GameShape;
import de.sopamo.triangula.android.geometry.GameShapeCircle;
import de.sopamo.triangula.android.particles.ParticleSpawner;
import org.jbox2d.common.Vec2;

public class Bomb implements Entity {

    public final static float CIRCLE = 0.22f;
    public final static long TIME_TO_BLINK = 500;

    private GameImpl game;
    private GameShape shape;
    private GameShape backGround;
    private IBody body;

    private boolean destroy = false;

    private long time;

    public Bomb(Vec2 pst) {
        this.game = GameImpl.getInstance();
        GameShapeCircle circle = new GameShapeCircle(new GLCircle(0.08f));
        IBody circleBody = circle.attachToNewBody(game.getWorld(), null, 0);
        circleBody.setPosition(pst);
        circle.setColor(1, 0, 0, 1);
        UserData data = new UserData();
        data.type = "bomb";
        data.color = Color.rgb(255,0,0);
        data.obj = this;
        circleBody.setUserData(data);
        game.getGsl().add(circle);
        game.getEntities().add(this);
        this.shape = circle;
        this.body = circleBody;


        /**
         * Setting backgroundCircle
         */
        this.backGround = new CircleBackground(new GLCircle(0.08f), pst);
        this.backGround.setColor(1, 0, 0, 0.2f);
        game.getGsl().add(this.backGround);
    }

    public void explode() {
        game.getMainPlayer().remove();
        ParticleSpawner.spawn(10,body.getWorldCenter().x,body.getWorldCenter().y,new Vec2(0.1f,0.1f), Color.WHITE);
        ParticleSpawner.spawn(10,body.getWorldCenter().x,body.getWorldCenter().y, new Vec2(-(0.1f),-(0.1f)),Color.WHITE);
        ParticleSpawner.spawn(15,body.getWorldCenter().x,body.getWorldCenter().y, new Vec2(0.12f,0),Color.WHITE);
        ParticleSpawner.spawn(15,body.getWorldCenter().x,body.getWorldCenter().y, new Vec2(-(0.12f),0),Color.WHITE);
        ParticleSpawner.spawn(15,body.getWorldCenter().x,body.getWorldCenter().y, new Vec2(0,0.12f),Color.WHITE);
        ParticleSpawner.spawn(15,body.getWorldCenter().x,body.getWorldCenter().y, new Vec2(0,-0.12f),Color.WHITE);
        ParticleSpawner.spawn(10,body.getWorldCenter().x,body.getWorldCenter().y, new Vec2(-(0.1f),0.1f),Color.WHITE);
        ParticleSpawner.spawn(10,body.getWorldCenter().x,body.getWorldCenter().y, new Vec2(0.1f,-0.1f),Color.WHITE);
        destroy = true;
    }

    @Override
    public void updateEntity() {
        if (destroy) {
            destroy = false;
            game.getGsl().remove(shape);
            game.getGsl().remove(backGround);
            game.getEntities().remove(this);
            game.getWorld().destroyBody(body);
            game.reinit();
            return;
        }
        time += 1000l / 60;
        if ((int) (((float) time) / TIME_TO_BLINK) % 2 == 0) {
            ((CircleBackground) backGround).setRadius(0.08f +
                    (CIRCLE - 0.08f) * (float) (time % TIME_TO_BLINK) / TIME_TO_BLINK);
        } else {
            ((CircleBackground) backGround).setRadius(0.08f +
                    (CIRCLE - 0.08f) * (1 - (float) (time % TIME_TO_BLINK) / TIME_TO_BLINK));
        }
    }
}