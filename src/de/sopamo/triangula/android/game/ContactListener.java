package de.sopamo.triangula.android.game;

import android.media.MediaPlayer;

import android.graphics.Color;
import android.util.Log;
import de.sopamo.box2dbridge.IBody;
import de.sopamo.box2dbridge.jbox2d.JBox2DBody;
import de.sopamo.triangula.android.GameActivity;
import de.sopamo.triangula.android.R;
import de.sopamo.triangula.android.game.mechanics.UserData;
import de.sopamo.triangula.android.game.models.*;
import de.sopamo.triangula.android.particles.ParticleSpawner;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;

public class ContactListener implements org.jbox2d.dynamics.ContactListener {

    private boolean forceSet;
    private MediaPlayer mediaPlayer;

    @Override
    public void add(ContactPoint point) {
        Body b1 = point.shape1.getBody();
        Body b2 = point.shape2.getBody();

        add(new JBox2DBody(b1),new JBox2DBody(b2),point.position);
    }

    public void add(IBody body1, IBody body2,Vec2 position) {
        check(body1,body2,position);
        check(body2,body1,position);
    }
    
    private boolean check(IBody body,IBody body2,Vec2 position) {
        if(body != null && body.getUserData() instanceof UserData) {
            Player player = GameImpl.getMainPlayer();
            if(((UserData)(body.getUserData())).type.equals("player")) {

                // Get a realistic direction force for the emitted particles
                Vec2 direction = player.getBody().getLinearVelocity().negate().mul(0.01f);
                // Emit particles if the force was large enough
                if(direction.length() > 0.04) {
                    UserData collisionUserData = ((UserData)(body2.getUserData()));
                    if(collisionUserData == null) {
                        collisionUserData = new UserData();
                    }
                    ParticleSpawner.spawn(10, position.x, position.y, direction,collisionUserData.color);
                }
                Vec2 force = player.getBody().getWorldCenter().sub(position);
                force.mulLocal(50);

                //sound when bouncing
                if (mediaPlayer!=null){
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                mediaPlayer = MediaPlayer.create(GameActivity.getInstance(),R.raw.bounce);;
                mediaPlayer.start();

                if(forceSet) {
                    forceSet = false;
                } else {
                    if(!player.isRewinding()) {
                        player.setForce(force);
                    }
                }
            }
            if(((UserData)(body.getUserData())).type.equals("switch")) {
                Switch sw = (Switch) ((UserData) body.getUserData()).obj;
                sw.trigger();
                //sound when switching
                if (mediaPlayer!=null){
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                mediaPlayer = MediaPlayer.create(GameActivity.getInstance(),R.raw.switches);
                mediaPlayer.start();
            }
            if(((UserData)(body.getUserData())).type.equals("bomb")) {
                forceSet = true;
                //sound when touching bomb
                if (mediaPlayer!=null){
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                mediaPlayer = MediaPlayer.create(GameActivity.getInstance(),R.raw.bomb);
                mediaPlayer.start();

                ((Bomb)((UserData) body.getUserData()).obj).explode();
                Vec2 force =
                player.getBody().getWorldCenter().sub(position);
                force.mulLocal(500);
                player.setForce(force);
            }
            if(((UserData)(body.getUserData())).type.equals("exit")) {
                Exit exit = (Exit) ((UserData) body.getUserData()).obj;
                exit.endGame();
            }
            if(((UserData)(body.getUserData())).type.equals("sucker")) {
                Exit exit = (Exit) ((UserData) body.getUserData()).obj;

                //sound when exiting level
                if (mediaPlayer!=null){
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                mediaPlayer = MediaPlayer.create(GameActivity.getInstance(),R.raw.exit);
                mediaPlayer.start();

                exit.endGame();
                exit.removeSucker();
                player.suck(exit.getExitBody().getWorldCenter().add(new Vec2(0,.4f)));
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
