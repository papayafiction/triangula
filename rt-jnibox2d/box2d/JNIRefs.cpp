

#include <jni.h>
#include "JNIBox2DWorld.h"
#include "JNIRefs.h"




jobject globalRef [MAX_GLOBAL_REFS];




jobject MakeGlobalRef(JNIEnv* env, jobject obj) {

	// loop through global refs and see if this one is already there
	for(int i = 0 ; i < MAX_GLOBAL_REFS ; i++)
	{
		if(globalRef[i] == obj) {
			printf("re-using global ref %d", (int)obj);
			return obj;
		}
	}

	// loop through global refs and find unused pos (NULL)
	for(int i = 0 ; i < MAX_GLOBAL_REFS ; i++)
	{
		if(globalRef[i] == 0) {
//			printf("creating java global reference %d @ %d \n", obj, i);

			jobject jglobal = env->NewGlobalRef(obj);

			// add global ref to reflist
			globalRef[i] = jglobal;

			return jglobal;
		}
	}

	char txt[100];
	sprintf(txt, "No free spot for global ref %d found. Max refs: %d", (int)obj, MAX_GLOBAL_REFS);
	throwExc(env, txt);
	return 0;


}

void DeleteAllGlobalRefs(JNIEnv* env) {
	// delete global refs
	for(int i = 0 ; i < MAX_GLOBAL_REFS ; i++) {
		if(globalRef[i] != 0) {
//			printf("deleting Java global ref %d from pos #%d\n", globalRef[i], i);
			env->DeleteGlobalRef(globalRef[i]);
			globalRef[i] = 0;
		}
	}
}

void DeleteGlobalRef(JNIEnv* env, jobject obj) {
	for(int i = 0 ; i < MAX_GLOBAL_REFS ; i++) {
		if(globalRef[i] == obj) {
//			printf("\tdeleting Java global ref %d from pos #%d\n", globalRef[i], i);
			env->DeleteGlobalRef(globalRef[i]);

			globalRef[i] = 0;
		}
	}
}

