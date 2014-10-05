/* 
 * File:   JNIBox2dRayCast.cpp
 * Author: Stefan
 * 
 * Created on 2. Oktober 2014, 12:56
 */

#include "JNIBox2DRayCast.h"
#include "JNIContactListener.h"
#include <math.h> 

#define APPNAME "Triangula"

#undef JNIEXPORT
#define JNIEXPORT extern "C"

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

b2Vec2 div(b2Vec2 vec, float times) {
    vec.x = vec.x/times;
    vec.y = vec.y/times;
    return vec;
}


float scalar(b2Vec2 v1,b2Vec2 v2) {
    return v1.x*v2.x+v1.y*v2.y;
}

float length(b2Vec2 v1) {
    return sqrt(v1.x*v1.x+v1.y*v1.y);
}

float angle(b2Vec2 v1,b2Vec2 v2) {
    float scal = scalar(v1,v2);
    float len = length(v1)*length(v2);
    float angle = acos(scal/len);
    if(v1.y<v2.y) angle = 2*M_PI-angle;
    return angle;
}

b2Vec2 stdVec;
b2Vec2 player;

bool comparator(b2Vec2 v1, b2Vec2 v2) {
    return angle(sub(v1,player),stdVec)<angle(sub(v2,player),stdVec);
}

class RayCaster : public b2RayCastCallback {
public:
    float closest;
    jint* bodyIds;
    int size;
    bool in(jint* array,int size, int value) {
        for(int i=0;i<size;i++) {
            if(value == array[i]) return true; 
        }
        return false;
    }
    float32 ReportFixture(b2Fixture* fixture, const b2Vec2& point, const b2Vec2& normal, float32 fraction){
        if(fixture->GetType() != b2Shape::e_polygon) return -1;
        UserData* data = (UserData*)fixture->GetBody()->GetUserData();
        if(data != 0) {
            if(this->in(bodyIds,size,data->body)) return -1;
        }
        closest = fraction;
        return fraction;
    }
};

class Query : public b2QueryCallback {
public:
    std::vector<b2Body*> bodies;
    bool ReportFixture(b2Fixture* fixture){
        if(((UserData*)fixture->GetBody()->GetUserData())->body == 0) return true;
        bodies.push_back(fixture->GetBody());
        return true;
    };
};

JNIEXPORT jobject JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DRayCast_nRayCast
  (JNIEnv* env, jobject obj, jfloat x,jfloat y, jintArray list) {
    jclass vector = env->FindClass("org/jbox2d/common/Vec2");
    jclass pgRenderer = env->FindClass("de/sopamo/triangula/android/PGRenderer");
    jmethodID ratio_method = env->GetStaticMethodID(pgRenderer,"getRatio","()F");
    jmethodID vector_constructor = env->GetMethodID(vector,"<init>","(FF)V");
   
    jfloat ratio = env->CallStaticFloatMethod(pgRenderer,ratio_method);
    
    jint size = env->GetArrayLength(list);
    jboolean* result;
    jint* bodyIds = env->GetIntArrayElements(list,result);
        
    std::vector<b2Vec2> points;
    std::vector<b2Vec2> points2;
    player.x = x;
    player.y = y;

    RayCaster raycaster;
    raycaster.bodyIds = bodyIds;
    raycaster.size = size;
    
    float viewPortX = player.x+1-ratio*5;
    if(viewPortX < 0)
        viewPortX = 0;
    
    b2Vec2 leftTop,leftBottom,rightTop,rightBottom;
    leftTop.x = viewPortX;
    leftTop.y = 0;
    
    leftBottom.x = viewPortX;
    leftBottom.y = -10;
    
    rightTop.x = 10*ratio+viewPortX;
    rightTop.y = 0;
    
    rightBottom.x = 10*ratio+viewPortX;
    rightBottom.y = -10;
    
    points2.push_back(leftTop);
    points2.push_back(leftBottom);
    points2.push_back(rightTop);
    points2.push_back(rightBottom);
    
    ((UserData*)bodyList[0]->GetUserData())->points = points2;
    
    float rayLength = 10/cos(atan(ratio));

    Query query;
    
    b2AABB aabb;
    aabb.lowerBound = leftBottom;
    aabb.upperBound = rightTop;
    
    world->QueryAABB(&query,aabb);
    query.bodies.push_back(bodyList[0]);
    
    for(int i=0;i<query.bodies.size();i++) {
        b2Body* b = query.bodies[i];
        if(b->GetUserData() == 0) continue;
        UserData* data = (UserData*)b->GetUserData();
        if((data->points).size() == 0) continue;
        std::vector<b2Vec2> edges = data->points;
        for(int j=0;j<edges.size();j++) {
            b2Vec2 edge = edges[j];
            b2Vec2 direction = sub(edge,player);
            direction = mul(div(direction,length(direction)),rayLength);
            b2Vec2 point = add(player,direction);
            
            raycaster.closest = 1;
            world->RayCast(&raycaster,player,point);
            points.push_back(add(player,mul(direction,raycaster.closest)));
        }      
    }
    env->ReleaseIntArrayElements(list,bodyIds,0);
   
    b2Vec2 direction;
    direction.x = 1;
    direction.y = 0;
    
    stdVec = direction;
    std::sort(points.begin(),points.end(),comparator);
    
    jobjectArray ret = env->NewObjectArray(points.size(),vector,0);
    for(int i=0;i<points.size();i++) {
        jobject javaVec = env->NewObject(vector,vector_constructor,(jfloat)points[i].x,(jfloat)points[i].y);
        env->SetObjectArrayElement(ret,i,javaVec);
        env->DeleteLocalRef(javaVec);
    }
    env->DeleteLocalRef(vector);
    env->DeleteLocalRef(pgRenderer);
    return ret;
}

