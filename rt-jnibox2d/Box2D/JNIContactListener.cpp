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
    b2WorldManifold worldManifold;
    contact->GetWorldManifold(&worldManifold);
    b2Vec2 point = worldManifold.points[0];
            env->CallVoidMethod(worldData,callback,
                    (jobject)((UserData*)body1->GetUserData())->globalRef,
                    (jobject)((UserData*)body2->GetUserData())->globalRef,
                    (jfloat)point.x,(jfloat)point.y);
          
}

void JNIContactListener::EndContact(b2Contact* contact) {
    
}

void JNIContactListener::SetEnv(JNIEnv* env) {
    this->env = env;
}