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
package de.sopamo.box2dbridge.jnibox2d;

import android.util.Log;
import de.sopamo.box2dbridge.IRayCastOutput;
import org.jbox2d.collision.FilterData;
import org.jbox2d.common.Vec2;

import android.util.FloatMath;

import de.sopamo.box2dbridge.IBody;
import de.sopamo.box2dbridge.IShape;

public class JNIBox2DBody implements IBody {

	int bodyID;

	float angularVelocity = 0;
	float angle = 0;
	Vec2 position = new Vec2();
	Vec2 velocity = new Vec2();

	Object userData;

	float linearDamping = 0;
	float angularDamping = 0;

	float inertiaInv;

	public JNIBox2DBody(int id) {
		this.bodyID = id;
		nAssociateJNIObject(id);
	}

	public void setUserData(Object userData) {
		this.userData = userData;
	}

	/**
	 * key method!! this function is called by the JNIBox2D engine.
	 * 
	 * upon every query with (update) set, this function is called for all
	 * bodies found.
	 * 
	 * @param x
	 * @param y
	 * @param vx
	 * @param vy
	 * @param angle
	 * @param avel
	 * @param inertiaInv
	 */
	public void callbackSetData(float x, float y, float vx, float vy,
			float angle, float avel, float inertiaInv) {
		position.x = x;
		position.y = y;

		velocity.x = vx;
		velocity.y = vy;

		this.angle = angle;
		this.angularVelocity = avel;

		this.inertiaInv = inertiaInv;

	}

	@Override
	public String toString() {

		return super.toString() + "_ID<" + bodyID + ">";
	}

	@Override
	public IShape createBox(float width, float height, float x, float y,
			float density, float angle) {

		nCreateBox(bodyID, width, height, x, y, 1, angle);

		// System.out.println("Created shape ID " + shapeID);

		IShape s = new JNIBox2DShape(0, this);
		return s;
	}

    public IShape createTriangle(float size, float x, float y, float density, float angle) {
        nCreateTriangle(bodyID, size, x, y, 1, angle);
        this.setAngle(angle);

        // System.out.println("Created shape ID " + shapeID);

        IShape s = new JNIBox2DShape(0, this);
        return s;
    }

    @Override
    public IShape createCircle(float radius, float x, float y, float density) {
        nCreateCircle(bodyID,radius,x,y,1);
        IShape s = new JNIBox2DShape(0,this);
        return s;
    }

    @Override
    public void setAngle(float angle) {
        nSetAngle(bodyID,angle);
    }

    @Override
	public void applyForce(Vec2 force, Vec2 point) {
        nApplyForce(bodyID, force.x, force.y, point.x, point.y);
	}

    @Override
    public void applyForceToCenter(Vec2 force) {
        nApplyForceToCenter(bodyID,force.x,force.y);
    }

    @Override
	public void applyTorque(float t) {
		nApplyTorque(bodyID, t);
	}

	@Override
	public float getAngle() {
		// updateData();
		return angle;
	}

	@Override
	public float getAngularVelocity() {
		// updateData();
		return angularVelocity;
	}

	@Override
	public Vec2 getLinearVelocity() {
		// updateData();
		return velocity;
	}

	@Override
	public Object getUserData() {
		return userData;
	}

	@Override
	public Vec2 getWorldCenter() {
		// updateData();
		return position;
	}

	@Override
	public Vec2 getWorldDirection(Vec2 v) {
		float sin = FloatMath.sin(angle);
		float cos = FloatMath.cos(angle);

		return new Vec2(cos * v.x + -sin * v.y, sin * v.x + cos * v.y);
	}

	@Override
	public void getWorldLocationToOut(Vec2 p, Vec2 q) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSleeping() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float getInertiaInv() {
		// updateData();
		return inertiaInv;
	}
	

	@Override
	public void setPosition(Vec2 pos) {
		position = pos;
        nSetPosition(bodyID, pos.x, pos.y);
	}

    @Override
    public IRayCastOutput rayCast(Vec2 start, Vec2 end, float fraction) {
        float frac = nRayCast(start.x,end.x,start.y,end.y,fraction);
        JNIRayCastOutput rayCastOutput = new JNIRayCastOutput();
        rayCastOutput.normal = new Vec2(nRayCastX(),nRayCastY());
        rayCastOutput.fraction = frac;
        return rayCastOutput;
    }

    @Override
	public void setAngularDamping(float d) {
		angularDamping = d;
		nSetDamping(bodyID, linearDamping, angularDamping);
	}

	@Override
	public void setLinearDamping(float d) {
		linearDamping = d;
		nSetDamping(bodyID, linearDamping, angularDamping);
	}

	@Override
	public void setMassFromShapes(){
	}

	@Override
	public FilterData getFilterData() {
		return null;
	}

	public void refilter(int categoryBits, int maskBits, int groupIndex) {
	}

	public void refilter() {

	}

	@Override
	public void destroyShape(IShape shape) {


	}

    @Override
    public void setAngularVelocity(float velocity) {
        nSetAngularVelocity(bodyID,velocity);
    }

    @Override
    public void setLinearVelocity(Vec2 velocity) {
        nSetLinearVelocity(bodyID,velocity.x,velocity.y);
    }

    static {
		System.loadLibrary("Box2D");
	}

	native void nCreateBox(int ID, float width, float height, float x, float y,
			float density, float angle);

    native void nCreateTriangle(int ID, float size, float x, float y, float density, float angle);

	native void nApplyForce(int ID, float fx, float fy, float px, float py);

	native public void nApplyTorque(int ID, float t);

	/*
	 * clumpsy, libbox2d stores this object as userData for each body. this way,
	 * when querying, body objects can be retrieved.
	 */
	native public void nAssociateJNIObject(int ID);

	native public void nSetDamping(int ID, float linearDamping,float angularDamping);
	native private void nSetPosition(int bodyID, float posx, float posy);

    native private void nSetLinearVelocity(int bodyID, float x, float y);

    native private void nSetAngularVelocity(int bodyID, float velocity);

    native public void nSetAngle(int bodyID,float angle);

    native public void nCreateCircle(int bodyID,float radius,float x, float y,float density);

    native private void nApplyForceToCenter(int bodyID,float x,float y);

    native private void nCreateLine(int bodyID,float x_start, float x_end, float y_start, float y_end, float thickness, float density);

    native private float nRayCast(float x_start,float x_end, float y_start, float y_end, float fraction);

    native private float nRayCastX();

    native private float nRayCastY();
}
