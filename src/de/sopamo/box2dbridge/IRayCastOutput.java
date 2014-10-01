package de.sopamo.box2dbridge;

import org.jbox2d.common.Vec2;

/**
 * Created by stevijo on 30.09.14.
 */
public interface IRayCastOutput {
    public Vec2 getNormal();
    public float getFraction();
}
