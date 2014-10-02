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

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.InputHandler;
import de.sopamo.triangula.android.game.PhysicsTask;
import de.sopamo.triangula.android.game.models.Image;
import de.sopamo.triangula.android.game.raycasting.Raycaster;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES10.*;

public class PGRenderer implements Renderer {

	GameImpl game;

    private long time ;
	
	private static int mWidth = 0;
	private static int mHeight = 0;
    private static float ratio;
    private static int mHalfWidth;
	private static int mHalfHeight;
    private float viewportX = 0;
    public static Image image;
    private Context context;
    private GL10 gl;
    private static PGRenderer instance;
	
	public PGRenderer(Context context) {
        this.context = context;
        PGRenderer.instance = this;

		game = new GameImpl();
	}

    public static int getWidth() {
        return PGRenderer.mWidth;
    }

    public static int getHeight() {
        return PGRenderer.mHeight;
    }

    public static void setWidth(int mWidth) {
        PGRenderer.mWidth = mWidth;
    }

    public static void setHeight(int mHeight) {
        PGRenderer.mHeight = mHeight;
    }

    public static PGRenderer getInstance() {
        return instance;
    }

    public GL10 getGl() {
        return gl;
    }

    /**
	 * Called at startup (and whenever surface needs change)
	 */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
        this.gl = gl;

        GameImpl.getInstance().getLevel().postSurfaceCreated();

    }

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
        onSizeChange(gl, width, height);
	}

	public static void onSizeChange(GL10 gl, int width, int height) {
        ratio = (width / (float) height);
		mWidth = width;
		mHeight = height;

		mHalfWidth = width / 2;
		mHalfHeight = height / 2;


        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
		gl.glFrustumf(0,2*ratio, -2, 0, 1f, 10f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);

        // Enabling alpha
        glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);

        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glEnable(GL_MULTISAMPLE);

        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        gl.glShadeModel(GL10.GL_SMOOTH);
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

        float playerX = GameImpl.getMainPlayer().getBody().getWorldCenter().x;
        viewportX = playerX * -1 - 1 + ratio*5;
        if(viewportX >0) {
            viewportX = 0;
        }
        gl.glTranslatef(viewportX,0,-5f);

		game.gameLoop();
        //Raycaster.cast();
        // Free PhysicsTask
        game.getPhysicsTask().setWaiting(false);
        game.getLevel().drawBackgroundElements(gl);
        game.drawFrame();

        /** Sync World after draw and wait for physics **/
        while(PhysicsTask.isUpdating());
        game.getWorld().sync();

        game.getLevel().postDraw();
	}


	public void init() {
        InputHandler handler = new InputHandler();
		game.init(handler, GameActivity.level);
	}
	public void destroy() {
		game.destroy();
	}
	
}
