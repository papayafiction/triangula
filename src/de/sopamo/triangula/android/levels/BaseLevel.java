package de.sopamo.triangula.android.levels;

import android.graphics.Color;
import android.util.Log;
import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;
import de.sopamo.box2dbridge.IBody;
import de.sopamo.box2dbridge.IWorld;
import de.sopamo.triangula.android.geometry.*;
import de.sopamo.triangula.android.levels.backgroundElements.*;
import de.sopamo.triangula.android.levels.backgroundElements.Rectangle;
import de.sopamo.triangula.android.tools.Util;
import org.jbox2d.common.Vec2;

import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BaseLevel {

    protected IBody ground;
    protected IWorld world;
    protected List<GameShape> gsl;
    protected Map jsonData;
    protected ArrayList<BackgroundElement> backgroundElements = new ArrayList<BackgroundElement>();

    public String getLevelString() {
        return "";
    }

    public void drawBackground(GL10 gl) {
        int color = Util.hex2Color((String)getColors().get(2));
        float[] colors = Util.getColorParts(color);
        gl.glClearColor(colors[0], colors[1], colors[2], 1);
    }

    public void drawBackgroundElements(GL10 gl) {
        for(BackgroundElement backgroundElement: backgroundElements) {
            backgroundElement.draw();
            backgroundElement.update();
        }
    }

    public void make(IWorld world,List<GameShape> gsl) {
        makeFence();
        makeBackground();
    }

    protected List getColors() {
        return (List)jsonData.get("colors");
    }

    protected int getTriangleColor() {
        int base;
        if(Math.random() > .5f) {
            base = Util.hex2Color((String)getColors().get(0));
        } else {
            base = Util.hex2Color((String)getColors().get(4));
        }
        float hsv[] = new float[3];
        Color.RGBToHSV(Color.red(base),Color.green(base),Color.blue(base),hsv);
        hsv[1] += Math.random()*0.1-0.05;
        hsv[2] += Math.random()*0.1-0.05;
        int color = Color.HSVToColor(hsv);

        return color;
    }

    protected void makeBackground() {
        Rectangle rect = new Rectangle(1,2,15f);
        rect.setColor(1f,1f,0,0.3f);
        backgroundElements.add(rect);
    }

    protected void makeFence() {
        float density = 0;
        GameShape gs;
        gs = GameShape.create(new GLRectangle(200, .1f));
        gs.setColor(1,1,0,1);
        gs.attachToBody(ground, new Vec2(0, -5f), density);
        gsl.add(gs);

        gs = GameShape.create(new GLRectangle(200, .1f));
        gs.setColor(1,0,0,1);
        gs.attachToBody(ground, new Vec2(0, 5f), density);
        gsl.add(gs);

        gs = GameShape.create(new GLRectangle(.1f, 10f));
        gs.setColor(0,0,1,1);
        gs.attachToBody(ground, new Vec2(100f, 0), density);
        gsl.add(gs);

        gs = GameShape.create(new GLRectangle(.1f, 10f));
        gs.setColor(0,1,0,1);
        gs.attachToBody(ground, new Vec2(-9f, 0), density);
        gsl.add(gs);
    }


    public void parseLevel() {
        JsonParserFactory factory = JsonParserFactory.getInstance();
        JSONParser parser = factory.newJsonParser();
        jsonData = parser.parseJson(getLevelString());
    }

    public void makeTriangles(List triangles) {
        for(int i = 0;i < triangles.size();++i) {
            Map triangle = (Map)triangles.get(i);

            // To get Box2D meters out of pixels we need to divide by 50. The level editor's size is the full size of
            // the triangle whereas our triangles here are twice as large as size.
            // The number 50 comes from onDrawFrame in PGTestRenderer. There we set the cameras z position to -5.
            float size = Float.parseFloat(triangle.get("size").toString()) * 0.02f / 2;
            float x = Float.parseFloat(triangle.get("x").toString()) * 0.02f-9+size;
            float y = Float.parseFloat(triangle.get("y").toString()) * 0.02f-5+size;

            GameShape gs;
            gs = new GameShapeTriangle(new GLTriangle(size));
            int color = getTriangleColor();
            float[] colors = Util.getColorParts(color);
            gs.setColor(colors[0], colors[1], colors[2], 1);
            IBody body = gs.attachToNewBody(world, null, 0);
            body.setPosition(new Vec2(x,y));
            gsl.add(gs);
        }
    }
}
