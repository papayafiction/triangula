/*
 * JNIBox2DBody.cpp
 *
 *  Created on: Oct 15, 2009
 *      Author: klm
 */

#include "Headers/gen/de_sopamo_box2dbridge_jnibox2d_JNIBox2DBody.h"

#include "Box2D.h"
#include "JNIBox2DWorld.h"

#include <jni.h>
#include <math.h>

#include <stdio.h>
//#include <iostream>
//using namespace std;

#undef JNIEXPORT
#define JNIEXPORT extern "C"


/*
 * Class:     de_sopamo_box2dbridge_jnibox2d_JNIBox2DBody
 * Method:    updateData
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DBody_nUpdateData
  (JNIEnv * env, jobject caller, jint id) {


	return;

	if(bodyList[id] ==NULL){
		return ;
	}

	jclass bodyClass = env->GetObjectClass(caller);
	jmethodID setValuesId = env->GetMethodID(bodyClass, "callbackSetData", "(FFFFFFF)V");

	b2Body *body = bodyList[id];
	float x = body->GetWorldCenter().x,
			y = body->GetWorldCenter().y,
			vx = body->GetLinearVelocity().x,
			vy = body->GetLinearVelocity().y,
			angle = body->GetAngle(),
			avel = body->GetAngularVelocity(),
			inertiaInv = 1.0f / body->GetInertia();

	env->CallVoidMethod(caller, setValuesId, x, y, vx, vy, angle, avel, inertiaInv);

}

/*
 * Class:     de_sopamo_box2dbridge_jnibox2d_JNIBox2DBody
 * Method:    nCreateBox
 * Signature: (FFFFFIII)V
 *
 *
 * 	native int nCreateBox(float width, float height, float x, float y,
			float density, int categoryBits, int maskBits, int groupIndex);
 *
 *
 */
JNIEXPORT void JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DBody_nCreateBox
  (JNIEnv *, jobject, jint ID, jfloat width, jfloat height, jfloat x, jfloat y, jfloat density, jfloat angle) {

	if(bodyList[ID] == 0)
		return;

        b2Vec2 center;
        center.Set(x,y);

	b2PolygonShape ps;
        ps.SetAsBox(width,height,center,angle);
        
        bodyList[ID]->CreateFixture(&ps,density);

}


/*
 * Class:     de_sopamo_box2dbridge_jnibox2d_JNIBox2DBody
 * Method:    nCreateTriangle
 * Signature: (IFFFFF)V
 *
 */
JNIEXPORT void JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DBody_nCreateTriangle
  (JNIEnv *, jobject, jint ID, jfloat size, jfloat x, jfloat y, jfloat density, jfloat angle) {

	if(bodyList[ID] == 0)
		return;

        b2Vec2 vertices[3];
        
        vertices[0].Set(0,-size);
        vertices[1].Set(size,size);
        vertices[2].Set(-size,size);
        
        b2PolygonShape ps;
        ps.Set(vertices,3);
        
        bodyList[ID]->CreateFixture(&ps,density);

}

JNIEXPORT void JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DBody_nCreateCircle
  (JNIEnv *, jobject, jint ID, jfloat radius, jfloat x, jfloat y, jfloat density) {

	if(bodyList[ID] == 0)
		return;

        b2CircleShape shape;
        shape.m_radius = radius;
        shape.m_p.Set(x,y);
        
        bodyList[ID]->CreateFixture(&shape,density);

}

/*
 * Class:     de_sopamo_box2dbridge_jnibox2d_JNIBox2DBody
 * Method:    nAssociateJNIObject
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DBody_nAssociateJNIObject
  (JNIEnv * env, jobject caller, jint id) {

	if(id < 0 || id >= MAX_BODIES)
		return;

	if(bodyList[id] == 0)
		return;

	jobject gref = MakeGlobalRef(env, caller);
	// gref is now JNIBOx2DBody object
        bodyList[id]->SetUserData(gref);

}

/*
 * Class:     de_sopamo_box2dbridge_jnibox2d_JNIBox2DBody
 * Method:    nApplyForce
 * Signature: (IFFFF)V
 */
JNIEXPORT void JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DBody_nApplyForce
  (JNIEnv * env, jobject caller, jint ID, jfloat fx, jfloat fy, jfloat wx, jfloat wy) {

	if(ID < 0 || ID >= MAX_BODIES) {
		throwExc(env, "ID #%d out of bounds");
		return;
	}

	// make sure body isn't dead
	if(bodyList[ID] == 0)
		return;

	b2Vec2 force, point;
	force.Set(fx, fy);
	point.Set(wx, wy);
	bodyList[ID]->ApplyForce(force, point,true);

}

/*
 * Class:     de_sopamo_box2dbridge_jnibox2d_JNIBox2DBody
 * Method:    nApplyTorque
 * Signature: (IF)V
 */
JNIEXPORT void JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DBody_nApplyTorque
  (JNIEnv * env, jobject, jint ID, jfloat torque) {

	if(ID < 0 || ID >= MAX_BODIES) {
		throwExc(env, "ID #%d out of bounds");
		return;
	}
	if(bodyList[ID] == 0)
		return;

	bodyList[ID]->ApplyTorque(torque,true);

	if(torque == 0.5f)
	{
//		cout << " body mass: " << bodyList[ID]->GetMass() << " ... body torque .. " << bodyList[ID]->GetAngularVelocity() << "\n" << flush;
	}
}


/*
 * Class:     de_sopamo_box2dbridge_jnibox2d_JNIBox2DBody
 * Method:    nSetDamping
 * Signature: (IFF)V
 */
JNIEXPORT void JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DBody_nSetDamping
  (JNIEnv * env, jobject, jint ID, jfloat liDa, jfloat anDa) {
	if(ID < 0 || ID >= MAX_BODIES) {
		throwExc(env, "ID #%d out of bounds");
		return;
	}
	if(bodyList[ID] == 0)
		return;

        bodyList[ID]->SetLinearDamping(liDa);
        bodyList[ID]->SetAngularDamping(anDa);
}



/*
 * Class:     de_sopamo_box2dbridge_jnibox2d_JNIBox2DBody
 * Method:    nSetPosition
 * Signature: (FF)V
 */
JNIEXPORT void JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DBody_nSetPosition
  (JNIEnv *, jobject, jint id, jfloat x, jfloat y) {

	if(bodyList[id] == 0)
		return;

	b2Vec2 pos;
	pos.Set(x, y);
	bodyList[id]->SetTransform(pos, bodyList[id]->GetAngle());

}

JNIEXPORT void JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DBody_nSetAngularVelocity
  (JNIEnv *, jobject, jint id, jfloat velocity) {

	if(bodyList[id] == 0)
		return;

	bodyList[id]->SetAngularVelocity(velocity);

}

JNIEXPORT void JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DBody_nSetLinearVelocity
  (JNIEnv *, jobject, jint id, jfloat x, jfloat y) {

	if(bodyList[id] == 0)
		return;
        
        b2Vec2 velocity;
        velocity.Set(x,y);
        bodyList[id]->SetLinearVelocity(velocity);
}

JNIEXPORT void JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DBody_nSetAngle
  (JNIEnv *, jobject, jint id, jfloat angle) {

	if(bodyList[id] == 0)
		return;
        
        bodyList[id]->SetTransform(bodyList[id]->GetWorldCenter(),angle);
}

