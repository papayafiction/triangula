package de.sopamo.triangula.android.game.raycasting;

import org.jbox2d.common.Vec2;

public class Line {
    protected Vec2 start;
    protected Vec2 end;

    public Line(Vec2 start, Vec2 end) {
        this.start = start;
        this.end = end;
    }

    public Vec2 getStart() {
        return start;
    }

    public void setStart(Vec2 start) {
        this.start = start;
    }

    public Vec2 getEnd() {
        return end;
    }

    public void setEnd(Vec2 end) {
        this.end = end;
    }
}
