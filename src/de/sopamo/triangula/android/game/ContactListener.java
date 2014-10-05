package de.sopamo.triangula.android.game;

import android.media.MediaPlayer;
import com.google.android.gms.games.Games;
import de.sopamo.box2dbridge.IBody;
import de.sopamo.box2dbridge.jbox2d.JBox2DBody;
import de.sopamo.triangula.android.App;
import de.sopamo.triangula.android.GameActivity;
import de.sopamo.triangula.android.R;
import de.sopamo.triangula.android.game.mechanics.UserData;
import de.sopamo.triangula.android.game.models.Bomb;
import de.sopamo.triangula.android.game.models.Exit;
import de.sopamo.triangula.android.game.models.Player;
import de.sopamo.triangula.android.game.models.Switch;
import de.sopamo.triangula.android.particles.ParticleSpawner;
import de.sopamo.triangula.android.tools.Hooks;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;

public class ContactListener implements org.jbox2d.dynamics.ContactListener {

    private boolean forceSet;
    private int counter;

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
            if(((UserData)(body.getUserData())).type.equals("player") && !((UserData)(body.getUserData())).type.equals("sucker")) {

                // Get a realistic direction force for the emitted particles
                Vec2 direction = player.getBody().getLinearVelocity().negate().mul(0.01f);
                // Emit particles if the force was large enough
                if(direction.length() > 0.03) {
                    UserData collisionUserData = ((UserData)(body2.getUserData()));
                    if(collisionUserData == null) {
                        collisionUserData = new UserData();
                    }
                    ParticleSpawner.spawn(10, position.x, position.y, direction,collisionUserData.color);

                    //sound when bouncing
                    Hooks.call(Hooks.BOUNCE);
                }
                Vec2 force = player.getBody().getWorldCenter().sub(position);
                force.mulLocal(50);



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
                if (!sw.isAlreadyActivated()) {
                    //sound when switching if not already switched
                    Hooks.call(Hooks.SWITCH);

                    sw.setAlreadyActivated(true);
                }
            }
            if(((UserData)(body.getUserData())).type.equals("bomb") && ((UserData)(body2.getUserData())).type.equals("player")) {
                forceSet = true;
                //sound when touching bomb
                Hooks.call(Hooks.EXPLODE);



                ((Bomb)((UserData) body.getUserData()).obj).explode();
                Vec2 force =
                player.getBody().getWorldCenter().sub(position);
                force.mulLocal(500);
                player.setForce(force);

                //count number of touched bombs
                count();

                //unlock achievements
                if(App.connectedToPlayServices()) {
                    Games.Achievements.increment(App.getGoogleApiClient(),App.getContext().getString(R.string.achievement_explode_100_bombs),1);
                    Games.Achievements.increment(App.getGoogleApiClient(),App.getContext().getString(R.string.achievement_explode_1000_bombs),1);
                    Games.Achievements.increment(App.getGoogleApiClient(),App.getContext().getString(R.string.achievement_explode_5000_bombs),1);
                    if (counter >= 10){
                        Games.Achievements.unlock(App.getGoogleApiClient(),GameActivity.getInstance().getString(R.string.achievement_bombs_10));
                    }else if (counter >= 5){
                        Games.Achievements.unlock(App.getGoogleApiClient(),GameActivity.getInstance().getString(R.string.achievement_bombs_5));
                    }else if (counter >= 3){
                        Games.Achievements.unlock(App.getGoogleApiClient(),GameActivity.getInstance().getString(R.string.achievement_bombs_3));
                    }else if (counter == 1){
                        Games.Achievements.unlock(App.getGoogleApiClient(),GameActivity.getInstance().getString(R.string.achievement_first_bomb));
                    }
                }

            }
            if(((UserData)(body.getUserData())).type.equals("exit")) {
                Exit exit = (Exit) ((UserData) body.getUserData()).obj;
                exit.endGame();
            }
            if(((UserData)(body.getUserData())).type.equals("sucker") && ((UserData)(body2.getUserData())).type.equals("player")) {
                Exit exit = (Exit) ((UserData) body.getUserData()).obj;

                //sound when exiting level
               Hooks.call(Hooks.EXIT);

                exit.removeSucker();
                //only suck in player, if above exit
                if (GameImpl.getMainPlayer().getBody().getWorldCenter().y > body.getWorldCenter().y){
                    player.suck(exit.getExitBody().getWorldCenter().add(new Vec2(0,.4f)));
                }
            }
        }
        return false;
    }

    //counter for bomb touches
    public void count(){
        counter = counter+1;
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
