package de.sopamo.triangula.android.levels;

import android.util.Log;
import de.sopamo.box2dbridge.IBody;
import de.sopamo.box2dbridge.IWorld;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.mechanics.Entity;
import de.sopamo.triangula.android.game.models.*;
import de.sopamo.triangula.android.geometry.GLRectangle;
import de.sopamo.triangula.android.geometry.GameShape;
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

public abstract class BaseLevel {

    protected ArrayList<String> colors = new ArrayList<String>();

    protected String creatorTag;
    protected String levelName;
    protected String levelUrl;
    protected boolean isOnlineLevel;
    protected IBody ground;
    protected List<String> achievments = new ArrayList<String>();
    protected IWorld world;
    protected GameImpl game;
    protected List<GameShape> gsl;
    protected List<Entity> entities;
    protected JSONObject jsonData;
    protected ArrayList<BackgroundElement> backgroundElements = new ArrayList<BackgroundElement>();
    protected String levelString;

    public BaseLevel () {

    }


    public String getLevelString() {
        return levelString;
    }
    public void setLevelString(String jsonString) {
        levelString=jsonString;
    }

    public void drawBackground(GL10 gl) {
        int color = Util.hex2Color(colors.get(4));
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
            makeColors(jsonData.getJSONArray("colors"));
            makeDoors(jsonData.getJSONArray("doors"));
            makeBombs(jsonData.getJSONArray("bombs"));
            makeSpikes(jsonData.getJSONArray("spikes"));
            makeExits(jsonData.getJSONArray("exits"));
            makeTriangles(jsonData.getJSONArray("triangles"));
        } catch (JSONException e) {
            Log.e("json", "Could not parse level String");
            System.exit(2);
        }

        try {
            makeBubbles(jsonData.getJSONArray("bubbles"));
        } catch (JSONException e){
            Log.e("json","Failure in parsing bubbles");
        }

        makeFence();
        makeBackground();

        postMake();
    }

    public void postMake() {}

    /**
     * Gets a random color for a triangle / door / spike
     *
     * @return The color
     */
    public int getTriangleColor() {
        if(colors == null) return -1;

        // Get the first or second color
        int randomColor = new Random().nextInt(2);
        // Get a color int from the hex color
        int base = Util.hex2Color(colors.get(randomColor));

        // Return a slightly modified color
        return Util.getSimilarColor(base);
    }

    /**
     * Gets a random color for a bubble
     *
     * @return The color
     */
    public int getBubbleColor() {
        if(colors == null) return -1;

        // Get the third or fourth color
        int randomColor = new Random().nextInt(2)+2;
        // Get a color int from the hex color
        int base = Util.hex2Color(colors.get(randomColor));

        // Return a slightly modified color
        return Util.getSimilarColor(base);
    }

    /**
     * Gets a random color for the background elements
     *
     * @return The color
     */
    public int getBackgroundColor() {
        if(colors == null) return -1;

        // Get a color int from the hex color
        int base = Util.hex2Color(colors.get(4));

        // Return a slightly modified color
        return Util.getSimilarColor(base,0.05f);
    }


    /**
     * Creates the background shapes
     */
    protected void makeBackground() {
        int backgroundShapes = (int)(new Random().nextFloat()*50)+70;
        for (int i = 0; i < backgroundShapes; ++i) {
            Random random = new Random();

            // Get random values
            float y = 4 + random.nextFloat() * 2;
            float x = i + random.nextFloat() * 2 - 5;
            float size = random.nextFloat() * 8;
            float speed = random.nextFloat() * 0.02f - 0.01f;

            // Every second element is placed at the bottom
            if (i % 2 == 0) {
                y *= -1;
            }

            // Create the rectangle
            Rectangle rect = new Rectangle(x, y, size, speed);

            // Set the color
            float[] colors = Util.getColorParts(getBackgroundColor());
            rect.setColor(colors[0], colors[1], colors[2], 1);

            // Add the element
            backgroundElements.add(rect);
        }
    }

    protected void makeFence() {
        float density = 0;
        GameShape gs;

        // Top
        gs = GameShape.create(new GLRectangle(200, .1f));
        gs.setColor(1,1,0,1);
        gs.attachToBody(ground, new Vec2(100, .05f), density);
        gsl.add(gs);

        // Bottom
        gs = GameShape.create(new GLRectangle(200, .1f));
        gs.setColor(1,0,0,1);
        gs.attachToBody(ground, new Vec2(100, -10.05f), density);
        gsl.add(gs);

        // Right
        gs = GameShape.create(new GLRectangle(.1f, 10f));
        gs.setColor(0,0,1,1);
        gs.attachToBody(ground, new Vec2(200f, -5f), density);
        gsl.add(gs);

        // Left
        gs = GameShape.create(new GLRectangle(.1f, 10f));
        gs.setColor(0,1,0,1);
        gs.attachToBody(ground, new Vec2(-0.05f,-5), density);
        gsl.add(gs);
    }


    public void parseLevel() {
        try {
            jsonData = new JSONObject(getLevelString());
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
            new Triangle(new Vec2(x,y),size,angle, getTriangleColor());
        }
    }

    public void makeBubbles(JSONArray bubbles) throws JSONException {
        for(int i = 0;i < bubbles.length();++i) {
            JSONObject bubble = bubbles.getJSONObject(i);

            float radius = Float.parseFloat(bubble.getString("size")) * 0.02f / 2;
            float x = Float.parseFloat(bubble.getString("x")) * 0.02f+radius;
            float y = Float.parseFloat(bubble.getString("y")) * 0.02f+radius;
            y*=-1;
            new Bubble(new Vec2(x,y),radius, getBubbleColor());
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

            new Spikes(count,size,new Vec2(x,y),angle, getTriangleColor());
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

            Door doorModel = new Door(new Vec2(x,y),size,angle, getTriangleColor());

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

    public void makeColors(JSONArray colors) throws  JSONException {
        for(int i = 0;i<5;++i) {
            this.colors.add(colors.getString(i));
        }
    }

    public List<String> getAchievements() {
        return achievments;
    }

    public void postDraw() {}
    public void postSurfaceCreated() {}
    public void end() {}



    public String getLevelName() { return levelName; }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public boolean isOnlineLevel() {
        return isOnlineLevel;
    }

    public void setIsOnlineLevel(boolean isOnlineLevel) {
        this.isOnlineLevel = isOnlineLevel;
    }

    public String getCreatorTag() {
        return creatorTag;
    }

    public void setCreatorTag(String creatorTag) {
        this.creatorTag = creatorTag;
    }

    public String getLevelUrl() {
        return levelUrl;
    }

    public void setLevelUrl(String levelUrl) {
        this.levelUrl = levelUrl;
    }
}
