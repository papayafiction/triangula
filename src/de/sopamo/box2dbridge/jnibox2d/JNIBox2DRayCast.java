package de.sopamo.box2dbridge.jnibox2d;

import de.sopamo.box2dbridge.IBody;
import org.jbox2d.common.Vec2;

import java.util.List;

public class JNIBox2DRayCast {

    public static Vec2[] rayCast(Vec2 pst,List<IBody> noRayCastBodies) {
        int[] bod = new int[noRayCastBodies.size()];
        for(int i = 0;i<noRayCastBodies.size();i++) {
            bod[i] = ((JNIBox2DBody)noRayCastBodies.get(i)).bodyID;
        }
        return nRayCast(pst.x,pst.y,bod);
    }

    private static boolean jniOk = false;
    public static boolean isJniOK() { return jniOk; }
    static {
        try  {
            System.loadLibrary("Box2D");
            jniOk = true;
        } catch (java.lang.UnsatisfiedLinkError e) {
            jniOk = false;
        }
    }

    native static private Vec2[] nRayCast(float x, float y,int[] bodyList);
}
