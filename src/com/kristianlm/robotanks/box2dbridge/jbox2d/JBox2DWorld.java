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

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.Segment;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.RaycastResult;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import com.kristianlm.robotanks.box2dbridge.IBody;
import com.kristianlm.robotanks.box2dbridge.IJoint;
import com.kristianlm.robotanks.box2dbridge.IShape;
import com.kristianlm.robotanks.box2dbridge.IWorld;

public class JBox2DWorld implements IWorld {

	World w;
	JBox2DBody groundBody;

	public JBox2DWorld() {

	}

	@Override
	public int create(AABB worldAABB, Vec2 gravity, boolean doSleep) {
		w = new World(worldAABB, gravity, doSleep);
		return 0;
	}

	@Override
	public void step(final float dt, final int iterations) {
		w.step(dt, iterations);
	}

	@Override
	public IShape[] query(final AABB aabb, final int maxCount) {
		Shape[] sl = w.query(aabb, maxCount);
		JBox2DShape[] jsl = new JBox2DShape[sl.length];

		int pos = 0;
		for (Shape s : sl) {
			if (s.getUserData() instanceof JBox2DShape)
				jsl[pos++] = (JBox2DShape) s.getUserData();
		}

		return jsl;
	}

	@Override
	public IBody createBody(final BodyDef def) {
		Body b = w.createBody(def);
		return new JBox2DBody(b);
	}

	@Override
	public IBody getGroundBody() {
		if (groundBody == null) {
			groundBody = new JBox2DBody(w.getGroundBody());
		}
		return groundBody;
	}

	@Override
	public void destroy() {

	}

	@Override
	public IJoint createRevoluteJoint(IBody body1, IBody body2, float anchorX,
			float anchorY) {

		if (!(body1 instanceof JBox2DBody))
			return null;
		if (!(body2 instanceof JBox2DBody))
			return null;

		JBox2DBody b1 = (JBox2DBody) body1;
		JBox2DBody b2 = (JBox2DBody) body2;

		RevoluteJointDef jd = new RevoluteJointDef();
		Vec2 anchor = new Vec2(anchorX, anchorY);
		jd.initialize(b1.body, b2.body, anchor);

		Joint j = w.createJoint(jd);

		return j;

	}

	@Override
	public void destroyBody(IBody body) {
		if (!(body instanceof JBox2DBody)) {
			System.out.println("no jbox2d body");
			return;
		}

		Body jbody = ((JBox2DBody) body).body;
		w.destroyBody(jbody);
	}

	@Override
	public void destroyJoint(IJoint j) {
		if (!(j instanceof Joint))
			return;

		Joint jj = (Joint) j;
		w.destroyJoint(jj);

	}

	@Override
	public IShape raycastOne(Segment segment, RaycastResult rr,
			boolean solidShapes, Object userData) {
		Shape shape = w.raycastOne(segment, rr, solidShapes, userData);
		if (shape == null)
			return null;

		IShape ishape = null;
		if (shape.getUserData() instanceof JBox2DShape)
			ishape = (JBox2DShape) shape.getUserData();

		return ishape;
	}


	/** JBox2D is always synced.
	 * 
	 */
	@Override
	public void sync() {
		// TODO Auto-generated method stub
		
	}
	
	public World getWorld() { return w; }

}
