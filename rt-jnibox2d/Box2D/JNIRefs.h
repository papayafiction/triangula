/*
 * JNIReafs.h
 *
 *  Created on: Nov 24, 2009
 *      Author: klm
 */

#ifndef JNIREAFS_H_
#define JNIREAFS_H_


#define MAX_GLOBAL_REFS 1000
extern jobject globalRef [MAX_GLOBAL_REFS];



jobject MakeGlobalRef(JNIEnv* env, jobject obj);
void DeleteGlobalRef(JNIEnv* env, jobject obj);
void DeleteAllGlobalRefs(JNIEnv* env);


#endif /* JNIREAFS_H_ */
