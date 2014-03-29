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

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.Segment;
import org.jbox2d.common.RaycastResult;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;

import com.kristianlm.robotanks.box2dbridge.IBody;
import com.kristianlm.robotanks.box2dbridge.IJoint;
import com.kristianlm.robotanks.box2dbridge.IShape;
import com.kristianlm.robotanks.box2dbridge.IWorld;

public class JNIBox2DWorld implements IWorld {

	JNIBox2DBody groundBody;
	
	
	public boolean testJNI() throws UnsatisfiedLinkError {
		// this is our plan:
		// make a test call to native library. it may throw unsatisfiedlink exception,
		// but then we catch it and use the JBox2d
		
		try {
			int result = nTestLib(13);
			System.out.println("jni test result: " + result);
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}
	
	@Override
	public int create(AABB worldAABB, Vec2 gravity, boolean doSleep) {
		
		
		if(!isJniOK()) {
			// our loadlibrary function failed (native lib not found)
			return -1;
		}
		
		nCreateWorld(	worldAABB.lowerBound.x, worldAABB.lowerBound.y,
						worldAABB.upperBound.x, worldAABB.upperBound.y, gravity.x,
						gravity.y, doSleep);

		groundBody = new JNIBox2DBody(0);

		return 0;
	}

	@Override
	public IBody createBody(BodyDef def) {

		int id = nCreateBody(def.position.x, def.position.y);
		JNIBox2DBody jb = new JNIBox2DBody(id);

		jb.setUserData(def.userData);

		return jb;
	}

	@Override
	public IBody getGroundBody() {

		return groundBody;
	}

	@Override
	public IShape[] query(AABB aabb, int maxCount) {

		JNIBox2DShape[] shapeList = new JNIBox2DShape[maxCount];
		
		nShapeQuery(aabb.lowerBound.x, aabb.lowerBound.y,
				aabb.upperBound.x, aabb.upperBound.y, maxCount, shapeList);

		/*
		 * IShape [] is = new JNIBox2DShape [bodies] ; for(int i = 0 ; i <
		 * bodies ; i++) { is[i] = new JNIBox2DShape(bodyList[i]); }
		 */
		// System.out.println("Found " + bodies + " bodies in query.");
		return shapeList;
	}

	public IJoint createRevoluteJoint(IBody body1, IBody body2, float anchorX, float anchorY) {
		JNIBox2DBody b1 = null, b2 = null;

		if (body1 instanceof JNIBox2DBody)
			b1 = (JNIBox2DBody) body1;
		else
			return null;

		if (body2 instanceof JNIBox2DBody)
			b2 = (JNIBox2DBody) body2;
		else
			return null;

		int jID = nCreateRevoluteJoint(b1.bodyID, b2.bodyID, anchorX, anchorY);
		
		if(jID < 0) {
			System.out.println("native could not create joint");
			return null;
		}
		
		return new JNIBox2DJoint(jID);
	}

	@Override
	public void destroyJoint(IJoint j) {
		if (!(j instanceof JNIBox2DJoint)) {
			System.out
					.println("Cannot destroy IJoint of type other than JNIBox2DJoint");
			return;
		}

		JNIBox2DJoint jj = (JNIBox2DJoint) j;

		nDestroyJoint(jj.jointID);

	}

	@Override
	public IShape raycastOne(Segment s, RaycastResult rr, boolean solidShapes,
			Object userData) {

		JNIBox2DShape shape = nRaycastOne(s.p1.x, s.p1.y, s.p2.x, s.p2.y, rr,
				solidShapes, userData);

		return shape;
		// return null;
	}

	@Override
	public void destroyBody(IBody body) {
		if (body instanceof JNIBox2DBody) {
			JNIBox2DBody jnibody = (JNIBox2DBody) body;
			nDestroyBody(jnibody.bodyID);

		} else
			System.out
					.println("WARNING JNI world destroybody: no jnibody instance provided");

	}

	@Override
	public void destroy() {
		nDestroy();
	}
	
	@Override
	public void sync() {
		nUpdateAllPositions();		
	}
	// implemented in C/C++
	// automatically dynamically linked at run-time
	// JVM looks for .so/.dll files in java.library.path
	native private void nUpdateAllPositions();


	private static boolean jniOk = false;
	public static boolean isJniOK() { return jniOk; }
	static {
		try  {
			System.loadLibrary("box2d");
			jniOk = true;
		} catch (java.lang.UnsatisfiedLinkError e) {
			jniOk = false;
		}
	}

	native private int nTestLib(int k);

	native public int nCreateWorld(float x1, float y1, float x2, float y2,
			float gravity_x, float gravity_y, boolean canSleep);

	// fills shapeList with IDs of shapes
	native private int nShapeQuery(float x1, float y1, float x2, float y2,
			int maxCount, Object[] shapeList);

	native public void step(float dt, int iterations);

	native public void nDestroy();

	native public int nCreateBody(float x, float y);

	native public void nDestroyBody(int bodyID);

	native public int nCreateRevoluteJoint(int body1, int body2, float x,
			float y);

	native public void nDestroyJoint(int jointID);

	native private JNIBox2DShape nRaycastOne(float p1x, float p1y, float p2x,
			float p2y, RaycastResult rr, boolean solidShapes, Object userData);

	native private void nUpdateAllPosditions();

}
