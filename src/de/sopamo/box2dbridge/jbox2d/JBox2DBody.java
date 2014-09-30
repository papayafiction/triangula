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


package de.sopamo.box2dbridge.jbox2d;

import de.sopamo.box2dbridge.IRayCastOutput;
import de.sopamo.box2dbridge.jnibox2d.JNIBox2DShape;
import org.jbox2d.collision.FilterData;
import org.jbox2d.collision.shapes.CircleDef;
import org.jbox2d.collision.shapes.PolygonDef;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import android.util.Log;

import de.sopamo.box2dbridge.IBody;
import de.sopamo.box2dbridge.IShape;

public class JBox2DBody implements IBody {

    Body body;

    public JBox2DBody(Body b) {
        body = b;
    }

    @Override
    public float getInertiaInv() {
        return body.m_invI;
    }

    @Override
    public IShape createBox(float halfWidth, float halfHeight, float x,
                            float y, float density, float angle) {

        PolygonDef pd = new PolygonDef();
        pd.density = density;

        Vec2 center = new Vec2(x, y);
        pd.setAsBox(halfWidth, halfHeight, center, angle);

        // System.out.println("created shape with density " + pd.density);

        Shape shape = body.createShape(pd);
        return new JBox2DShape(this, shape);

    }

    public IShape createTriangle(float size, float x, float y, float density, float angle) {

        PolygonDef pd = new PolygonDef();
        float hsize = size * 1f;
        pd.addVertex(new Vec2(0,-hsize));
        pd.addVertex(new Vec2(hsize,hsize));
        pd.addVertex(new Vec2(-hsize,hsize));
        pd.density = density;

        Shape shape = body.createShape(pd);
        return new JBox2DShape(this, shape);
    }

    @Override
    public IShape createCircle(float radius, float x, float y, float density) {
        CircleDef cd = new CircleDef();
        cd.localPosition = new Vec2(x,y);
        cd.radius = radius;
        cd.density = density;

        Shape shape = body.createShape(cd);
        return new JBox2DShape(this,shape);
    }

    @Override
    public void setAngle(float angle) {
        body.setXForm(body.getPosition(),angle);
    }

    @Override
    public void refilter(int categoryBits, int maskBits, int groupIndex) {

        FilterData fd = new FilterData();
        fd.categoryBits = categoryBits;
        fd.maskBits = maskBits;
        fd.groupIndex = groupIndex;

        Shape shapeList = body.getShapeList();
        if(shapeList != null)
            shapeList.setFilterData(fd);

        body.getWorld().refilter(body.getShapeList());
    }

    @Override
    public FilterData getFilterData() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setUserData(Object obj) {
        body.setUserData(obj);
    }

    @Override
    public void refilter() {
        FilterData fd = new FilterData();

        fd.categoryBits = 0x01;
        fd.maskBits = 0xFF;
        fd.groupIndex = 0;
        body.getShapeList().setFilterData(fd);

        body.getWorld().refilter(body.getShapeList());

    }

    @Override
    public void applyForce(Vec2 force, Vec2 point) {
        body.applyForce(force, point);
    }

    @Override
    public void applyForceToCenter(Vec2 force) {
        body.applyForce(force,getWorldCenter());
    }

    @Override
    public void applyTorque(float t) {
        body.applyTorque(t);

    }

    @Override
    public float getAngle() {
        return body.getAngle();
    }

    @Override
    public float getAngularVelocity() {
        return body.getAngularVelocity();
    }

    @Override
    public Vec2 getLinearVelocity() {
        return body.getLinearVelocity();
    }

    @Override
    public Object getUserData() {
        return body.getUserData();
    }

    @Override
    public Vec2 getWorldCenter() {
        return body.getWorldCenter();
    }

    @Override
    public Vec2 getWorldDirection(Vec2 p) {
        return body.getWorldDirection(p);
    }

    @Override
    public void getWorldLocationToOut(Vec2 p, Vec2 q) {
        body.getWorldLocationToOut(p, q);
    }

    @Override
    public boolean isSleeping() {
        return body.isSleeping();
    }

    @Override
    public void setAngularDamping(float d) {
        body.setAngularDamping(d);

    }

    @Override
    public void setLinearDamping(float d) {
        body.setLinearDamping(d);
    }

    @Override
    public void setMassFromShapes() {
        body.setMassFromShapes();

    }

    @Override
    public void destroyShape(IShape shape) {
        if (!(shape instanceof JBox2DShape))
            return;

        JBox2DShape s = (JBox2DShape) shape;
        body.destroyShape(s.shape);
    }

    @Override
    public void setAngularVelocity(float velocity) {
        body.setAngularVelocity(velocity);
    }

    @Override
    public void setLinearVelocity(Vec2 velocity) {
        body.setLinearVelocity(velocity);
    }

    @Override
    public void setPosition(Vec2 pos) {
        body.setXForm(pos, body.getAngle());

    }

    @Override
    public IRayCastOutput rayCast(Vec2 start, Vec2 end, float fraction) {
        return null;
    }

    public Body getBody() {
        return body;
    }
}