

/*
 *
 * JNI Box2D binder For RoboTanks
 * Kristian Lein-Mathisen 2009
 *
 */

#include "Headers/gen/com_kristianlm_robotanks_box2dbridge_jnibox2d_JNIBox2DWorld.h"
#include "JNIBox2DWorld.h"
#include "box2d.h"
#include "JNIRefs.h"
#include <jni.h>

//#include <iostream>
//using namespace std;

// we may be missing something here ... but JNIEXPORT is empty so this is so much more convenient!
#undef JNIEXPORT
#define JNIEXPORT extern "C"






void throwExc(JNIEnv* env, const char* msg) {
	env->ThrowNew(env->FindClass("java/lang/Exception"), msg);
}


// Global World variables


b2World* world = NULL;

b2Body* bodyList[MAX_BODIES];

//----------

b2Shape* shapeList[MAX_SHAPES];


b2Joint* jointList[MAX_JOINTS];






JNIEXPORT jint JNICALL Java_com_kristianlm_robotanks_box2dbridge_jnibox2d_JNIBox2DWorld_nTestLib
  (JNIEnv *, jobject, jint k)  {

	return k * 2;
	

}

/*
 * Class:     com_kristianlm_superelevation_box2dbridge_jnibox2d_JNIBox2DWorld
 * Method:    n_createWorld
 * Signature: (FFFFFFZ)I
 */
JNIEXPORT jint JNICALL Java_com_kristianlm_robotanks_box2dbridge_jnibox2d_JNIBox2DWorld_nCreateWorld
  (JNIEnv * env, jobject caller, jfloat x1, jfloat y1, jfloat x2, jfloat y2, jfloat gx, jfloat gy, jboolean canSleep) {


	for(int i = 0 ; i < MAX_BODIES ; i++) {
		bodyList[i] = 0;
	}

	for(int i = 0 ; i < MAX_SHAPES ; i++)
		shapeList[i] = 0;

	for(int i = 0 ; i < MAX_JOINTS ; i++)
		jointList[i] = 0;

	for(int i = 0 ; i < MAX_GLOBAL_REFS ; i++)
		globalRef[i] = 0;


	b2AABB aabb;
	aabb.lowerBound.Set(x1, y1);
	aabb.upperBound.Set(x2, y2);

	b2Vec2 gravity;
	gravity.Set(gx, gy);

	world = new b2World(aabb, gravity, canSleep);
	
	// ground body

	bodyList[0] = world->GetGroundBody();




}

/*
 * Class:     com_kristianlm_superelevation_box2dbridge_jnibox2d_JNIBox2DWorld
 * Method:    step
 * Signature: (FI)V
 */
JNIEXPORT void JNICALL Java_com_kristianlm_robotanks_box2dbridge_jnibox2d_JNIBox2DWorld_step
  (JNIEnv *, jobject, jfloat dt, jint iterations) {
	world->Step(dt, iterations, iterations);
}

/*
 * Class:     com_kristianlm_superelevation_box2dbridge_jnibox2d_JNIBox2DWorld
 * Method:    nDestroy
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_kristianlm_robotanks_box2dbridge_jnibox2d_JNIBox2DWorld_nDestroy
  (JNIEnv * env, jobject) {

	DeleteAllGlobalRefs(env);

//	cout << "NATIVE goodbye world\n" << flush;


	delete world;
	world = 0;

}

/*
 * Class:     com_kristianlm_superelevation_box2dbridge_jnibox2d_JNIBox2DWorld
 * Method:    nCreateBody
 * Signature: (FF)I
 */
JNIEXPORT jint JNICALL Java_com_kristianlm_robotanks_box2dbridge_jnibox2d_JNIBox2DWorld_nCreateBody
  (JNIEnv *, jobject, jfloat x, jfloat y) {

	b2BodyDef bd;
	bd.position.Set(x, y);

	// look for free spot and insert.
	for(int i = 0 ; i < MAX_BODIES ; i++) {
		if(bodyList[i] == 0) {
			bodyList[i] = world->CreateBody(&bd);
			bodyList[i]->SetUserData(0);
			return i;
		}
	}

	printf("no free body-slot!");

	return 0;

}


