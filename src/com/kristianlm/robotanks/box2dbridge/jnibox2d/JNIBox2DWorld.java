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

import android.util.Log;
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
		
		/*
		 * IShape [] is = new JNIBox2DShape [bodies] ; for(int i = 0 ; i <
		 * bodies ; i++) { is[i] = new JNIBox2DShape(bodyList[i]); }
		 */
		// System.out.println("Found " + bodies + " bodies in query.");
		return shapeList;
	}

	@Override
	public void destroyJoint(IJoint j) {
		if (!(j instanceof JNIBox2DJoint)) {
			System.out
					.println("Cannot destroy IJoint of type other than JNIBox2DJoint");
			return;
		}


	}

	@Override
	public IShape raycastOne(Segment s, RaycastResult rr, boolean solidShapes,
			Object userData) {

		return null;
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
    public IJoint createRevoluteJoint(IBody body1, IBody body2, float anchorX, float anchorY) {
        return null;
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
			System.loadLibrary("Box2D");
			jniOk = true;
		} catch (java.lang.UnsatisfiedLinkError e) {
			jniOk = false;
		}
	}

    public void setGravity(float x,float y) {
        nSetGravity(x,y);
    }

    @Override
    public void step(float dt, int iterations) {
        nStep(dt,iterations);
    }

	native private int nTestLib(int k);

	native public int nCreateWorld(float x1, float y1, float x2, float y2,
			float gravity_x, float gravity_y, boolean canSleep);

	native public void nStep(float dt, int iterations);

	native public void nDestroy();

	native public int nCreateBody(float x, float y);

	native public void nDestroyBody(int bodyID);

    native public void nSetGravity(float gravity_x, float gravity_y);

}
