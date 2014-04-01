package de.sopamo.triangula.android.game;

import android.util.Log;
import de.sopamo.box2dbridge.IBody;
import de.sopamo.box2dbridge.IShape;
import de.sopamo.box2dbridge.jbox2d.JBox2DBody;
import de.sopamo.box2dbridge.jnibox2d.JNIBox2DBody;
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
        if(body1 != null && body1.getUserData() instanceof String) {
            if(body1.getUserData().equals("player")) {
                Vec2 center = body1.getWorldCenter();
                ParticleSpawner.spawn(10,center.x,center.y);
            }
        }
        if(body2 != null && body2.getUserData() instanceof String) {
            Log.e("foouserdata2", (String) body2.getUserData());
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
