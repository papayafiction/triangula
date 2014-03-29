


LOCAL_PATH := $(call my-dir)/../rt-jnibox2d/

include $(CLEAR_VARS)

LOCAL_MODULE := box2d

SOURCES := \
	./JNIBox2DWorld.cpp \
	./JNIBox2DBody.cpp \
	./JNIBox2DShape.cpp \
	./JNIRefs.cpp \
	./Dynamics/b2Body.cpp \
	./Dynamics/b2Island.cpp \
	./Dynamics/b2World.cpp \
	./Dynamics/b2ContactManager.cpp \
	./Dynamics/Contacts/b2Contact.cpp \
	./Dynamics/Contacts/b2PolyContact.cpp \
	./Dynamics/Contacts/b2CircleContact.cpp \
	./Dynamics/Contacts/b2PolyAndCircleContact.cpp \
	./Dynamics/Contacts/b2EdgeAndCircleContact.cpp \
	./Dynamics/Contacts/b2PolyAndEdgeContact.cpp \
	./Dynamics/Contacts/b2ContactSolver.cpp \
	./Dynamics/b2WorldCallbacks.cpp \
	./Dynamics/Joints/b2MouseJoint.cpp \
	./Dynamics/Joints/b2PulleyJoint.cpp \
	./Dynamics/Joints/b2Joint.cpp \
	./Dynamics/Joints/b2RevoluteJoint.cpp \
	./Dynamics/Joints/b2PrismaticJoint.cpp \
	./Dynamics/Joints/b2DistanceJoint.cpp \
	./Dynamics/Joints/b2GearJoint.cpp \
	./Dynamics/Joints/b2LineJoint.cpp \
	./Dynamics/Controllers/b2Controller.cpp \
	./Dynamics/Controllers/b2BuoyancyController.cpp \
	./Dynamics/Controllers/b2GravityController.cpp \
	./Dynamics/Controllers/b2TensorDampingController.cpp \
	./Dynamics/Controllers/b2ConstantForceController.cpp \
	./Dynamics/Controllers/b2ConstantAccelController.cpp \
	./Common/b2StackAllocator.cpp \
	./Common/b2Math.cpp \
	./Common/b2BlockAllocator.cpp \
	./Common/b2Settings.cpp \
	./Collision/b2Collision.cpp \
	./Collision/b2Distance.cpp \
	./Collision/Shapes/b2Shape.cpp \
	./Collision/Shapes/b2CircleShape.cpp \
	./Collision/Shapes/b2PolygonShape.cpp \
	./Collision/Shapes/b2EdgeShape.cpp \
	./Collision/b2TimeOfImpact.cpp \
	./Collision/b2PairManager.cpp \
	./Collision/b2CollidePoly.cpp \
	./Collision/b2CollideCircle.cpp \
	./Collision/b2BroadPhase.cpp 
LOCAL_SRC_FILES := $(addprefix box2d/,$(SOURCES))

    
#LOCAL_LDLIBS := -lGLESv1_CM -ldl -llog

include $(BUILD_SHARED_LIBRARY)


