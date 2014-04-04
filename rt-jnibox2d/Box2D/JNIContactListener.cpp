/* 
 * File:   JNIContactListener.cpp
 * Author: stevijo
 * 
 * Created on 31. März 2014, 16:14
 */

#include "JNIContactListener.h"

JNIContactListener::JNIContactListener() {
}

JNIContactListener::JNIContactListener(const JNIContactListener& orig) {
}

JNIContactListener::~JNIContactListener() {
}

void JNIContactListener::BeginContact(b2Contact* contact) {
    b2Body* body1 = contact->GetFixtureA()->GetBody();
    b2Body* body2 = contact->GetFixtureB()->GetBody();
    b2Manifold* m = contact->GetManifold();
    
    contact->GetWorldManifold(&worldManifold);
    b2Vec2 point = worldManifold.points[0];
            env->CallVoidMethod(worldData,callback,
                    (jobject)body1->GetUserData(),
                    (jobject)body2->GetUserData(),
                    (jfloat)point.x,(jfloat)point.y);
          
}

void JNIContactListener::EndContact(b2Contact* contact) {
    
}

void JNIContactListener::SetEnv(JNIEnv* env) {
    this->env = env;
}