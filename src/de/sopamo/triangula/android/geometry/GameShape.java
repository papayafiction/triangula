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

import org.jbox2d.common.Vec2;

import de.sopamo.box2dbridge.IBody;
import de.sopamo.box2dbridge.IShape;
import de.sopamo.box2dbridge.IWorld;

import static android.opengl.GLES10.glColor4f;

abstract public class GameShape {

    protected IBody body;
    protected IShape shape;
    protected Vec2 shapePosition;
    protected GLInterface glShape;

    protected float red = 0;
    protected float blue = 0;
    protected float green = 1;
    protected float alpha = 1;

	public static GameShape create(GLRectangle rect) {
		return new GameShapeRectangle(rect);
	}
	
	abstract public IShape attachToBody(IBody body, Vec2 position, float density);
	abstract public IBody attachToNewBody(IWorld world, Vec2 position, float density);

    public void detachBody(IWorld world) {
        world.destroyBody(body);
        body = null;
    }

    public void setColor(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public void setColor(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }


    public void draw() {

        glColor4f(red, green, blue, alpha);
        Vec2 p = body.getWorldCenter().add(shapePosition);

        glShape.draw(p.x, p.y, body.getAngle());
    }

    public Vec2 getShapePosition() {
        return shapePosition;
    }

    public IShape getShape() {
        return shape;
    }

    public void detachFromBody(IBody body) {
        body.destroyShape(shape);
        shape = null;
        body = null;
    }
}
