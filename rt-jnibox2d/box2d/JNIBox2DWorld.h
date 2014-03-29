/*
 * JNIBox2DWorld.h
 *
 *  Created on: Oct 16, 2009
 *      Author: klm
 */

#ifndef JNIBOX2DWORLD_H_
#define JNIBOX2DWORLD_H_

#include <jni.h>
#include "box2d.h"
#include "JNIRefs.h"

extern b2World* world;

// universal pools for easy java <-> jni <-> b2 intervention

// universal storage for bodies (duplicate of b2's)
#define MAX_BODIES 1000
extern b2Body* bodyList[MAX_BODIES];

#define MAX_SHAPES 1000
extern b2Shape* shapeList [MAX_SHAPES];

#define MAX_JOINTS 20
extern b2Joint* jointList [MAX_JOINTS];





void throwExc(JNIEnv* env, const char* msg);



#endif /* JNIBOX2DWORLD_H_ */
