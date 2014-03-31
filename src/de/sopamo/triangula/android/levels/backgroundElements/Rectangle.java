package de.sopamo.triangula.android.levels.backgroundElements;

import android.util.Log;
import de.sopamo.triangula.android.geometry.GLRectangle;

import static android.opengl.GLES10.*;

public class Rectangle extends GLRectangle implements BackgroundElement {
    private float red = 0;
    private float green = 0;
    private float blue = 0;
    private float alpha = 1;

    private float x;
    private float y;
    private float angle;
    private float size;


    public Rectangle(float x, float y, float size) {
        super(size,size);
        this.size = size;
        this.x = x;
        this.y = y;
        this.angle = 0;
    }

    public void draw() {
        glColor4f(red, green, blue, alpha);
        super.draw(x,y,angle);
    }

    public void update() {
        angle += .001f;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setColor(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public void setColor(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
}
