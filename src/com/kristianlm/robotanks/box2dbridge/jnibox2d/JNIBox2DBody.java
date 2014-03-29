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

import org.jbox2d.collision.FilterData;
import org.jbox2d.common.Vec2;

import android.util.FloatMath;

import com.kristianlm.robotanks.box2dbridge.IBody;
import com.kristianlm.robotanks.box2dbridge.IShape;

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

		System.out.println("associated " + this + " with " + id);
		// let libbox2d know which java object stores the lib's body object
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

		int shapeID = nCreateBox(bodyID, width, height, x, y, density, angle);

		// System.out.println("Created shape ID " + shapeID);

		IShape s = new JNIBox2DShape(shapeID, this);
		return s;
	}

	@Override
	public void applyForce(Vec2 force, Vec2 point) {
		nApplyForce(bodyID, force.x, force.y, point.x, point.y);
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
		nSetPosition(bodyID, pos.x, pos.y);		
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
	public void setMassFromShapes() {
		nSetMassFromShapes(bodyID);
	}

	@Override
	public FilterData getFilterData() {
		FilterData fd = new FilterData();
		nGetFilterData(bodyID, fd);
		return fd;
	}

	public void refilter(int categoryBits, int maskBits, int groupIndex) {
		nRefilter(bodyID, categoryBits, maskBits, groupIndex);
	}

	public void refilter() {
		nRefilter(bodyID, 0x01, 0xFF, 0);
	}

	@Override
	public void destroyShape(IShape shape) {
		if (!(shape instanceof JNIBox2DShape))
			return;

		JNIBox2DShape s = (JNIBox2DShape) shape;
		nDestroyShape(bodyID, s.shapeID);

	}

	static {
		System.loadLibrary("box2d");
	}

	native void nUpdateData(int ID);

	native int nCreateBox(int ID, float width, float height, float x, float y,
			float density, float angle);

	native int nCreateShape(int ID, float density, float[] ordered_vlist);

	native void nApplyForce(int ID, float fx, float fy, float px, float py);

	native public void nApplyTorque(int ID, float t);

	/*
	 * clumpsy, libbox2d stores this object as userData for each body. this way,
	 * when querying, body objects can be retrieved.
	 */
	native public void nAssociateJNIObject(int ID);

	native public void nSetMassFromShapes(int ID);

	native public void nSetDamping(int ID, float linearDamping,
			float angularDamping);

	native private void nRefilter(int ID, int categoryBits, int maskBits,
			int groupIndex);

	native private void nGetFilterData(int bodyID, FilterData fd);

	native private void nDestroyShape(int bodyID, int shapeID);
	
	native private void nSetPosition(int bodyID, float posx, float posy);

}
