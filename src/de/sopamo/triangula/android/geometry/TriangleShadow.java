package de.sopamo.triangula.android.geometry;


import android.util.Log;
import org.jbox2d.common.Vec2;

import static android.opengl.GLES10.glColor4f;

public class TriangleShadow extends GameShapeTriangle implements Shadow {

    private Vec2 pst;
    private float angle;

    public TriangleShadow(GLTriangle r, Vec2 pst, float angle) {
        super(r);
        this.pst = pst;
        this.angle = angle;
    }
    @Override
    public void draw() {
        glColor4f(red, green, blue, alpha);
        glShape.draw(pst.x, pst.y, angle);
    }

    public void setDrawPosition(Vec2 pst) {
        this.pst = pst;
    }
}
