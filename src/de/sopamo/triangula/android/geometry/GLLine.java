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
import org.jbox2d.common.Vec2;

import java.nio.FloatBuffer;

import static android.opengl.GLES10.*;

public class GLLine implements GLInterface {

	FloatBuffer fbVertices;
    private double angle;
    private Vec2 center;
    private float width;

	public GLLine(Vec2 start, Vec2 end) {
        center = new Vec2((start.x+end.x)/2,(start.y+end.y)/2);
        angle = Math.atan2(start.y - end.y, start.x - end.x);
        width = end.sub(start).length();
        onUpdateSize();
	}
	
	public void draw() {
		glPushMatrix();

		glTranslatef(center.x, center.y, 0);
		glRotatef((float)Math.toDegrees(angle), 0, 0, 1);
		GLBufferTool.setGLVertexBuffer(2, fbVertices); 
		glDrawArrays(GL_TRIANGLE_STRIP, 
				0, 
				4);
		
		glPopMatrix();
	}

    @Override
    public void draw(float x, float y, float angle) {
        center = new Vec2(x,y);
        this.angle = angle;
        onUpdateSize();
        draw();
    }

	protected void onUpdateSize() {
		float halfWidth = width * 0.5f;
		
		float[] v = {
                halfWidth,  .05f,
                halfWidth, -.05f,
				-halfWidth,  .05f,
				-halfWidth, -.05f,
		};

		
		fbVertices = BufferTool.makeFloatBuffer(v);
	}
}
