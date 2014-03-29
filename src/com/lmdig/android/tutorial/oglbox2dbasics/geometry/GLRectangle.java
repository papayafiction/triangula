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



package com.lmdig.android.tutorial.oglbox2dbasics.geometry;

import java.nio.FloatBuffer;
import static android.opengl.GLES10.*;
import android.util.FloatMath;

import com.lmdig.android.tutorial.oglbox2dbasics.tools.BufferTool;
import com.lmdig.android.tutorial.oglbox2dbasics.tools.GLBufferTool;

public class GLRectangle extends Rectangle {
	
	FloatBuffer fbVertices;
	
	public GLRectangle() {
		super();		
	}
	
	public GLRectangle(float w, float h) {
		super(w, h);
	}
	
	public void draw() {
		GLBufferTool.setGLVertexBuffer(2, fbVertices); 
		glDrawArrays(GL_TRIANGLE_STRIP, 
				0, 
				4);

	}
	public void draw(float x, float y, float angle) {
		glPushMatrix();
		
		glTranslatef(x, y, 0);
		glRotatef((float)Math.toDegrees(angle), 0, 0, 1);
		GLBufferTool.setGLVertexBuffer(2, fbVertices); 
		glDrawArrays(GL_TRIANGLE_STRIP, 
				0, 
				4);
		
		glPopMatrix();

	}

	@Override
	protected void onUpdateSize() {
		super.onUpdateSize();
		
		float[] v = { 	 
				 getHalfWidth(),  getHalfHeight(),
				 getHalfWidth(), -getHalfHeight(),
				-getHalfWidth(),  getHalfHeight(),
				-getHalfWidth(), -getHalfHeight(),
		};

		
		fbVertices = BufferTool.makeFloatBuffer(v);
	}

}
