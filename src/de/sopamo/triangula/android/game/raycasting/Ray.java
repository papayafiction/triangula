package de.sopamo.triangula.android.game.raycasting;

import de.sopamo.triangula.android.geometry.GLLine;
import org.jbox2d.common.Vec2;

import static android.opengl.GLES10.glColor4f;

public class Ray extends Line {
    GLLine glLine;
    public static int color = 0;

    public Ray(Vec2 start, Vec2 end) {
        super(start,end);
        glLine = new GLLine(start,end);
    }

    public void draw() {
        glColor4f(color/255f, color/255f, color/255f, 1);
        color++;
        glLine.draw();
    }

    public void setEnd(Vec2 end) {
        this.end = end;
    }


}
