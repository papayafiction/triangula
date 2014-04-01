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
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

public class TestGLSurfaceView extends GLSurfaceView {

	
	PGTestRenderer pgRenderer = new PGTestRenderer();
	
	public TestGLSurfaceView(Context context) {
		super(context);
		setFocusable(true);
		setFocusableInTouchMode(true);
//		setDebugFlags(DEBUG_LOG_GL_CALLS | DEBUG_CHECK_GL_ERROR);

        // Enable AntiAliasing
        setEGLConfigChooser(new MultisampleConfigChooser());
		setRenderer(pgRenderer);
		
		pgRenderer.init();
	}

	public void destroy() {
		pgRenderer.destroy();
	}
	
}