/*
 * Class:     com_kristianlm_robotanks_box2dbridge_jnibox2d_JNIBox2DWorld
 * Method:    nDestroyBody
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_kristianlm_robotanks_box2dbridge_jnibox2d_JNIBox2DWorld_nDestroyBody
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
		jniBodyClass = env->FindClass("com/kristianlm/robotanks/box2dbridge/jnibox2d/JNIBox2DBody");//env->GetObjectClass(caller);
		if(jniBodyClass == 0) {
			throwExc(env, "com/kristianlm/robotanks/box2dbridge/jnibox2d/JNIBox2DBody not found");
			return;
		}

//		cout << "class = " << jniBodyClass << "\n" << flush;

		callbackSetData = env->GetMethodID(jniBodyClass, "callbackSetData", "(FFFFFFF)V");
		if(callbackSetData == 0) {
			throwExc(env, "callbackSetData method ID (FFFFFFF)V not found");
			return;
		}
	}

	//b2Body *body = bodyList[id];
	float x = body->GetWorldCenter().x,
			y = body->GetWorldCenter().y,
			vx = body->GetLinearVelocity().x,
			vy = body->GetLinearVelocity().y,
			angle = body->GetAngle(),
			avel = body->GetAngularVelocity(),
			inertiaInv = 1.0f / body->GetInertia();

	// The JNIBody reference is made global and stored for all body userdata.
	if(body->GetUserData() != bodyList[0]->GetUserData())
		if(body->GetUserData() != 0)
			env->CallVoidMethod((jobject)body->GetUserData(), callbackSetData, x, y, vx, vy, angle, avel, inertiaInv);
//		else
//			cout << "dropped a body (no userdata) \n" << flush;

}
/*
 * Class:     com_kristianlm_superelevation_box2dbridge_jnibox2d_JNIBox2DWorld
 * Method:    nBodyQuery
 * Signature: (FFFFI[I)I
 */

JNIEXPORT jint JNICALL Java_com_kristianlm_robotanks_box2dbridge_jnibox2d_JNIBox2DWorld_nShapeQuery
  (JNIEnv *env, jobject, jfloat x1, jfloat y1, jfloat x2, jfloat y2, jint maxCount, jobjectArray array) {


	b2AABB aabb;
	aabb.lowerBound.Set(x1, y1);
	aabb.upperBound.Set(x2, y2);

	// list of shapes from query
	b2Shape** shapeList = new b2Shape* [maxCount];
	// get shape list
	int count = world->Query(aabb, shapeList, maxCount);

	int upCount = 0;
	for (int32 i = 0; i < count; ++i)
	{
		jobject JNIShape = (jobject) shapeList[i]->GetUserData();

		// update the JNI body position unless sleeping
		// sleeping bodies never change their position
		b2Body* body = shapeList[i]->GetBody();

//		printf("#%d: ", i);
//		if(body->IsStatic())
//			printf("\tstatic");
//		else
//			printf("\tdynamic");
//		printf("\tflag = %d ", body->IsJNIUpdated());

		// if the body is static and has updated flag set, don't update
		if(!body->IsStatic() || !body->IsJNIUpdated()) {
			updateBodyData(env, body);
			// set updated flag
			body->SetJNIUpdated();
			upCount++;

//			printf("\tnew up.=", body->IsJNIUpdated());
		}
//		printf(" | ");
		env->SetObjectArrayElement(array, i, JNIShape);
	}

//	printf(" - TOTAL %d updates\n", upCount);
//	fflush(stdout);


	delete [] shapeList;

	return count;
}

