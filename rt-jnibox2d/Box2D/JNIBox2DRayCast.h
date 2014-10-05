/* 
 * File:   JNIBox2dRayCast.h
 * Author: Stefan
 *
 * Created on 2. Oktober 2014, 12:56
 */

#ifndef JNIBOX2DRAYCAST_H
#define	JNIBOX2DRAYCAST_H

#include <jni.h>
#include "Box2D.h"
#include "JNIRefs.h"
#include <android/log.h>
#include "JNIBox2DWorld.h"
#include <vector>

extern b2Vec2 rotate(b2Vec2 vec,float angle);
extern b2Vec2 mul(b2Vec2 vec, float times);
extern b2Vec2 add(b2Vec2 vec1, b2Vec2 vec2);
extern b2Vec2 sub(b2Vec2 vec1, b2Vec2 vec2);



#endif	/* JNIBOX2DRAYCAST_H */

