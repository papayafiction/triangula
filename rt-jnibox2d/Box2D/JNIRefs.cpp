

#include <jni.h>
#include "JNIBox2DWorld.h"
#include "JNIRefs.h"




jobject globalRef [MAX_GLOBAL_REFS];

jobject MakeGlobalRef(JNIEnv* env, jobject obj) {
    for(int i = 0 ; i < MAX_GLOBAL_REFS ; i++)
	{
		if(globalRef[i] == obj) {
			printf("re-using global ref %d", (int)obj);
			return obj;
		} else if(globalRef[i] == 0) {
			jobject jglobal = env->NewGlobalRef(obj);

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
	for(int i = 0 ; i < MAX_GLOBAL_REFS ; i++) {
		if(globalRef[i] != 0) {
			env->DeleteGlobalRef(globalRef[i]);
			globalRef[i] = 0;
		}
	}
}

void DeleteGlobalRef(JNIEnv* env, jobject obj) {
	for(int i = 0 ; i < MAX_GLOBAL_REFS ; i++) {
		if(globalRef[i] == obj) {
			env->DeleteGlobalRef(globalRef[i]);

			globalRef[i] = 0;
			return;
		}
	}
}

