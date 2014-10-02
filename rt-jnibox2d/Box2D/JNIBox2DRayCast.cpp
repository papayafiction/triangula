/* 
 * File:   JNIBox2dRayCast.cpp
 * Author: Stefan
 * 
 * Created on 2. Oktober 2014, 12:56
 */

#include "JNIBox2DRayCast.h"
#include "Headers/gen/de_sopamo_box2dbridge_jnibox2d_JNIBox2DRayCast.h"
#include <math.h> 

#define APPNAME "Triangula"

b2Vec2 rotate(b2Vec2 vec,float angle) {
    b2Vec2 newVec;
    newVec.x = vec.x*cos(angle)-vec.y*sin(angle);
    newVec.y = vec.x*sin(angle)+vec.y*cos(angle);
    return newVec;
}

b2Vec2 add(b2Vec2 vec1, b2Vec2 vec2) {
    b2Vec2 newVec;
    newVec.x = vec1.x+vec2.x;
    newVec.y = vec1.y+vec2.y;
    return newVec;
}

b2Vec2 sub(b2Vec2 vec1, b2Vec2 vec2) {
    b2Vec2 newVec;
    newVec.x = vec1.x-vec2.x;
    newVec.y = vec1.y-vec2.y;
    return newVec;
}

b2Vec2 mul(b2Vec2 vec, float times) {
    vec.x = vec.x*times;
    vec.y = vec.y*times;
    return vec;
}

bool in(jint* array,int size, int value) {
    for(int i=0;i<size;i++) {
        if(value == array[i]) return true; 
    }
    return false;
}

JNIEXPORT jobjectArray JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DRayCast_nRayCast
  (JNIEnv* env, jobject obj, jfloat x,jfloat y, jintArray list) {
    jclass vector = env->FindClass("org/jbox2d/common/Vec2");
    jmethodID vector_constructor = env->GetMethodID(vector,"<init>","(FF)V");
    
    jint size = env->GetArrayLength(list);
    jboolean* result;
    jint* bodyIds = env->GetIntArrayElements(list,result);
        
    std::vector<b2Vec2> points;
    b2Vec2 player;
    player.x = x;
    player.y = y;

    for(int i=0;i< MAX_BODIES;i++) {
        if(bodyList[i] == NULL) break;
        b2Body* body = bodyList[i];
        b2Vec2 pos = body->GetPosition();
        for(b2Fixture* f = body->GetFixtureList();f;f=f->GetNext()) {
            b2Shape::Type type = f->GetType();
            if(type == b2Shape::e_circle) {
                b2CircleShape* circleShape = (b2CircleShape*)f->GetShape();
                continue;
            } else if(type == b2Shape::e_polygon) {
                b2PolygonShape* polygonShape = (b2PolygonShape*)f->GetShape();
                int vertexes = polygonShape->GetVertexCount();
                for(int j=0;j<vertexes;j++) {    
                    b2Vec2 edge = add(pos,mul(rotate(polygonShape->GetVertex(j),body->GetAngle()),0.98f));
                    b2RayCastInput input;
                    input.maxFraction = 1;
                    input.p1 = player;
                    input.p2 = edge;
                    
                    float closestFraction = input.maxFraction;
                    for(int k = 0; k< MAX_BODIES; k++) {
                        if(bodyList[k] == NULL) break;
                        if(in(bodyIds,size,k)) continue;
                        b2Body* b = bodyList[k];
                        for(b2Fixture* f2 = b->GetFixtureList();f2;f2=f2->GetNext()) {
                            if(f2->GetType() != b2Shape::e_polygon) continue;
                            b2RayCastOutput output;
                            if(!f2->RayCast(&output,input,0))
                                continue;
                            if(output.fraction < closestFraction) {
                                closestFraction = output.fraction;
                            }
                        }
                    }             
                    b2Vec2 point = add(player,mul(sub(edge,player),closestFraction));
                    points.push_back(point);     
                    
                }                
            }                      
        }
    }
    env->ReleaseIntArrayElements(list,bodyIds,0);
    
    
    jobjectArray ret = env->NewObjectArray(points.size(),vector,0);
    for(int i=0;i<points.size();i++) {
        jobject javaVec = env->NewObject(vector,vector_constructor,(jfloat)points[i].x,(jfloat)points[i].y);
        env->SetObjectArrayElement(ret,i,javaVec);
        env->DeleteLocalRef(javaVec);
    }
    env->DeleteLocalRef(vector);
    return ret;
}

