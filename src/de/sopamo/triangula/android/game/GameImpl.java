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

import de.sopamo.triangula.android.game.mechanics.Entity;
import de.sopamo.triangula.android.game.models.Player;
import de.sopamo.triangula.android.game.mechanics.Rewindable;
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
    private InputHandler handler;

    private static GameImpl instance;


    private static IBody playerBody;
    private static Player player;


	IWorld world = Box2DFactory.newWorld();

    List<Rewindable> rewindables = new ArrayList<Rewindable>();
    List<Entity> entities = new ArrayList<Entity>();
	List<GameShape> gsl = new ArrayList<GameShape>();
	List<Particle> pl = new ArrayList<Particle>();

    // Debug stuff
    long nanoTime;
    long lastClick = 0;
    float fps;
    int frames;

    public GameImpl() {
        instance = this;

    }

    public Level getLevel() {
        return level;
    }

    public static GameImpl getInstance() {
        return instance;
    }

    public void init(InputHandler handler) {
        this.handler = handler;
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
        world.setContactListener(new ContactListener());
        // Create player
        Player player = new Player(new Vec2(-8,0),handler);
        GameImpl.player = player;
        playerBody = player.getBody();

        // Initalize and make level
        level = new Starter();
        makeLevel();
	}

    private void makeLevel() {
        level.make(this);
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
        for(int i = 0;i < pl.size();++i) {
            Particle particle = pl.get(i);
            particle.draw();
        }
	}

	@Override
	public void gameLoop() {
		if(world == null) {
			Log.e("pg", "World not initialized");
			return;
		}

        handler.update();

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

        /** Save Rewindable Actions **/
        for(int i=0;i<rewindables.size();i++) {
            Rewindable rewindable = rewindables.get(i);
            rewindable.run();
        }

        /** DO SOME REWIND **/
        if(handler.longTouched) {
            for(int i=0;i<rewindables.size();i++) {
                Rewindable rewindable = rewindables.get(i);
                if(!rewindable.isRewinding()) rewindable.startRewind();
            }
        } else {
            for(int i=0;i<rewindables.size();i++) {
                Rewindable rewindable = rewindables.get(i);
                if(rewindable.isRewinding()) rewindable.stopRewind();
            }
        }

        /** Update Entities **/
        for(int i=0;i<entities.size();i++) {
            Entity entity = entities.get(i);
            entity.update();
        }
        
        world.step(TIME_STEP, ITERATIONS);
        world.sync();
	}

    public void addParticle(Particle particle) {
        if(this.pl.size() > 200) {
            this.pl.remove(0);
        }
        this.pl.add(particle);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public IWorld getWorld() {
        return world;
    }

    public List<GameShape> getGsl() {
        return gsl;
    }

    public List<Rewindable> getRewindables() {
        return rewindables;
    }

    public void removeParticle(Particle particle) {
        this.pl.remove(particle);
    }

    public InputHandler getInputHandler() {
        return handler;
    }

    public static Player getMainPlayer() {
        return GameImpl.player;
    }
}