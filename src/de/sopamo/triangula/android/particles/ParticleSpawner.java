package de.sopamo.triangula.android.particles;

import de.sopamo.triangula.android.game.GameImpl;
import org.jbox2d.common.Vec2;

import java.util.Random;

public class ParticleSpawner {

    private static int s1 = 2343235;
    private static int s2 = 6523465;

    public static void spawn(int amount,float x,float y) {
        float alpha = (float)Math.PI * 2 / amount;
        int i = -1;
        while(++i < amount)
        {
            float theta = alpha * i;
            Vec2 vec2 = new Vec2();
            vec2.x = (float) Math.cos(theta) * 0.01f;
            vec2.y = (float) Math.sin(theta) * 0.01f;
            Particle particle = new Particle(x,y,vec2);
            GameImpl.getInstance().addParticle(particle);
        }
    }
}
