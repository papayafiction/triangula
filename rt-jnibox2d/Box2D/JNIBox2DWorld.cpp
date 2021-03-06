

/*
 *
 * JNI Box2D binder For RoboTanks
 * Kristian Lein-Mathisen 2009
 *
 */

#include "JNIBox2DWorld.h"
#include "JNIContactListener.h"
#include "JNIBox2DRayCast.h"

//#include <iostream>
//using namespace std;

// we may be missing com/kristianlm/robotankssomething here ... but JNIEXPORT is empty so this is so much more convenient!
#undef JNIEXPORT
#define JNIEXPORT extern "C"



void throwExc(JNIEnv* env, const char* msg) {
	env->ThrowNew(env->FindClass("java/lang/Exception"), msg);
}


// Global World variables


b2World* world = NULL;

b2Body* bodyList[MAX_BODIES];

jmethodID callback;
jobject worldData;

JNIContactListener* listener;

//----------

JNIEXPORT jint JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld_nTestLib
  (JNIEnv *, jobject, jint k)  {

	return k * 2;
	

}

/*
 * Class:     de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld
 * Method:    n_createWorld
 * Signature: (FFFFFFB)I
 */
JNIEXPORT jint JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld_nCreateWorld
  (JNIEnv * env, jobject caller, jfloat x1, jfloat y1, jfloat x2, jfloat y2, jfloat gx, jfloat gy, jboolean canSleep) {

       
	for(int i = 0 ; i < MAX_BODIES ; i++) {
		bodyList[i] = 0;
	}

	for(int i = 0 ; i < MAX_GLOBAL_REFS ; i++)
		globalRef[i] = 0;


	b2Vec2 gravity;      
	gravity.Set(gx, gy);

	world = new b2World(gravity);
        
        jobject worldId = MakeGlobalRef(env,caller);
        jclass claz = env->FindClass("de/sopamo/box2dbridge/jnibox2d/JNIBox2DWorld");

        callback = env->GetMethodID(claz,"listenerCallback",
            "(Lde/sopamo/box2dbridge/jnibox2d/JNIBox2DBody;Lde/sopamo/box2dbridge/jnibox2d/JNIBox2DBody;FF)V");
        worldData = worldId;
        
        b2BodyDef bd;
        bd.position.Set(0,0);
        bd.type = b2_staticBody;
        
        

        bodyList[0] = world->CreateBody(&bd);
      	// ground body
        return 0;
}

/*
 * Class:     de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld
 * Method:    nStep
 * Signature: (FI)V
 */
JNIEXPORT void JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld_nStep
  (JNIEnv * env, jobject, jfloat dt, jint iterations) {
    listener->SetEnv(env);
    world->Step(dt, iterations,iterations);
}

/*
 * Class:     de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld
 * Method:    nDestroy
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld_nDestroy
  (JNIEnv * env, jobject) {

	DeleteAllGlobalRefs(env);

//	cout << "NATIVE goodbye world\n" << flush;


	delete world;
	world = 0;

}

/*
 * Class:     de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld
 * Method:    nCreateBody
 * Signature: (FF)I
 */
JNIEXPORT jint JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld_nCreateBody
  (JNIEnv *, jobject, jfloat x, jfloat y) {

	b2BodyDef bd;
        bd.type = b2_dynamicBody;
        bd.allowSleep = false;
	bd.position.Set(x, y);
	// look for free spot and insert.
	for(int i = 0 ; i < MAX_BODIES ; i++) {
		if(bodyList[i] == 0) {
			bodyList[i] = world->CreateBody(&bd);
			return i;
		}
	}

	printf("no free body-slot!");

	return 0;

}

/*
 * Class:     de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld
 * Method:    nCreateStaticBody
 * Signature: (FF)I
 */
JNIEXPORT jint JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld_nCreateStaticBody
  (JNIEnv *, jobject, jfloat x, jfloat y) {

	b2BodyDef bd;
        bd.type = b2_staticBody;
	bd.position.Set(x, y);
	// look for free spot and insert.
	for(int i = 0 ; i < MAX_BODIES ; i++) {
		if(bodyList[i] == 0) {
			bodyList[i] = world->CreateBody(&bd);
			return i;
		}
	}

	printf("no free body-slot!");

	return 0;

}


/*
 * Class:     de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld
 * Method:    nDestroyBody
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld_nDestroyBody
  (JNIEnv * env, jobject caller, jint bodyID) {

	if(bodyID < 0 || bodyID >= MAX_BODIES) {
		throwExc(env, "destroy body ID outta range.");
		return ;
	}

	world->DestroyBody(bodyList[bodyID]);
	// free spot
	bodyList[bodyID] = 0;


}


int findId(b2Body* body)
{
	for(int i = 0 ; i <= MAX_BODIES ; i++) {
		if(body == bodyList[i]) {
			return i;
		}
	}
//	cout << "NATIVE: did not find body " << body << "\n" << flush;
	return -1;
}

jclass jniBodyClass = 0;
jmethodID callbackSetData = 0;


void updateBodyData(JNIEnv * env, b2Body* body) {
	if(jniBodyClass == 0) {
		jniBodyClass = env->FindClass("de/sopamo/box2dbridge/jnibox2d/JNIBox2DBody");//env->GetObjectClass(caller);
		if(jniBodyClass == 0) {
			throwExc(env, "de/sopamo/box2dbridge/jnibox2d/JNIBox2DBody not found");
			return;
		}

		callbackSetData = env->GetMethodID(jniBodyClass, "callbackSetData", "(FFFFFFF)V");
		if(callbackSetData == 0) {
			throwExc(env, "callbackSetData method ID (FFFFFFF)V not found");
			return;
		}
	}
        

	float x = body->GetWorldCenter().x,
			y = body->GetWorldCenter().y,
			vx = body->GetLinearVelocity().x,
			vy = body->GetLinearVelocity().y,
			angle = body->GetAngle(),
			avel = body->GetAngularVelocity(),
			inertiaInv = 1.0f / body->GetInertia();
        
        UserData* data = (UserData*)body->GetUserData();
        UserData* data2 = (UserData*)bodyList[0]->GetUserData();

	if(data != data2) {                       
			env->CallVoidMethod((jobject)((UserData*)body->GetUserData())->globalRef, callbackSetData, x, y, vx, vy, angle, avel, inertiaInv);
        }
}


/*
 * Class:     de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld
 * Method:    nUpdateAllPositions
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld_nUpdateAllPositions
  (JNIEnv * env, jobject) {

	// Callback for all JNIBox2DBodies
    
	for(int i = 0 ; i < MAX_BODIES ; i++) {
		b2Body* body = bodyList[i];
                
		// valid body pointer
		if(body != 0) {
                        
			// check if we have userdata (should be pointer to JNIBox2DBody global ref.
			if(body->GetUserData() != NULL)
				updateBodyData(env, body);
		}
	}

}



/*
* Class:     de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld
* Method:    nSetGravity
* Signature: (FF)V
*/
JNIEXPORT void JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld_nSetGravity
(JNIEnv *, jobject, jfloat gravity_x, jfloat gravity_y) {
    b2Vec2 gravity;
    gravity.Set(gravity_x,gravity_y);
    world->SetGravity(gravity);
}

JNIEXPORT void JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld_nSetContactListener
(JNIEnv * env, jobject) {
    listener = new JNIContactListener();
    listener->SetEnv(env);
    world->SetContactListener(listener);
}


