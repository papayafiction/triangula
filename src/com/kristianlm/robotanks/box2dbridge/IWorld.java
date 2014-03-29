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

package com.kristianlm.robotanks.box2dbridge;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.Segment;
import org.jbox2d.common.RaycastResult;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;

public interface IWorld {

	public int create(final AABB worldAABB, final Vec2 gravity,
			final boolean doSleep);

	public void step(final float dt, final int iterations);

	public IShape[] query(final AABB aabb, final int maxCount);

	public IBody createBody(final BodyDef def);

	public void destroyBody(IBody body);

	public IBody getGroundBody();

	public void destroy();

	public IJoint createRevoluteJoint(IBody body1, IBody body2, float anchorX,
			float anchorY);

	public void destroyJoint(IJoint j);

	public IShape raycastOne(Segment s, RaycastResult rr, boolean solidShapes,
			Object userData);
	
	/** SOme implementations of IWorld may not update body positions etc
	 *  for performance reasons. eg. JNIBOx2d only updates java-variables for
	 *  positions when bodies are queried.
	 *  
	 *  this syncs all such variables 
	 */
	public void sync();
}
