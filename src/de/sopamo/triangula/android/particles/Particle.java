package de.sopamo.triangula.android.particles;

import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.geometry.GLInterface;
import de.sopamo.triangula.android.geometry.GLRectangle;
import org.jbox2d.common.Vec2;

import static android.opengl.GLES10.glColor4f;

public class Particle {
    GLInterface glShape;
    float x;
    float y;
    Vec2 movement;
    int lifetime = 60;

    public Particle(float x, float y, Vec2 movement) {
        this.x = x;
        this.y = y;
        this.movement = movement;
        glShape = new GLRectangle(0.03f,0.03f);
    }

    public void draw() {
        glColor4f(0, 1, 0, 1);
        glShape.draw(x, y, 0);

        x += movement.x;
        y += movement.y;
        lifetime--;
        if(lifetime == 0) {
            GameImpl.getInstance().removeParticle(this);
        }
    }
}
