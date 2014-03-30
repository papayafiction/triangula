package com.lmdig.android.tutorial.oglbox2dbasics.particles;

import com.lmdig.android.tutorial.oglbox2dbasics.geometry.GLInterface;
import com.lmdig.android.tutorial.oglbox2dbasics.geometry.GLRectangle;
import org.jbox2d.common.Vec2;

import static android.opengl.GLES10.glColor4f;

public class Particle {
    GLInterface glShape;
    float x;
    float y;

    public Particle(float x, float y) {
        this.x = x;
        this.y = y;
        glShape = new GLRectangle(0.03f,0.03f);
    }

    public void draw() {

        glColor4f(0, 1, 0, 1);

        glShape.draw(x, y, 0);
    }
}
