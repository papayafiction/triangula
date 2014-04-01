/*
 * JNIBox2DWorld.h
 *
 *  Created on: Oct 16, 2009
 *      Author: klm
 */

#ifndef JNIBOX2DWORLD_H_
#define JNIBOX2DWORLD_H_

#include <jni.h>
#include "Box2D.h"
#include "JNIRefs.h"

extern b2World* world;

// universal pools for easy java <-> jni <-> b2 intervention

// universal storage for bodies (duplicate of b2's)
#define MAX_BODIES 1000
extern b2Body* bodyList[MAX_BODIES];
extern jmethodID callback;
extern jobject worldData;

void throwExc(JNIEnv* env, const char* msg);



#endif /* JNIBOX2DWORLD_H_ */
