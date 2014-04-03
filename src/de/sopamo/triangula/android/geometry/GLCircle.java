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



package de.sopamo.triangula.android.geometry;

import de.sopamo.triangula.android.tools.BufferTool;
import de.sopamo.triangula.android.tools.GLBufferTool;

import java.nio.FloatBuffer;

import static android.opengl.GLES10.*;

public class GLCircle extends Circle implements GLInterface {

	FloatBuffer fbVertices;

	public GLCircle() {
		super();
	}

	public GLCircle(float r) {
		super(r);
	}
	
	public void draw() {
		GLBufferTool.setGLVertexBuffer(2, fbVertices);
		glDrawArrays(GL_TRIANGLE_FAN,
				0, 
				4);
	}
	public void draw(float x, float y, float angle) {
        glPushMatrix();

        glTranslatef(x, y, 0);
        glRotatef((float)Math.toDegrees(angle), 0, 0, 1);
        GLBufferTool.setGLVertexBuffer(2, fbVertices);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 176);

        glPopMatrix();
	}

    public void draw(float x,float y, float angle, float radius) {
        float ratio = radius/this.getRadius();
        glPushMatrix();

        glTranslatef(x, y, 0);
        glRotatef((float)Math.toDegrees(angle), 0, 0, 1);
        glScalef(ratio,ratio,0);
        GLBufferTool.setGLVertexBuffer(2, fbVertices);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 176);

        glPopMatrix();
    }

	@Override
	protected void onUpdateSize() {
		super.onUpdateSize();
		
		float[] v = new float[176*2];
        for(int i = 0; i < v.length;){
            v[i]= getRadius()* (float)Math.cos(2*i*(Math.PI/v.length));
            v[i+1]= getRadius()* (float)Math.sin(2*i*(Math.PI/v.length));
            i = i+2;
        }

		
		fbVertices = BufferTool.makeFloatBuffer(v);
	}

}
