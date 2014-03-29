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

import org.jbox2d.collision.FilterData;
import org.jbox2d.common.Vec2;

public interface IBody {

	float getAngle();

	public IShape createBox(float halfWidth, float halfHeight, float x,
			float y, float density, float angle);

	// public IShape createFromGeometry(Geometry g);
	// public void deleteBox();

	public void setMassFromShapes();

	public Vec2 getWorldCenter();

	public void applyForce(Vec2 force, Vec2 point);

	public void setLinearDamping(float d);

	public void setAngularDamping(float d);

	public void applyTorque(float t);

	public float getAngularVelocity();

	public Vec2 getLinearVelocity();

	public float getInertiaInv();

	public boolean isSleeping();

	public Vec2 getWorldDirection(Vec2 p);

	public void getWorldLocationToOut(Vec2 p, Vec2 q);

	public Object getUserData();

	public void refilter(int categoryBits, int maskBits, int groupIndex);

	public void refilter();

	public FilterData getFilterData();

	public void destroyShape(IShape shape);
	
	
	public void setPosition(Vec2 pos);

}
