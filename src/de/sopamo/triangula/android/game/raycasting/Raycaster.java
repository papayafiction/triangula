package de.sopamo.triangula.android.game.raycasting;

import de.sopamo.box2dbridge.IBody;
import de.sopamo.box2dbridge.IRayCastOutput;
import de.sopamo.box2dbridge.jnibox2d.JNIBox2DBody;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.tools.BufferTool;
import de.sopamo.triangula.android.tools.GLBufferTool;
import org.jbox2d.common.Vec2;

import static android.opengl.GLES10.*;
import static android.opengl.GLES10.GL_TRIANGLE_STRIP;
import static android.opengl.GLES10.glPopMatrix;

public class Raycaster {

    private static Vec2[] points;

    public static void cast() {
        points = new Vec2[200];
        GameImpl.getInstance().removeRays();
        Vec2 player = GameImpl.getMainPlayer().getBody().getWorldCenter();
        IBody body = new JNIBox2DBody(Integer.MAX_VALUE);

        for(int i = 0;i<200;i++) {
            float rayX = 10 * (float)Math.cos(2 * i * (Math.PI / 200));
            float rayY = 10 * (float)Math.sin(2*i*(Math.PI/200));
            Vec2 end = new Vec2(player.x + rayX, rayY + player.y);
            IRayCastOutput res = body.rayCast(player,end,300);
            Float dist = res.getFraction();
            if(dist != null) {
                Vec2 lengthened = end.sub(player).mul(dist);
                points[i] = lengthened;
            }
        }

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
        Vec2 pst =
        GameImpl.getMainPlayer().getBody().getWorldCenter();
        glTranslatef(pst.x,pst.y, 0);
        float[] v = new float[points.length*2];

        int i = 0;
        for(Vec2 point:points) {
            v[i] = point.x;
            i++;
            v[i] = point.y;
            i++;
        }

        GLBufferTool.setGLVertexBuffer(2, BufferTool.makeFloatBuffer(v));
        glDrawArrays(GL_TRIANGLE_FAN,
                0,
                points.length);

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
