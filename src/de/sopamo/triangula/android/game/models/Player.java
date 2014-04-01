package de.sopamo.triangula.android.game.models;

import de.sopamo.box2dbridge.IBody;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.InputHandler;
import de.sopamo.triangula.android.game.mechanics.Entity;
import de.sopamo.triangula.android.game.mechanics.Rewindable;
import de.sopamo.triangula.android.game.mechanics.State;
import de.sopamo.triangula.android.geometry.GLRectangle;
import de.sopamo.triangula.android.geometry.GameShape;
import de.sopamo.triangula.android.geometry.GameShapeRectangle;
import org.jbox2d.common.Vec2;

import java.util.EmptyStackException;
import java.util.Stack;

public class Player implements Rewindable,Entity {
    private GameImpl game;
    private Vec2 pst;
    private IBody body;
    private GameShape shape;
    private boolean rewind = false;
    private State currentState;
    private InputHandler inputHandler;
    private Stack<State> states = new Stack<State>();


    public Player(GameImpl game, Vec2 pst,InputHandler handler) {
        this.game = game;
        this.pst = pst;
        this.inputHandler = handler;
        shape = new GameShapeRectangle(new GLRectangle(0.2f,0.2f));
        shape.setColor(255,76,22);
        body = shape.attachToNewBody(game.getWorld(),null,1);
        body.setUserData("player");
        body.setPosition(pst);
        game.getGsl().add(shape);
        game.getRewindables().add(this);
    }

    @Override
    public void startRewind() {
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
        if(rewind) return;
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
        if(inputHandler.isTouched()) {
            Vec2 currPlayerPosition = body.getWorldCenter();
            float targetX = currPlayerPosition.x;
            float targetY = currPlayerPosition.y;

            body.setAngularDamping(3);
            body.setLinearDamping(1);
            body.applyForce(new Vec2(1, 5), new Vec2(targetX, targetY));
        }
    }
}
