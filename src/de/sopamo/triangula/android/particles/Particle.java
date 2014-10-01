package de.sopamo.triangula.android.particles;

import android.graphics.Color;
import android.util.Log;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.geometry.GLInterface;
import de.sopamo.triangula.android.geometry.GLRectangle;
import org.jbox2d.common.Vec2;

import static android.opengl.GLES10.glColor4f;

public class Particle {
    GLRectangle glShape;
    float x;
    float y;
    Vec2 movement;
    int lifetime = 30;
    int color;

    public Particle(float x, float y, Vec2 movement,int color) {
        this.x = x;
        this.y = y;
        this.movement = movement;
        this.color = color;
        glShape = new GLRectangle(0.15f,0.15f);
    }

    public void draw() {
        // Set color
        glColor4f(Color.red(color)/255f, Color.green(color)/255f, Color.blue(color)/255f, 1);

        // Get angle via lifetime. We rotate by 360 degrees total
        float angle = (float)Math.toRadians(lifetime*12);
        // Draw the particle
        glShape.draw(x, y, angle);

        // Set the new (smaller) size
        float size = 0.15f*(lifetime/30f);
        glShape.setHeight(size);
        glShape.setWidth(size);

        // Move the particle
        x += movement.x;
        y += movement.y;

        // Make the particle move less next tick
        movement.mulLocal(0.99f);

        // Slowly dying...
        lifetime--;

        // Particle is dead
        if(lifetime == 0) {
            GameImpl.getInstance().removeParticle(this);
        }
    }
}
