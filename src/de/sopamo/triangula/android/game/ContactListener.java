package de.sopamo.triangula.android.game;

import android.util.Log;
import de.sopamo.box2dbridge.IBody;
import de.sopamo.box2dbridge.IShape;
import de.sopamo.box2dbridge.jbox2d.JBox2DBody;
import de.sopamo.box2dbridge.jnibox2d.JNIBox2DBody;
import de.sopamo.triangula.android.game.mechanics.UserData;
import de.sopamo.triangula.android.game.models.Bomb;
import de.sopamo.triangula.android.game.models.Player;
import de.sopamo.triangula.android.game.models.Spikes;
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

        add(new JBox2DBody(b1),new JBox2DBody(b2),point.position);
    }

    public void add(IBody body1, IBody body2,Vec2 position) {
        check(body1,position);
        check(body2,position);
    }
    
    private boolean check(IBody body,Vec2 position) {
        if(body != null && body.getUserData() instanceof UserData) {
            Player player = GameImpl.getMainPlayer();
            if(((UserData)(body.getUserData())).type.equals("player")) {
                ParticleSpawner.spawn(10, position.x,
                        position.y);
            }
            if(((UserData)(body.getUserData())).type.equals("spike")) {
                Vec2 force=
                player.getBody().getWorldCenter().sub(position);
                force.mulLocal(50);
                player.setForce(force);
            }
            if(((UserData)(body.getUserData())).type.equals("switch")) {
                Switch sw = (Switch) ((UserData) body.getUserData()).obj;
                sw.trigger();
            }
            if(((UserData)(body.getUserData())).type.equals("bomb")) {
                ((Bomb)((UserData) body.getUserData()).obj).explode();
                Vec2 force =
                player.getBody().getWorldCenter().sub(position);
                force.mulLocal(500);
                player.setForce(force);
            }
        }
        return false;
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
