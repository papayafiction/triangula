package de.sopamo.triangula.android.geometry;


import org.jbox2d.common.Vec2;

import static android.opengl.GLES10.glColor4f;

/**
 * Fixed from Paul, Frodo did evil stuff
 * CircleBackground for moving BombBackground
 *
 */
public class CircleBackground extends GameShapeCircle {

    private Vec2 pst;
    private float radius;

    public CircleBackground(GLCircle r, Vec2 pst) {
        super(r);
        this.pst = pst;
    }

    public void setRadius(float r) {
        radius = r;
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public void draw() {
        glColor4f(red, green, blue, alpha);
        ((GLCircle)glShape).draw(pst.x,pst.y,0,
                radius);
    }

    @Override
    public void setPosition(Vec2 pst) {
        this.pst = pst;
    }

    @Override
    public Vec2 getPosition() {
        return this.pst;
    }
}
