package de.sopamo.triangula.android.game.models;

import com.google.android.gms.games.Games;
import de.sopamo.box2dbridge.IBody;
import de.sopamo.triangula.android.GameActivity;
import de.sopamo.triangula.android.R;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.InputHandler;
import de.sopamo.triangula.android.game.mechanics.Entity;
import de.sopamo.triangula.android.game.mechanics.Rewindable;
import de.sopamo.triangula.android.game.mechanics.State;
import de.sopamo.triangula.android.game.mechanics.UserData;
import de.sopamo.triangula.android.geometry.GLCircle;
import de.sopamo.triangula.android.geometry.GameShape;
import de.sopamo.triangula.android.geometry.GameShapeCircle;
import org.jbox2d.common.Vec2;

import java.util.EmptyStackException;
import java.util.Stack;

public class Player implements Rewindable,Entity {
    private GameImpl game;
    private IBody body;
    private GameShape shape;
    private GameShape shapeInner;
    private long state = 0;
    private Vec2 force = null;
    private Vec2 suckerForce = null;
    private float suckerToLow;
    private float suckerStrength = 1.5f;
    private boolean sucking;
    private boolean rewind = false;
    private State currentState;
    private InputHandler inputHandler;
    private Stack<State> states = new Stack<State>();


    public Player(Vec2 pst,InputHandler handler) {
        this.game = GameImpl.getInstance();
        this.inputHandler = handler;
        shape = new GameShapeCircle(new GLCircle(0.12f));
        shape.setColor(0,0,0);
        body = shape.attachToNewBody(game.getWorld(),null,0.05f);
        body.setAngularDamping(3);
        body.setLinearDamping(1);

        shapeInner = new GameShapeCircle(new GLCircle(0.1f));
        shapeInner.setColor(220,220,220);
        shapeInner.attachToBody(body, new Vec2(0,0),0.05f);
        
        UserData data = new UserData();
        data.type = "player";
        data.obj = this;
        body.setUserData(data);
        body.setPosition(pst);
        game.getGsl().add(shape);
        game.getEntities().add(this);
        game.getRewindables().add(this);

        game.getGsl().add(shapeInner);
        game.getNoRayCast().add(body);
    }

    @Override
    public void startRewind() {
        if(GameActivity.getGoogleApiClient().isConnected()) {
            Games.Achievements.unlockImmediate(GameActivity.getGoogleApiClient(),GameActivity.getInstance().getString(R.string.achievement_travel_back_in_time_));
        }

        if(isSucking()) return;
        rewind = true;
        game.getWorld().setGravity(new Vec2(0,0));
    }

    @Override
    public void stopRewind() {
        rewind = false;
        game.getWorld().setGravity(new Vec2(0,-9.8f));
        if(currentState == null) {
            body.setLinearVelocity(new Vec2(0,0));
            body.setAngularVelocity(0);
            return;
        }
        body.setLinearVelocity(currentState.linearVelocity);
        body.setAngularVelocity(currentState.angularVelocity);
    }

    @Override
    public void run() {
        if(rewind) rewind();
        else saveCurrentState();
    }

    public void rewind() {
        if(isSucking()) stopRewind();
        if(!rewind) return;
        State state = null;
        try {
            state = states.pop();
        } catch(EmptyStackException e) {
            currentState = null;
            body.setAngularVelocity(0);
            body.setLinearVelocity(new Vec2(0, 0));
            return;
        }
        currentState = state;
        body.setPosition(state.pst);
        body.setAngle(state.angle);
    }

    @Override
    public boolean isRewinding() {
        return rewind;
    }

    public void saveCurrentState() {
        if(rewind || (state++)%2 != 0) return;
        State state = new State();
        state.angularVelocity = body.getAngularVelocity();
        state.linearVelocity = body.getLinearVelocity();
        state.pst = body.getWorldCenter().add(new Vec2(0, 0));
        state.angle = body.getAngle();
        states.push(state);

    }

    public IBody getBody() {
        return body;
    }

    @Override
    public void update() {
        if(inputHandler.isTouched() && !isSucking()) {
            Vec2 currPlayerPosition = body.getWorldCenter();
            float targetX = currPlayerPosition.x;
            float targetY = currPlayerPosition.y;

            body.applyForce(new Vec2(3, 25),new Vec2(targetX, targetY));
        }
        if(force != null) {
            body.applyForceToCenter(force);
            force = null;
        }
        if(suckerForce != null && suckerToLow <= 0) {
            Vec2 vector = suckerForce.sub(body.getWorldCenter());
            suckerStrength+=.05f;
            vector.mulLocal(suckerStrength/vector.length());
            body.setLinearVelocity(vector);
        }
        if(suckerToLow > 0) {
            Vec2 up = new Vec2(
                    (body.getWorldCenter().x < suckerForce.x)?1f:-1f,
                    5);
            suckerStrength+=.05f;
            up.mulLocal(suckerStrength/up.length());
            body.setLinearVelocity(up);
            suckerToLow = suckerForce.y - body.getWorldCenter().y+0.2f;
        }
    }

    public void suck(Vec2 pst) {
        sucking = true;
        suckerForce = pst;
        game.getWorld().setGravity(new Vec2(0,0));
        if(body.getWorldCenter().y+0.2f < pst.y)  {
            suckerToLow  = pst.y - body.getWorldCenter().y+0.2f;
            return;
        }
        suckerToLow = 0;
    }

    public boolean isSucking() {
        return sucking;
    }

    public void setForce(Vec2 force) {
        if(isSucking()) return;
        this.force = force;
    }
}
