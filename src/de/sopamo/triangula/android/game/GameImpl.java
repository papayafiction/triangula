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

import de.sopamo.box2dbridge.Box2DFactory;
import de.sopamo.box2dbridge.IBody;
import de.sopamo.box2dbridge.IWorld;
import de.sopamo.box2dbridge.jbox2d.JBox2DWorld;
import de.sopamo.box2dbridge.jnibox2d.JNIBox2DWorld;
import de.sopamo.triangula.android.GameActivity;
import de.sopamo.triangula.android.PGRenderer;
import de.sopamo.triangula.android.game.mechanics.Entity;
import de.sopamo.triangula.android.game.mechanics.Rewindable;
import de.sopamo.triangula.android.game.models.Player;
import de.sopamo.triangula.android.game.raycasting.Line;
import de.sopamo.triangula.android.game.raycasting.Ray;
import de.sopamo.triangula.android.game.raycasting.Raycaster;
import de.sopamo.triangula.android.geometry.GameShape;
import de.sopamo.triangula.android.geometry.Shadow;
import de.sopamo.triangula.android.levels.Level;
import de.sopamo.triangula.android.particles.Particle;
import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;
import java.util.List;

public class GameImpl implements GameInterface {

    public static final int TIME_TO_RESTART = 500;
    public static final float TIME_STEP = 1f / 60f;
    public static final int ITERATIONS = 5;


    private Level level;
    private InputHandler handler;

    private static GameImpl instance;

    private PhysicsTask physicsTask;

    private boolean reinit;

    private static IBody playerBody;
    private static Player player;

    private static Class nextLevel;


    IWorld world = Box2DFactory.newWorld();

    List<Rewindable> rewindables = new ArrayList<Rewindable>();
    List<IBody> noRayCast = new ArrayList<IBody>();
    List<Entity> entities = new ArrayList<Entity>();
	List<GameShape> gsl = new ArrayList<GameShape>();
	List<Particle> pl = new ArrayList<Particle>();
	List<Ray> rays = new ArrayList<Ray>();

    // Debug stuff
    long nanoTime;
    long lastClick = 0;
    long respawnTime;
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

    public void init(InputHandler handler, Level level) {
        this.level = level;
        this.handler = handler;
        respawnTime = 0;
        // density of dynamic bodies
        float density = 1;

		/* create world's bounding box. */
        AABB aabb = new AABB(
                new Vec2(-50, -50),
                new Vec2(50, 50)
        );
        if (world == null) {
            world = Box2DFactory.newWorld();
        }

        // World setup
        Vec2 gravity = new Vec2(0, -9.8f);
        world.create(aabb, gravity, true);
        world.setContactListener(new ContactListener());

        // Create player
        Player player = new Player(new Vec2(1, -5), handler);
        GameImpl.player = player;
        playerBody = player.getBody();

        // Initalize and make level
        makeLevel();
    }

    private void makeLevel() {
        level.make();
    }

    public void destroy() {
        // in case we are using JNIBox2D, this
        // is very important! otherwise we end up with memory leaks.
        // world.destroy will recursively destroy all its attached content
        physicsTask.softCancel();
        world.destroy();
        world = null;
        rewindables = new ArrayList<Rewindable>();
        entities = new ArrayList<Entity>();
        gsl = new ArrayList<GameShape>();
        pl = new ArrayList<Particle>();
        rays = new ArrayList<Ray>();

    }

	@Override
	public void drawFrame() {

        // Draw game shape shadows
        for(int i=0;i<gsl.size();i++) {
            GameShape gs = gsl.get(i);
            if(!(gs instanceof Shadow)) continue;
            gs.draw();
        }

        Raycaster.draw();

        // Draw game shapes
        for(int i=0;i<gsl.size();i++) {
            GameShape gs = gsl.get(i);
            if(gs instanceof Shadow) continue;
            gs.draw();
        }

        // Draw rays
        for(int i = 0;i < rays.size();++i) {
            Ray ray = rays.get(i);
            ray.draw();
        }
        // Draw particles
        for(int i = 0;i < pl.size();++i) {
            Particle particle = pl.get(i);
            particle.draw();
        }

	}

    public void reinit() {
        reinit = true;
    }

    @Override
    public void gameLoop() {
        /** REINIT **/
        if (reinit) {
            respawnTime += 1000l / 60;
            if (respawnTime >= TIME_TO_RESTART) {
                reinit = false;
                destroy();
                init(handler, level);
                startPhysicTask();
            }
        }
        /** ## DEBUG ## **/
        frames++;
        long elap = System.currentTimeMillis() - nanoTime;

        if (elap > 1000) {
            // update info every second
            fps = frames / ((float) elap / 1000f);
            nanoTime = System.currentTimeMillis();
            frames = 0;

            String engine = (world instanceof JBox2DWorld ? "JBox2D" : world instanceof JNIBox2DWorld ? "JNIBox2D" : "unknown");
            GameActivity.setStatus(engine + ", fps: " + fps + " Width: " + PGRenderer.getWidth() + " Height: " + PGRenderer.getHeight());
        }
        /** ## END DEBUG ## **/
    }

    public void addParticle(Particle particle) {
        if (this.pl.size() > 200) {
            this.pl.remove(0);
        }
        this.pl.add(particle);
    }

    public ArrayList<Line> getWalls() {
        ArrayList<Line> walls = new ArrayList<Line>();
        for(int i=0;i<gsl.size();i++) {
            GameShape gs = gsl.get(i);
            walls.addAll(gs.getWalls());
        }
        return walls;
    }

    public void addRay(Ray ray) {
        this.rays.add(ray);
    }
    public void removeRays() {
        this.rays = new ArrayList<Ray>();
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

    public static Class getNextLevel() {
        return nextLevel;
    }

    public static void setNextLevel(Class nextLevel) {
        GameImpl.nextLevel = nextLevel;
    }

    public InputHandler getHandler() {
        return handler;
    }

    public List<Particle> getPl() {
        return pl;
    }

    public PhysicsTask getPhysicsTask() {
        return physicsTask;
    }

    public void  startPhysicTask() {
        physicsTask = new PhysicsTask();
        physicsTask.start();
    }

    public void resume() {
        synchronized (physicsTask) {
            physicsTask.notify();
        }
    }

    public List<IBody> getNoRayCast() {
        return noRayCast;
    }
}