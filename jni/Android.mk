


LOCAL_PATH := $(call my-dir)/../rt-jnibox2d/

include $(CLEAR_VARS)

LOCAL_MODULE := Box2D

SOURCES := \
	./JNIBox2DWorld.cpp \
	./JNIBox2DBody.cpp \
	./JNIBox2DShape.cpp \
	./JNIContactListener.cpp \
	./JNIBox2DRayCast.cpp \
	./JNIRefs.cpp \
	./Dynamics/b2Body.cpp \
	./Dynamics/b2Fixture.cpp \
	./Dynamics/b2Island.cpp \
	./Dynamics/b2World.cpp \
	./Dynamics/b2ContactManager.cpp \
	./Dynamics/Contacts/b2Contact.cpp \
	./Dynamics/Contacts/b2PolygonContact.cpp \
	./Dynamics/Contacts/b2CircleContact.cpp \
	./Dynamics/Contacts/b2ChainAndPolygonContact.cpp \
	./Dynamics/Contacts/b2ChainAndCircleContact.cpp \
	./Dynamics/Contacts/b2PolygonAndCircleContact.cpp \
	./Dynamics/Contacts/b2EdgeAndCircleContact.cpp \
	./Dynamics/Contacts/b2EdgeAndPolygonContact.cpp \
	./Dynamics/Contacts/b2ContactSolver.cpp \
	./Dynamics/b2WorldCallbacks.cpp \
	./Dynamics/Joints/b2MouseJoint.cpp \
	./Dynamics/Joints/b2PulleyJoint.cpp \
	./Dynamics/Joints/b2Joint.cpp \
	./Dynamics/Joints/b2MotorJoint.cpp \
	./Dynamics/Joints/b2RevoluteJoint.cpp \
	./Dynamics/Joints/b2WeldJoint.cpp \
	./Dynamics/Joints/b2WheelJoint.cpp \
	./Dynamics/Joints/b2FrictionJoint.cpp \
	./Dynamics/Joints/b2PrismaticJoint.cpp \
	./Dynamics/Joints/b2DistanceJoint.cpp \
	./Dynamics/Joints/b2GearJoint.cpp \
	./Dynamics/Joints/b2RopeJoint.cpp \
	./Common/b2StackAllocator.cpp \
	./Common/b2Math.cpp \
	./Common/b2Draw.cpp \
	./Common/b2Timer.cpp \
	./Common/b2BlockAllocator.cpp \
	./Common/b2Settings.cpp \
	./Collision/b2Collision.cpp \
	./Collision/b2Distance.cpp \
	./Collision/Shapes/b2CircleShape.cpp \
	./Collision/Shapes/b2PolygonShape.cpp \
	./Collision/Shapes/b2ChainShape.cpp \
	./Collision/Shapes/b2EdgeShape.cpp \
	./Collision/b2TimeOfImpact.cpp \
	./Collision/b2CollidePolygon.cpp \
	./Collision/b2DynamicTree.cpp \
	./Collision/b2CollideCircle.cpp \
	./Collision/b2CollideEdge.cpp \
	./Collision/b2BroadPhase.cpp
LOCAL_SRC_FILES := $(addprefix Box2D/,$(SOURCES))

#LOCAL_LDLIBS := -lGLESv1_CM -ldl -llog

include $(BUILD_SHARED_LIBRARY)


