package de.sopamo.box2dbridge.jnibox2d;

import de.sopamo.box2dbridge.IRayCastOutput;
import org.jbox2d.common.Vec2;

/**
 * Created by stevijo on 30.09.14.
 */
public class
        JNIRayCastOutput implements IRayCastOutput {

    public Vec2 normal;
    public float fraction;


    @Override
    public Vec2 getNormal() {
        return normal;
    }

    @Override
    public float getFraction() {
        return fraction;
    }
}