/*
 * Class:     com_kristianlm_robotanks_box2dbridge_jnibox2d_JNIBox2DWorld
 * Method:    nRaycastOne
 * Signature: (FFFFLorg/jbox2d/common/RaycastResult;ZLjava/lang/Object;)I
 */
JNIEXPORT jobject JNICALL Java_com_kristianlm_robotanks_box2dbridge_jnibox2d_JNIBox2DWorld_nRaycastOne
  (JNIEnv * env, jobject caller, jfloat p1x, jfloat p1y, jfloat p2x, jfloat p2y , jobject raycastRes, jboolean solidShapes, jobject userData) {

	float32 lambda = 1;
	b2Vec2 normal;

	b2Segment segment;
	segment.p1.x = p1x;
	segment.p1.y = p1y;

	segment.p2.x = p2x;
	segment.p2.y = p2y;

//	cout << "p1 = (" << segment.p1.x << ", " << segment.p1.y << ")  ";
//	cout << "p2 = (" << segment.p2.x << ", " << segment.p2.y << ") \n";


	b2Shape* shape = world->RaycastOne(segment, &lambda, &normal, solidShapes, NULL);

	jclass classRaycastResult = env->GetObjectClass(raycastRes);
	jfieldID fieldIDlambda = env->GetFieldID(classRaycastResult, "lambda", "F");

	env->SetFloatField(raycastRes, fieldIDlambda, lambda);

//	cout << "native lambda: " << lambda << "\t\t" << flush;

	if(shape == NULL)
		return NULL;


	return (jobject)shape->GetUserData();
}


/*
 * Class:     com_kristianlm_superelevation_box2dbridge_jnibox2d_JNIBox2DWorld
 * Method:    nCreateRevoluteJoint
 * Signature: (IIFF)V
 */
JNIEXPORT jint JNICALL Java_com_kristianlm_robotanks_box2dbridge_jnibox2d_JNIBox2DWorld_nCreateRevoluteJoint
  (JNIEnv *, jobject, jint id1, jint id2, jfloat aX, jfloat aY) {
	b2RevoluteJointDef rj;

	rj.enableLimit = false;
	rj.enableMotor = false;

	b2Vec2 anchor; anchor.Set(aX, aY);
	rj.Initialize(bodyList[id1], bodyList[id2], anchor);


	// find free spot and return its ID
	for(int i = 0 ; i < MAX_JOINTS ; i++) {
		if(jointList[i] == 0) {
			jointList[i] = world->CreateJoint(&rj);
			return i;
		}
	}

	return -1;
}


/*
 * Class:     com_kristianlm_superelevation_box2dbridge_jnibox2d_JNIBox2DWorld
 * Method:    nDestroyJoint
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_kristianlm_robotanks_box2dbridge_jnibox2d_JNIBox2DWorld_nDestroyJoint
  (JNIEnv *, jobject, jint jointID) {

	if(jointID < 0 && jointID >= MAX_JOINTS)
		return;

	printf("NATIVE destroying joint #%d (%X)\n", jointID, jointList[jointID]);

	if(jointList[jointID] == 0)
		return;

	world->DestroyJoint(jointList[jointID]);

	jointList[jointID] = 0;

	printf("destroying joint ok\n");
	fflush(stdout);

}


/*
 * Class:     com_kristianlm_robotanks_box2dbridge_jnibox2d_JNIBox2DWorld
 * Method:    nUpdateAllPositions
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_kristianlm_robotanks_box2dbridge_jnibox2d_JNIBox2DWorld_nUpdateAllPositions
  (JNIEnv * env, jobject) {

	// Callback for all JNIBox2DBodies

	for(int i = 0 ; i < MAX_BODIES ; i++) {
		b2Body* body = bodyList[i];

		// valid body pointer
		if(body != 0) {

			// check if we have userdata (should be pointer to JNIBox2DBody global ref.
			if(body->GetUserData() != 0)
				updateBodyData(env, body);
		}
	}
}
