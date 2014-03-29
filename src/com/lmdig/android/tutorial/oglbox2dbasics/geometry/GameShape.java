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

import org.jbox2d.common.Vec2;

import com.kristianlm.robotanks.box2dbridge.IBody;
import com.kristianlm.robotanks.box2dbridge.IShape;
import com.kristianlm.robotanks.box2dbridge.IWorld;

abstract public class GameShape {

    IBody body;
    IShape shape;
    Vec2 shapePosition;
    GLInterface glShape;

	public static GameShape create(GLRectangle rect) {
		return new GameShapeRectangle(rect);
	}
	
	abstract public IShape attachToBody(IBody body, Vec2 position, float density);
	abstract public IBody attachToNewBody(IWorld world, Vec2 position, float density);

    public void detachBody(IWorld world) {
        world.destroyBody(body);
        body = null;
    }


    public void draw() {
        Vec2 p = body.getWorldCenter().add(shapePosition);

        glShape.draw(p.x, p.y, body.getAngle());
    }


    public void detachFromBody(IBody body) {
        body.destroyShape(shape);
        shape = null;
        body = null;
    }
	
	

}
