/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class JNIBox2DWorld */

#ifndef _Included_de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld
#define _Included_de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld
 * Method:    nTestLib
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld_nTestLib
  (JNIEnv *, jobject, jint);

/*
 * Class:     de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld
 * Method:    nCreateWorld
 * Signature: (FFFFFFZ)I
 */
JNIEXPORT jint JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld_nCreateWorld
  (JNIEnv *, jobject, jfloat, jfloat, jfloat, jfloat, jfloat, jfloat, jboolean);

/*
 * Class:     de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld
 * Method:    nStep
 * Signature: (FI)V
 */
JNIEXPORT void JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld_nStep
  (JNIEnv *, jobject, jfloat, jint);

/*
 * Class:     de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld
 * Method:    nDestroy
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld_nDestroy
  (JNIEnv *, jobject);

/*
 * Class:     de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld
 * Method:    nCreateBody
 * Signature: (FF)I
 */
JNIEXPORT jint JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld_nCreateBody
  (JNIEnv *, jobject, jfloat, jfloat);

/*
 * Class:     com_kristianlm_superelevation_box2dbridge_jnibox2d_JNIBox2DWorld
 * Method:    nCreateStaticBody
 * Signature: (FF)I
 */
JNIEXPORT jint JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld_nCreateStaticBody
  (JNIEnv *, jobject, jfloat, jfloat);

/*
 * Class:     de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld
 * Method:    nDestroyBody
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld_nDestroyBody
  (JNIEnv *, jobject, jint);

/*
 * Class:     de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld
 * Method:    nUpdateAllPositions
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld_nUpdateAllPositions
  (JNIEnv *, jobject);

/*
 * Class:     de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld
 * Method:    nSetGravity
 * Signature: (FF)V
 */
JNIEXPORT void JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld_nSetGravity
  (JNIEnv *, jobject, jfloat, jfloat);

JNIEXPORT void JNICALL Java_de_sopamo_box2dbridge_jnibox2d_JNIBox2DWorld_nSetContactListener
(JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
