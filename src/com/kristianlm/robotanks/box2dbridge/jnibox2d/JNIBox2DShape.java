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



package com.kristianlm.robotanks.box2dbridge.jnibox2d;

import com.kristianlm.robotanks.box2dbridge.IBody;
import com.kristianlm.robotanks.box2dbridge.IShape;

public class JNIBox2DShape implements IShape {

	JNIBox2DBody body;
	Object userData;
	int shapeID;

	public JNIBox2DShape(int ID, JNIBox2DBody body) {
		this.body = body;
		shapeID = ID;

		nAssociateShape(shapeID);
	}

	/*
	 * public JNIBox2DShape(int id) { this.body = new JNIBox2DBody(id); }
	 */

	@Override
	public IBody getBody() {
		return body;
	}

	public void setUserData(Object obj) {
		userData = obj;
	}

	public Object getUserData() {
		return userData;
	}

	native void nAssociateShape(int id);

}
