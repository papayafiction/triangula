/*
 * JNIBox2DBody.cpp
 *
 *  Created on: Oct 15, 2009
 *      Author: klm
 */


#include "JNIBox2DBody.h"
#include "JNIBox2DRayCast.h"


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

void setStaticEdges(b2Body* b) {
    UserData* data = (UserData*)b->GetUserData();
    std::vector<b2Vec2> points;
    b2Vec2 vec;
    vec.x = 0.005f;
    vec.y = 0.005f;
    for(b2Fixture* f=b->GetFixtureList();f;f=f->GetNext()) {
        if(f->GetType() == b2Shape::e_polygon) {
            b2PolygonShape* ps = (b2PolygonShape*)f->GetShape();
            for(int i=0;i<ps->GetVertexCount();i++) {
                points.push_back(add(b->GetPosition(),add(rotate(ps->GetVertex(i),b->GetAngle()),vec)));
                points.push_back(add(b->GetPosition(),sub(rotate(ps->GetVertex(i),b->GetAngle()),vec)));
            }
        }
    }
    if(data == 0 ) data = new UserData;
    data->points = points;
    b->SetUserData(data);   
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
        if(ID != 0) {
            setStaticEdges(bodyList[ID]);
        }
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
        setStaticEdges(bodyList[ID]);
}

/*
 Signature (IFFFFFF)V
 */
JNIEXPORT void JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DBody_nCreateLine
(JNIEnv *, jobject, jint ID, jfloat x_start, jfloat x_end, jfloat y_start, jfloat y_end, jfloat width, jfloat density) {
    if(bodyList[ID] == 0)
        return;
    b2Vec2 vertices[4];
    vertices[0].Set(x_start,y_start);
    vertices[1].Set(x_start+width,y_start+width);
    vertices[2].Set(x_end,y_end);
    vertices[3].Set(x_end+width,y_end+width);
    
    b2PolygonShape ps;
    ps.Set(vertices,4);
    bodyList[ID]->CreateFixture(&ps,density);
    setStaticEdges(bodyList[ID]);
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
        UserData* data = new UserData;
	jobject gref = MakeGlobalRef(env, caller);
        data->globalRef = gref;
        data->body = id;
	// gref is now JNIBOx2DBody object
        bodyList[id]->SetUserData(data);

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

JNIEXPORT void JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DBody_nApplyForceToCenter
  (JNIEnv * env, jobject caller, jint ID, jfloat fx, jfloat fy) {

	if(ID < 0 || ID >= MAX_BODIES) {
		throwExc(env, "ID #%d out of bounds");
		return;
	}

	// make sure body isn't dead
	if(bodyList[ID] == 0)
		return;

	b2Vec2 force;
	force.Set(fx, fy);
	bodyList[ID]->ApplyForceToCenter(force, true);

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
        setStaticEdges(bodyList[id]);
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

jclass jniBodyClass2 = 0;
jmethodID callbackSetData2 = 0;

jfloat xRay = 0;
jfloat yRay = 0;

JNIEXPORT jfloat JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DBody_nRayCast
  (JNIEnv* env, jobject, jfloat x_start, jfloat x_end, jfloat y_start, jfloat y_end, jfloat fraction) {

        b2Vec2 p1,p2;
        p1.Set(x_start,y_start);
        p2.Set(x_end,y_end);
        
        b2RayCastInput rayCastInput;
        rayCastInput.p1 = p1;
        rayCastInput.p2 = p2;
        rayCastInput.maxFraction = fraction;
        
        float closestFraction = fraction;
        b2Vec2 intersectionNormal(0,0);
        for(int i = 0; i<= MAX_BODIES; i++) {
            if(bodyList[i] == NULL) break;
            b2Body* b = bodyList[i];
            for(b2Fixture* f = b->GetFixtureList();f;f=f->GetNext()) {
                
                b2RayCastOutput output;
                if(!f->RayCast(&output,rayCastInput,0))
                    continue;
                if(output.fraction < closestFraction) {
                    closestFraction = output.fraction;
                    intersectionNormal = output.normal;
                }
            }
        }
        xRay = intersectionNormal.x;
        yRay = intersectionNormal.y;

        return (jfloat) closestFraction;
}

JNIEXPORT jfloat JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DBody_nRayCastX
  (JNIEnv* env, jobject) {
    return xRay;
  }
  JNIEXPORT jfloat JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DBody_nRayCastY
    (JNIEnv* env, jobject) {
    return yRay;
    }

