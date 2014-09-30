package de.sopamo.triangula.android.levels;

import android.graphics.Color;
import android.util.Log;
import de.sopamo.box2dbridge.IBody;
import de.sopamo.box2dbridge.IWorld;
import de.sopamo.triangula.android.PGRenderer;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.mechanics.Entity;
import de.sopamo.triangula.android.game.mechanics.UserData;
import de.sopamo.triangula.android.game.models.*;
import de.sopamo.triangula.android.geometry.*;
import de.sopamo.triangula.android.levels.backgroundElements.BackgroundElement;
import de.sopamo.triangula.android.levels.backgroundElements.Rectangle;
import de.sopamo.triangula.android.tools.Util;
import org.jbox2d.common.Vec2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BaseLevel {

    protected static ArrayList<String> colors = new ArrayList<String>();

    protected IBody ground;
    protected IWorld world;
    protected GameImpl game;
    protected List<GameShape> gsl;
    protected List<Entity> entities;
    protected JSONObject jsonData;
    protected ArrayList<BackgroundElement> backgroundElements = new ArrayList<BackgroundElement>();

    public String getLevelString() {
        return "";
    }

    public void drawBackground(GL10 gl) {
        int color = Util.hex2Color(colors.get(2));
        float[] colors = Util.getColorParts(color);
        gl.glClearColor(colors[0], colors[1], colors[2], 1);
    }

    public void drawBackgroundElements(GL10 gl) {
        for(int i=0;i<backgroundElements.size();i++) {
            BackgroundElement backgroundElement = backgroundElements.get(i);
            backgroundElement.draw();
            backgroundElement.update();
        }
    }

    public void make() {
        this.backgroundElements = new ArrayList<BackgroundElement>();
        this.game = GameImpl.getInstance();
        this.world = game.getWorld();
        this.ground = world.getGroundBody();
        this.entities = game.getEntities();
        this.gsl = game.getGsl();
        parseLevel();

        try {
            makeDoors(jsonData.getJSONArray("doors"));
            makeBombs(jsonData.getJSONArray("bombs"));
            makeSpikes(jsonData.getJSONArray("spikes"));
            makeExits(jsonData.getJSONArray("exits"));
            makeTriangles(jsonData.getJSONArray("triangles"));
        } catch (JSONException e) {
            Log.e("json", "Could not parse level String");
            System.exit(2);
        }

        makeFence();
        makeBackground();

        postMake();
    }

    public void postMake() {}

    public static int getTriangleColor() {
        if(colors == null) return -1;
        int base;
        if(Math.random() > .5f) {
            base = Util.hex2Color(colors.get(0));
        } else {
            base = Util.hex2Color(colors.get(4));
        }
        float hsv[] = new float[3];
        Color.RGBToHSV(Color.red(base),Color.green(base),Color.blue(base),hsv);
        hsv[1] += Math.random()*0.1-0.05;
        hsv[2] += Math.random()*0.1-0.05;
        int color = Color.HSVToColor(hsv);

        return color;
    }

    protected void makeBackground() {
        int backgroundShapes = 100;
        for(int i = 0;i<backgroundShapes;++i) {
            float y = 4 + new Random().nextFloat() * 2;
            if(i%2 == 0)  {
                y *= -1;
            }
            float size = new Random().nextFloat() * 8;
            float speed = new Random().nextFloat() * 0.02f-0.01f;
            Rectangle rect = new Rectangle(i+new Random().nextFloat()*2-5,y,size,speed);
            float grey = new Random().nextFloat();
            rect.setColor(grey,grey,grey,new Random().nextFloat() * 0.3f);
            backgroundElements.add(rect);
        }
    }

    protected void makeFence() {
        float density = 0;
        GameShape gs;


        gs = GameShape.create(new GLRectangle(200, .1f));
        gs.setColor(1,1,0,1);
        gs.attachToBody(ground, new Vec2(100, 0), density);
        gsl.add(gs);

        gs = GameShape.create(new GLRectangle(200, .1f));
        gs.setColor(1,0,0,1);
        gs.attachToBody(ground, new Vec2(100, -10), density);
        gsl.add(gs);

        gs = GameShape.create(new GLRectangle(.1f, 10f));
        gs.setColor(0,0,1,1);
        gs.attachToBody(ground, new Vec2(200f, -5f), density);
        gsl.add(gs);

        gs = GameShape.create(new GLRectangle(.1f, 10f));
        gs.setColor(0,1,0,1);
        gs.attachToBody(ground, new Vec2(0,-5), density);
        gsl.add(gs);
    }


    public void parseLevel() {
        try {
            jsonData = new JSONObject(getLevelString());
            JSONArray jsonColors = jsonData.getJSONArray("colors");
            for(int i = 0;i<5;++i) {
                colors.add(jsonColors.getString(i));
            }
        } catch (JSONException e) {
            Log.e("json","Could not parse level String");
            System.exit(2);
        }
    }

    public void makeTriangles(JSONArray triangles) throws JSONException {
        for(int i = 0;i < triangles.length();++i) {
            JSONObject triangle = triangles.getJSONObject(i);

            // To get Box2D meters out of pixels we need to divide by 50. The level editor's size is the full size of
            // the triangle whereas our triangles here are twice as large as size.
            // The number 50 comes from onDrawFrame in PGRenderer. There we set the cameras z position to -5.
            float size = Float.parseFloat(triangle.getString("size")) * 0.02f / 2;
            float x = Float.parseFloat(triangle.getString("x")) * 0.02f+size;
            float y = Float.parseFloat(triangle.getString("y")) * 0.02f+size;
            float angle = Float.parseFloat(triangle.getString("angle"));
            angle = (float)Math.toRadians(angle);
            y*=-1;
            GameShape gs;
            gs = new GameShapeTriangle(new GLTriangle(size,angle));
            int color = getTriangleColor();
            float[] colors = Util.getColorParts(color);
            gs.setColor(colors[0], colors[1], colors[2], 1);
            IBody body = gs.attachToNewBody(world, null, 0);
            UserData data = new UserData();
            data.type = "triangle";
            data.color = Color.rgb((int)(colors[0]*255),(int)(colors[1]*255),(int)(colors[2]*255));
            body.setUserData(data);
            body.setPosition(new Vec2(x,y));
            gsl.add(gs);
        }
    }

    public void makeSpikes(JSONArray spikes) throws JSONException {
        for(int i=0;i<spikes.length();i++) {
            JSONObject spike = spikes.getJSONObject(i);

            float size = Float.parseFloat(spike.getString("size")) * 0.02f / 2;
            float x = Float.parseFloat(spike.getString("x")) * 0.02f;
            float y = Float.parseFloat(spike.getString("y")) * 0.02f;
            float angle = Float.parseFloat(spike.getString("angle"));
            int count = spike.getInt("count");

            y*=-1;

            new Spikes(count,size,new Vec2(x,y),angle);
        }
    }
    
    public void makeDoors(JSONArray doors) throws JSONException {
        for(int i = 0;i < doors.length();++i) {
            JSONObject door = doors.getJSONObject(i).getJSONObject("door");
            JSONObject sw = doors.getJSONObject(i).getJSONObject("switch");

            // To get Box2D meters out of pixels we need to divide by 50. The level editor's size is the full size of
            // the door whereas our doors here are twice as large as size.
            // The number 50 comes from onDrawFrame in PGRenderer. There we set the cameras z position to -5.
            float size = Float.parseFloat(door.getString("size")) * 0.02f / 2;
            float x = Float.parseFloat(door.getString("x")) * 0.02f+size;
            float y = Float.parseFloat(door.getString("y")) * 0.02f+size;
            float angle = Float.parseFloat(door.getString("angle"));
            y *= -1;

            Door doorModel = new Door(new Vec2(x,y),size,angle);

            x = Float.parseFloat(sw.getString("x")) * 0.02f+.05f;
            y = Float.parseFloat(sw.getString("y")) * 0.02f+0.05f;

            y*=-1;
            new Switch(new Vec2(x,y)).attachToDoor(doorModel);
        }
    }

    public void makeBombs(JSONArray bombs) throws JSONException {
        for(int i = 0;i < bombs.length();++i) {
            JSONObject bomb = bombs.getJSONObject(i);

            // To get Box2D meters out of pixels we need to divide by 50. The level editor's size is the full size of
            // the triangle whereas our triangles here are twice as large as size.
            // The number 50 comes from onDrawFrame in PGRenderer. There we set the cameras z position to -5.
            float x = Float.parseFloat(bomb.getString("x")) * 0.02f+0.04f;
            float y = Float.parseFloat(bomb.getString("y")) * 0.02f+0.04f;
            y*=-1;
            new Bomb(new Vec2(x,y));

        }
    }

    public void makeExits(JSONArray exits) throws JSONException {
        for(int i = 0;i < exits.length();++i) {
            JSONObject exit = exits.getJSONObject(i);

            // To get Box2D meters out of pixels we need to divide by 50.
            // The number 50 comes from onDrawFrame in PGRenderer. There we set the cameras z position to -5.
            float x = Float.parseFloat(exit.getString("x")) * 0.02f+.2f;
            float y = Float.parseFloat(exit.getString("y")) * 0.02f+.8f;

            y*=-1;
            new Exit(new Vec2(x,y));
        }
    }

    public void postDraw() {}
    public void postSurfaceCreated() {}
    public void end() {}

}
