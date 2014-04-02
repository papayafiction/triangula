package de.sopamo.triangula.android.game;

import org.jbox2d.common.Vec2;

public class InputHandler {

    public final static long TIME_FOR_LONGTOUCH = 100;

    public boolean longTouched;

    private boolean touched;
    private Vec2 touchPosition;
    private long touchTime;

    public void update() {
        if(touchTime != 0 &&
                System.currentTimeMillis()-touchTime >= TIME_FOR_LONGTOUCH) {
            longTouched = true;
        }
    }

    public boolean isTouched() {
        if(touched) {
            touched = false;
            return true;
        }
        return false;
    }

    public void setTouched() {
        touched = true;
        touchTime = System.currentTimeMillis();
    }

    public void reset() {
        touched = false;
        longTouched = false;
        touchTime = 0;
    }

    public Vec2 getTouchPosition() {
        return touchPosition;
    }

    public void setTouchPosition(Vec2 touchPosition) {
        this.touchPosition = touchPosition;
    }


}
