/*
 * JNIBox2DShape.cpp
 *
 *  Created on: Oct 29, 2009
 *      Author: klm
 */


#include "Headers/gen/com_kristianlm_robotanks_box2dbridge_jnibox2d_JNIBox2DShape.h"

#include "box2d.h"
#include "JNIBox2DWorld.h"

#include <jni.h>

#include <string.h>

//#include <iostream>
//using namespace std;

#undef JNIEXPORT
#define JNIEXPORT extern "C"


/*
 *
 * The associated shape, shapeList[id] will store the original JNISHape reference
 * (making it global) as UserData.
 *
 * Class:     com_kristianlm_superelevation_box2dbridge_jnibox2d_JNIBox2DShape
 * Method:    nAssociateShape
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_kristianlm_robotanks_box2dbridge_jnibox2d_JNIBox2DShape_nAssociateShape
  (JNIEnv * env, jobject caller, jint shapeID) {


	// caller is JNIBox2DSHape
	jobject globalRef = MakeGlobalRef(env, caller);

	shapeList[shapeID]->SetUserData(globalRef);


}
