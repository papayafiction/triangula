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
import org.jbox2d.dynamics.BodyDef;

import de.sopamo.box2dbridge.IBody;
import de.sopamo.box2dbridge.IShape;
import de.sopamo.box2dbridge.IWorld;

public class GameShapeRectangle extends GameShape {
	
	public GameShapeRectangle(GLRectangle r) {
		glShape = r;
	}
	
	
	/**
	 * Attach the shape to a new body created from world
	 */
	@Override
	public IBody attachToNewBody(IWorld world, Vec2 position, float density) {

		BodyDef def = new BodyDef();
		def.angularDamping = 0.5f;
		def.linearDamping = 0.5f;
		def.allowSleep = false;
		body = world.createBody(def);
		
		attachToBody(body, position, density);
		
		body.refilter(0xFFFF, 0xFFFF, 0);

		return body;
	}
	
	/**
	 * Attach the shape to the body
	 * 
	 * @param body
	 * @param position Shape's placment relative to body. 0,0 is at 
	 * the body's center, which is what you usually want. Exceptions 
	 * include the ground-body, which doesn't move @ 0,0.  
	 */
	public IShape attachToBody(IBody body, Vec2 position, float density) {
		
		if(this.shape != null) {
			throw new RuntimeException("Shape already attached");
		}
		
		if(position == null)
			position = new Vec2(0, 0);
		
		shapePosition = position;
		
		this.body = body;
		shape = body.createBox(
                    ((GLRectangle)glShape).getHalfWidth(),
                    ((GLRectangle)glShape).getHalfHeight(),
					position.x, 
					position.y, 
					density, 
					0
				);

		body.setMassFromShapes();
		return shape;
	}
}