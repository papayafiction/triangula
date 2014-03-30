package de.sopamo.triangula.android.particles;

import de.sopamo.triangula.android.geometry.GLInterface;
import de.sopamo.triangula.android.geometry.GLRectangle;

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
