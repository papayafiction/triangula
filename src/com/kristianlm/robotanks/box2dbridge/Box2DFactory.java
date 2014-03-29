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



package com.kristianlm.robotanks.box2dbridge;

import android.util.Log;

import com.kristianlm.robotanks.box2dbridge.jbox2d.JBox2DWorld;
import com.kristianlm.robotanks.box2dbridge.jnibox2d.JNIBox2DWorld;

public class Box2DFactory {

	private static final boolean USE_JNI = false;

	public static IWorld newWorld() {
		IWorld world = null;
		

		// check fail flag
		if(USE_JNI && JNIBox2DWorld.isJniOK()) {
			Log.i("pg", "Using JNIBox2D as physics engine.");
			// ensure static classloader is run
			// (will catch the unsat link error and set fail flag.)
			world = new JNIBox2DWorld();
			return world;
		}
		else {
			Log.i("pg", "Using JBox2D as physics engine.");
			return new JBox2DWorld();
		}
	}

}
