package de.sopamo.triangula.android.particles;

import de.sopamo.triangula.android.game.GameImpl;
import org.jbox2d.common.Vec2;

import java.util.Random;

public class ParticleSpawner {

    public static void spawn(int amount,float x,float y, Vec2 direction, int color) {
        int i = -1;
        // Spawn articles
        while(++i < amount)
        {
            // Get a randomized direction force
            Vec2 particleDirection = direction.mul((float)Math.random()*0.5f);
            // Splatter the particles in various directions
            particleDirection.addLocal(new Vec2(0.02f*(float)Math.random(),0.02f*(float)Math.random()));
            // Create particle
            Particle particle = new Particle(x,y,particleDirection,color);
            // Add particle to the game
            GameImpl.getInstance().addParticle(particle);
        }
    }
}
