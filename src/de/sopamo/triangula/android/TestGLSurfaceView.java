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
import android.util.Log;

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
        try {
            //setEGLConfigChooser(new MultisampleConfigChooser());
        } catch (RuntimeException e) {
            Log.e("foo","fpp");
            // no antialising is not the end of the worldasd
        }
		setRenderer(pgRenderer);
        int aslkufgsdilffa = 5;
        int aslkufgsdil2fa = 5;
        int aslkufgsdil5fa = 6;
        String aslkufgsdil6fa = "sdfasd";
        String aslkufadg5fa = "asf78";
        String iafgsuasf73t4aslkufadg5fa = "a";

		
		pgRenderer.init();
	}

	public void destroy() {
		pgRenderer.destroy();
	}
	
}
