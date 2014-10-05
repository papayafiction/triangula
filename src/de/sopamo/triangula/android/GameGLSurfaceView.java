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
import android.graphics.PixelFormat;
import android.view.View;

public class GameGLSurfaceView extends android.opengl.GLSurfaceView {

	
	PGRenderer pgRenderer;
	
	public GameGLSurfaceView(Context context) {
        super(context);
        getHolder().setFormat(PixelFormat.RGBA_8888);
        pgRenderer = new PGRenderer(context);
        setFocusable(true);
		setFocusableInTouchMode(true);

        // Enable AntiAliasing
        try {
            setEGLConfigChooser(new MultisampleConfigChooser());
        } catch (Exception e) {
            // no antialising is not the end of the world
        }

        // Try to enable immersive mode
        if(android.os.Build.VERSION.SDK_INT == 19) {
            try {
                // if this next line doesn't thrown an exception then we are on ICS or
                // above, so we can use the new field.
                this.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            } catch (Exception ex) {
            }
        }
		setRenderer(pgRenderer);
		
    }

    public void init() {
        pgRenderer.init();
    }

	public void destroy() {
		pgRenderer.destroy();
	}
	
}
