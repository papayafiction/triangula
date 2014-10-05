/* 
 * File:   JNIContactListener.h
 * Author: stevijo
 *
 * Created on 31. März 2014, 16:14
 */

#ifndef JNICONTACTLISTENER_H
#define	JNICONTACTLISTENER_H

#include "Box2D.h"
#include <jni.h>
#include <android/log.h>
#include "JNIBox2DWorld.h"
#include "JNIRefs.h"

class JNIContactListener : public b2ContactListener {
public:
    JNIContactListener();
    JNIContactListener(const JNIContactListener& orig);
    virtual ~JNIContactListener();
    void SetEnv(JNIEnv* env);
    void BeginContact(b2Contact* contact);
    void EndContact(b2Contact* contact);
private:
    JNIEnv* env;
};

#endif	/* JNICONTACTLISTENER_H */

