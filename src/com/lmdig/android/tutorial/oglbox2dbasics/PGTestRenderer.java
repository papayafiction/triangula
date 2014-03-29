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



package com.lmdig.android.tutorial.oglbox2dbasics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.lmdig.android.tutorial.oglbox2dbasics.game.GameImpl;
import com.lmdig.android.tutorial.oglbox2dbasics.game.GameInterface;

import android.opengl.GLSurfaceView.Renderer;

public class PGTestRenderer implements Renderer {

	GameInterface game;
	
	private static int mWidth;
	private static int mHeight;
	private static int mHalfWidth;
	private static int mHalfHeight;
	
	public PGTestRenderer() {
		game = new GameImpl();
	}
	

	/**
	 * Called at startup (and whenever surface needs change)
	 */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		
		
	}


	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		onSizeChange(gl, width, height);
	}


	
	public static void onSizeChange(GL10 gl, int width, int height) {
		mWidth = width;
		mHeight = height;

		mHalfWidth = width / 2;
		mHalfHeight = height / 2;

		gl.glViewport(0, 0, width, height);

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-(width / (float) height), ((float) width / height), -1,
				1, 1f, 100);

		gl.glMatrixMode(GL10.GL_MODELVIEW);

	}
	
	/**
	 * Called every frame
	 */
	@Override
	public void onDrawFrame(GL10 gl) {

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		gl.glClearColor(0.1f, 0.1f, 0.0f, 1f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glTranslatef(0, 0, -5);
		
		game.gameLoop();
		
		game.drawFrame();
		
	}


	public void init() {
		game.init();
	}
	public void destroy() {
		game.destroy();
	}
	
}
