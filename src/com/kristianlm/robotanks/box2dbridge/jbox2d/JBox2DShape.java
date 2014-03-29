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

package com.kristianlm.robotanks.box2dbridge.jbox2d;

import org.jbox2d.collision.shapes.Shape;

import com.kristianlm.robotanks.box2dbridge.IBody;
import com.kristianlm.robotanks.box2dbridge.IShape;

public class JBox2DShape implements IShape {

	IBody body;
	Shape shape;

	Object userData;

	public JBox2DShape(JBox2DBody b, Shape s) {
		body = b;
		this.shape = s;

		s.setUserData(this);
	}

	@Override
	public IBody getBody() {
		return body;
	}

	@Override
	public void setUserData(Object obj) {
		userData = obj;
		// shape.setUserData(obj);
	}

	@Override
	public Object getUserData() {
		return userData;
		// return shape.getUserData();
	}

}
