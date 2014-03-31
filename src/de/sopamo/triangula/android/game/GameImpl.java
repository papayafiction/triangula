/*
 *
 * (c)2010 Lein-Mathisen Digital
 * http://lmdig.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.  
 *
 */

package de.sopamo.triangula.android.game;

import java.util.ArrayList;
import java.util.List;
import de.sopamo.triangula.android.geometry.*;
import de.sopamo.triangula.android.levels.Level;
import de.sopamo.triangula.android.levels.Starter;
import de.sopamo.triangula.android.particles.Particle;
import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import android.util.Log;
import de.sopamo.box2dbridge.Box2DFactory;
import de.sopamo.box2dbridge.IBody;
import de.sopamo.box2dbridge.IWorld;
import de.sopamo.box2dbridge.jbox2d.JBox2DWorld;
import de.sopamo.box2dbridge.jnibox2d.JNIBox2DWorld;
import de.sopamo.triangula.android.MainActivity;

public class GameImpl implements GameInterface {

	private static final float TIME_STEP = 1f / 60f;
	private static final int   ITERATIONS = 5;

    private Level level;

    private static IBody bdy;

	IWorld world = Box2DFactory.newWorld();

	List<GameShape> gsl = new ArrayList<GameShape>();
	List<Particle> pl = new ArrayList<Particle>();

    // Debug stuff
    long nanoTime;
    float fps;
    int frames;

    public Level getLevel() {
        return level;
    }

    public void init() {
		// density of dynamic bodies
		float density = 1;
		
		/* create world's bounding box. */
		AABB aabb = new AABB(
						new Vec2(-50, -50), 
						new Vec2( 50,  50)
					);


        // World setup
        Vec2 gravity = new Vec2(0, -9.8f);
        world.create(aabb,gravity,true);
        ((JBox2DWorld) world).getWorld().setContactListener(new ContactListener());
        // Create player
        GameShape myTriangle;
        myTriangle = new GameShapeRectangle(new GLRectangle(0.2f,0.2f));
        myTriangle.setColor(255.0f,76.0f,22.0f);
        bdy = myTriangle.attachToNewBody(world, null, density);
        myTriangle.getShape().setUserData("player");
        bdy.setPosition(new Vec2(-8, 0f));
        gsl.add(myTriangle);

        // Initalize and make level
        level = new Starter();
        makeLevel();
	}

    private void makeLevel() {
        level.make(world,gsl);
    }

	public void destroy() {
		// in case we are using JNIBox2D, this
		// is very important! otherwise we end up with memory leaks.
		// world.destroy will recursively destroy all its attached content
		world.destroy();
	}

	@Override
	public void drawFrame() {
        // Draw game shapes
		for(GameShape gs : gsl) {
			gs.draw();
		}

        // Draw particles
        for(Particle particle: pl) {
            particle.draw();
        }
	}

	@Override
	public void gameLoop() {
		if(world == null) {
			Log.e("pg", "World not initialized");
			return;
		}

        /** ## DEBUG ## **/
		frames++;
		long elap = System.currentTimeMillis() - nanoTime;
		if(elap > 1000) {
			// update info every second
			fps = frames / ((float)elap / 1000f);
			nanoTime = System.currentTimeMillis();
			frames = 0;

			String engine = (world instanceof JBox2DWorld ? "JBox2D" : world instanceof JNIBox2DWorld ? "JNIBox2D": "unknown");
			MainActivity.setStatus(engine + ", fps: " + fps);
		}
        /** ## END DEBUG ## **/
		
		if(world instanceof JBox2DWorld) {
			JBox2DWorld jw = ((JBox2DWorld)world);
			World w = jw.getWorld();
			//w.setGravity(new Vec2(MainActivity.x, MainActivity.y));
		} else if (world instanceof JNIBox2DWorld) {
            //((JNIBox2DWorld) world).setGravity(MainActivity.x, MainActivity.y);
        }

        if(MainActivity.touched){
            MainActivity.touched = false;
            //Calculate the target vector
            Vec2 currPlayerPosition = bdy.getWorldCenter();
            float targetX = currPlayerPosition.x;
            float targetY = currPlayerPosition.y;

            bdy.setAngularDamping(3);
            bdy.setLinearDamping(1);
            bdy.applyForce(new Vec2(1, 5), new Vec2(targetX, targetY));
        }
		
		world.step(TIME_STEP, ITERATIONS);
		world.sync();
	}
}