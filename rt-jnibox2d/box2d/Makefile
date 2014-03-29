TARGETS= Gen/pc/libbox2d.so # coultn't get this to work: Gen/android/libbox2d.so #Gen/fixed/libbox2d.a

#android arm compiler

#ARM-GCC=/opt/android-ndk-1.6_r1/build/prebuilt/linux-x86/arm-eabi-4.2.1/bin/arm-eabi-gcc
#ARM-GPP=/opt/android-ndk-1.6_r1/build/prebuilt/linux-x86/arm-eabi-4.2.1/bin/arm-eabi-c++

# SET DIRECTORY FOR SE-CORE PROJECT
COREDIR=/home/klm/workspace/se-core

CXXFLAGS=	-g -O2 -I /usr/lib/jvm/java-6-sun/include/ \
		-I /usr/lib/jvm/java-6-sun/include/linux  \

#-I /opt/android-ndk-1.6_r1/build/platforms/android-4/arch-arm/usr/include
#

SOURCES = \
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

ifneq ($(INCLUDE_DEPENDENCIES),yes)

all:	
	@make --no-print-directory INCLUDE_DEPENDENCIES=yes $(TARGETS)

.PHONY:	clean
clean:
	rm -rf Gen

else

# include all sources' dependency files
-include $(addprefix Gen/pc/,$(SOURCES:.cpp=.d))


endif


Headers/gen/com_kristianlm_robotanks_box2dbridge_jnibox2d_JNIBox2DShape.h : $(COREDIR)/src/com/kristianlm/robotanks/box2dbridge/jnibox2d/JNIBox2DShape.java
	javah -d Headers/gen/ -jni  -classpath $(COREDIR)/bin/ $(subst Headers/gen/,,$(subst .h,,$(subst _,.,$@)))
	touch $@

Headers/gen/com_kristianlm_robotanks_box2dbridge_jnibox2d_JNIBox2DBody.h : $(COREDIR)/src/com/kristianlm/robotanks/box2dbridge/jnibox2d/JNIBox2DBody.java
	javah -d Headers/gen/ -jni  -classpath $(COREDIR)/bin/ $(subst Headers/gen/,,$(subst .h,,$(subst _,.,$@)))
	touch $@

Headers/gen/com_kristianlm_robotanks_box2dbridge_jnibox2d_JNIBox2DWorld.h : $(COREDIR)/src/com/kristianlm/robotanks/box2dbridge/jnibox2d/JNIBox2DWorld.java
	javah -d Headers/gen/ -jni  -classpath $(COREDIR)/bin/ $(subst Headers/gen/,,$(subst .h,,$(subst _,.,$@)))
	touch $@


Gen/pc/%.o:		%.cpp 
	@mkdir -p $(dir $@)
	c++ $(CXXFLAGS) -c -o $@ $<


Gen/pc/libbox2d.so:	$(addprefix Gen/pc/,$(SOURCES:.cpp=.o)) 
	gcc -o $@ -shared \
		-lstdc++ \
		$^
	@#ar cr $@ $^
	@#ranlib $@ 

# create dependency files
Gen/pc/%.d:		%.cpp
	@mkdir -p $(dir $@)
	c++ -MM -MT $(@:.d=.o) $(CXXFLAGS) -o $@ $<

#Headers/gen/%.h: #
#echo $^ 
#




#---------------------------

Gen/android/libbox2d.so: $(addprefix Gen/android/,$(SOURCES:.cpp=.o)) 
	$(ARM-GCC) -o $@ -shared \
		$^

Gen/android/%.o:		%.cpp 
	@mkdir -p $(dir $@)
	$(ARM-GCC) $(CXXFLAGS) -c -o $@ $<


Gen/android/%.d:		%.cpp
	@mkdir -p $(dir $@)
	$(ARM-GPP) -MM -MT $(@:.d=.o) $(CXXFLAGS) -o $@ $<

# ---------------

Gen/float/%.o:		%.cpp
	@mkdir -p $(dir $@)
	c++ $(CXXFLAGS) -c -o $@ $<


Gen/float/libbox2d.a:	$(addprefix Gen/float/,$(SOURCES:.cpp=.o))
	ar cr $@ $^
	ranlib $@ 

Gen/float/%.d:		%.cpp
	@mkdir -p $(dir $@)
	c++ -MM -MT $(@:.d=.o) $(CXXFLAGS) -o $@ $<




NDS_COMPILE_FLAGS= -g -O2 -fomit-frame-pointer -ffast-math \
		-march=armv5te -mtune=arm946e-s -mthumb-interwork \
		-DARM9 -fno-rtti -fno-exceptions -c \
		-DTARGET_IS_NDS \
		-I$(DEVKITPRO)/libnds/include 






Gen/nds-float/%.o:		%.cpp
	@mkdir -p $(dir $@)
	arm-eabi-g++ $(NDS_COMPILE_FLAGS) -o $@ $<

Gen/nds-float/lib/libbox2d.a:	$(addprefix Gen/nds-float/,$(SOURCES:.cpp=.o))
	@mkdir -p $(dir $@)
	arm-eabi-ar cr $@ $^
	arm-eabi-ranlib $@ 


Gen/nds-float/%.d:		%.cpp
	@mkdir -p $(dir $@) 
	arm-eabi-g++ -MM -MT $(@:.d=.o) $(NDS_COMPILE_FLAGS) -o $@ $<



