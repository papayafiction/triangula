package de.sopamo.triangula.android.game.raycasting;

import de.sopamo.box2dbridge.jnibox2d.JNIBox2DRayCast;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.tools.BufferTool;
import de.sopamo.triangula.android.tools.GLBufferTool;
import org.jbox2d.common.Vec2;

import static android.opengl.GLES10.*;

public class Raycaster {

    private static Vec2[] points;

    public static void cast() {
        GameImpl.getInstance().removeRays();
        Vec2 player = GameImpl.getMainPlayer().getBody().getWorldCenter();

        points = JNIBox2DRayCast.rayCast(player,GameImpl.getInstance().getNoRayCast());
    }

    public static void draw() {
        glEnable(GL_STENCIL_TEST);
        glClearStencil(0);
        glClear(GL_STENCIL_BUFFER_BIT);
        glColorMask(false, false, false, false);
        glDepthMask(false);
        glStencilFunc(GL_ALWAYS, 1, 1);
        glStencilOp(GL_REPLACE, GL_REPLACE, GL_REPLACE);


        // Draw stencil
        glPushMatrix();
        float[] v = new float[points.length*2+4];
        Vec2 pst = GameImpl.getMainPlayer().getBody().getWorldCenter();

        v[0] = pst.x;
        v[1] = pst.y;
        int i = 2;
        for(Vec2 point:points) {
            v[i++] = point.x;
            v[i++] = point.y;
        }
        v[i++] = points[0].x;
        v[i] = points[0].y;

        GLBufferTool.setGLVertexBuffer(2, BufferTool.makeFloatBuffer(v));
        glDrawArrays(GL_TRIANGLE_FAN,
                0,
                points.length+2);

        glPopMatrix();


        glColorMask(true, true, true, true);
        glDepthMask(true);
        glStencilFunc(GL_EQUAL, 0, 1);
        glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);


        // Draw shadow
        glColor4f(0,0,0,.7f);
        float[] v2 = {
                0,  0,
                0,  -15,
                25, -15,
                25, 0,
                0,0
        };

        GLBufferTool.setGLVertexBuffer(2, BufferTool.makeFloatBuffer(v2));
        glDrawArrays(GL_TRIANGLE_STRIP, 0,
               5);


        glDisable(GL_STENCIL_TEST);
    }
}
