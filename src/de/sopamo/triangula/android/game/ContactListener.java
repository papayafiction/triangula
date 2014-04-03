package de.sopamo.triangula.android.game;

import android.util.Log;
import de.sopamo.box2dbridge.IBody;
import de.sopamo.box2dbridge.IShape;
import de.sopamo.box2dbridge.jbox2d.JBox2DBody;
import de.sopamo.box2dbridge.jnibox2d.JNIBox2DBody;
import de.sopamo.triangula.android.game.mechanics.UserData;
import de.sopamo.triangula.android.game.models.Switch;
import de.sopamo.triangula.android.particles.ParticleSpawner;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;

public class ContactListener implements org.jbox2d.dynamics.ContactListener {
    @Override
    public void add(ContactPoint point) {
        Body b1 = point.shape1.getBody();
        Body b2 = point.shape2.getBody();

        add(new JBox2DBody(b1),new JBox2DBody(b2));
    }

    public void add(IBody body1, IBody body2) {
        check(body1,body2);
        check(body2,body1);
    }
    
    private void check(IBody body,IBody body2) {
        if(body != null && body.getUserData() instanceof UserData) {
            if(((UserData)(body.getUserData())).type.equals("player")) {
                Vec2 center = body.getWorldCenter().add(new Vec2(0,0));

                // Spawn contact particles
                ParticleSpawner.spawn(10,center.x,center.y);

                // On contact kick player back
                Vec2 centerEnv = body2.getWorldCenter().add(new Vec2(0,0));
                Vec2 force = new Vec2(center.x-centerEnv.x,center.y-centerEnv.y);
                force.mulLocal(10);
                body.applyForce(force,center);
            }
            if(((UserData)(body.getUserData())).type.equals("switch")) {
                Switch sw = (Switch) ((UserData) body.getUserData()).obj;
                sw.trigger();
            }
        }    
    }

    @Override
    public void persist(ContactPoint point) {

    }

    @Override
    public void remove(ContactPoint point) {

    }

    @Override
    public void result(ContactResult point) {

    }
}
