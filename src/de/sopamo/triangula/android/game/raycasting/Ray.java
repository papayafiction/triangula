package de.sopamo.triangula.android.game.raycasting;

import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.geometry.GLInterface;
import de.sopamo.triangula.android.geometry.GLLine;
import de.sopamo.triangula.android.geometry.GLRectangle;
import org.jbox2d.common.Vec2;

import static android.opengl.GLES10.glColor4f;

public class Ray extends Line {
    GLLine glLine;

    public Ray(Vec2 start, Vec2 end) {
        super(start,end);
        glLine = new GLLine(start,end);
    }

    public void draw() {
        glColor4f(0, 1, 0, 1);
        glLine.draw();
    }

    public void setEnd(Vec2 end) {
        this.end = end;
    }


}
