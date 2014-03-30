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


package com.lmdig.android.tutorial.oglbox2dbasics.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.lmdig.android.tutorial.oglbox2dbasics.geometry.*;
import com.lmdig.android.tutorial.oglbox2dbasics.levels.Level;
import com.lmdig.android.tutorial.oglbox2dbasics.levels.Starter;
import com.lmdig.android.tutorial.oglbox2dbasics.particles.Particle;
import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import android.util.Log;

import com.kristianlm.robotanks.box2dbridge.Box2DFactory;
import com.kristianlm.robotanks.box2dbridge.IBody;
import com.kristianlm.robotanks.box2dbridge.IWorld;
import com.kristianlm.robotanks.box2dbridge.jbox2d.JBox2DWorld;
import com.kristianlm.robotanks.box2dbridge.jnibox2d.JNIBox2DWorld;
import com.lmdig.android.tutorial.oglbox2dbasics.MainActivity;


public class GameImpl implements GameInterface {

	
	private static final float TIME_STEP = 1f / 60f;
	private static final int   ITERATIONS = 5;

    private Level level;

	
	IWorld world = Box2DFactory.newWorld();

	List<GameShape> gsl = new ArrayList<GameShape>();
	List<Particle> pl = new ArrayList<Particle>();

	public void init() {

        level = new Starter();

		// density of dynamic bodies
		float density = 1;
		
		// create world's bounding box. 
		// if objects exceed these borders, they will no longer be
		// animated (body dies). limits imposed for performance reasons.
		AABB aabb = new AABB(	
						new Vec2(-50, -50), 
						new Vec2( 50,  50)
					);
		
		Vec2 gravity = new Vec2(0, -9.8f); 

		world.create(
				aabb,
				gravity,
				true);

        for(float i = -2.5f;i<2.5f;i+=1f) {
            for(float j = -2.5f;j<2.5f;j+=1f) {
                GameShape gs;
                gs = new GameShapeTriangle(new GLTriangle(0.2f));
                gs.setColor(new Random().nextFloat(),new Random().nextFloat(),new Random().nextFloat(),1);
                IBody b1 = gs.attachToNewBody(world, null, density);
                b1.setPosition(new Vec2(i, j));
                gsl.add(gs);
            }
        }

		makeFence();
        makeLevel();
	}

    private void makeLevel() {
        IBody ground = world.getGroundBody();
        level.make(world,gsl);
    }
	
	private void makeFence() {
		IBody ground = world.getGroundBody();
		
		// static bodies are defined as those having mass and intertia 0
		// this ensures they are never moved. they only affect positions of
		// other dynamic bodies who collide with them.
		float density = 0;
		GameShape gs;
		gs = GameShape.create(new GLRectangle(50, .1f));
        gs.setColor(1,1,0,1);
		gs.attachToBody(ground, new Vec2(0, -4), density);
		gsl.add(gs);
		
		gs = GameShape.create(new GLRectangle(50, .1f));
        gs.setColor(1,0,0,1);
		gs.attachToBody(ground, new Vec2(0, 4), density);
		gsl.add(gs);
		
		gs = GameShape.create(new GLRectangle(.1f, 50f));
        gs.setColor(0,0,1,1);
		gs.attachToBody(ground, new Vec2(3, 0), density);
		gsl.add(gs);
		
		gs = GameShape.create(new GLRectangle(.1f, 50f));
        gs.setColor(0,1,0,1);
		gs.attachToBody(ground, new Vec2(-3, 0), density);
		gsl.add(gs);

        for(int i = 1;i<5;++i) {
            for(int j = 1;j<5;++j) {
                pl.add(new Particle(i,j));
            }
        }

	}

	public void destroy() {
		// in case we are using JNIBox2D, this
		// is very important! otherwise we end up with memory leaks.
		// world.destroy will recursively destroy all its attached content
		world.destroy();
	}
	
	
	@Override
	public void drawFrame() {

		for(GameShape gs : gsl) {
			gs.draw();
		}

        for(Particle particle: pl) {
            particle.draw();
        }
	}
	
	long nanoTime;
	float fps;
	int frames;

	@Override
	public void gameLoop() {
		if(world == null) {
			Log.e("pg", "World not initialized");
			return;
		}
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
		
		if(world instanceof JBox2DWorld) {
//			Log.d("pg", "gravity seet to " + MainActivity.x + ", " + MainActivity.y);
			JBox2DWorld jw = ((JBox2DWorld)world);
			World w = jw.getWorld();
			w.setGravity(new Vec2(MainActivity.x, MainActivity.y));
		}
		
		world.step(TIME_STEP, ITERATIONS);
		world.sync();
	}
	

}
