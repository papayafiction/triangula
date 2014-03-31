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



package de.sopamo.triangula.android;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.GameInterface;

import android.opengl.GLSurfaceView.Renderer;

import static android.opengl.GLES10.*;
import static android.opengl.GLES10.GL_BLEND;

public class PGTestRenderer implements Renderer {

	GameInterface game;
	
	private static int mWidth;
	private static int mHeight;
	private static int mHalfWidth;
	private static int mHalfHeight;
    private float viewportX = 0;
	
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

        glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);

        /* The following part enables lighting. This doesn't look good without 3d objects and / or without materials.
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);

        float[] light_position0 = { 0f, 0f, 0f, 1.0f };

        float[] light_ambient = { 0.0f, 0.0f, 0.0f, 1.0f };
        float[] light_diffuse = { 1.0f, 1.0f, 1.0f, 1.0f };
        float[] light_specular = { 1.0f, 1.0f, 1.0f, 1.0f };

        glLightfv(GL_LIGHT0,GL_POSITION,light_position0,0);
        glLightfv(GL_LIGHT0,GL_AMBIENT,light_ambient,0);
        glLightfv(GL_LIGHT0,GL_DIFFUSE,light_diffuse,0);
        glLightfv(GL_LIGHT0,GL_SPECULAR,light_specular,0);
        */
	}
	
	/**
	 * Called every frame
	 */
	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        game.getLevel().drawBackground(gl);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glTranslatef(viewportX, 0, -5);
        //viewportX-=0.008f;
		game.gameLoop();

        game.getLevel().drawBackgroundElements(gl);
		game.drawFrame();
		
	}


	public void init() {
		game.init();
	}
	public void destroy() {
		game.destroy();
	}
	
}
