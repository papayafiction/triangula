package de.sopamo.triangula.android.levels;

import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;
import de.sopamo.box2dbridge.IBody;
import de.sopamo.box2dbridge.IWorld;
import de.sopamo.triangula.android.geometry.GLRectangle;
import de.sopamo.triangula.android.geometry.GLTriangle;
import de.sopamo.triangula.android.geometry.GameShape;
import de.sopamo.triangula.android.geometry.GameShapeTriangle;
import org.jbox2d.common.Vec2;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class BaseLevel {

    protected IBody ground;
    protected IWorld world;
    protected List<GameShape> gsl;

    public String getLevelString() {
        return "";
    }

    public void make(IWorld world,List<GameShape> gsl) {
        makeFence();
    }

    protected void makeFence() {
        float density = 0;
        GameShape gs;
        gs = GameShape.create(new GLRectangle(18, .1f));
        gs.setColor(1,1,0,1);
        gs.attachToBody(ground, new Vec2(0, -5f), density);
        gsl.add(gs);

        gs = GameShape.create(new GLRectangle(18, .1f));
        gs.setColor(1,0,0,1);
        gs.attachToBody(ground, new Vec2(0, 5f), density);
        gsl.add(gs);

        gs = GameShape.create(new GLRectangle(.1f, 10f));
        gs.setColor(0,0,1,1);
        gs.attachToBody(ground, new Vec2(9f, 0), density);
        gsl.add(gs);

        gs = GameShape.create(new GLRectangle(.1f, 10f));
        gs.setColor(0,1,0,1);
        gs.attachToBody(ground, new Vec2(-9f, 0), density);
        gsl.add(gs);
    }

    public Map parseLevel() {
        JsonParserFactory factory = JsonParserFactory.getInstance();
        JSONParser parser = factory.newJsonParser();
        Map jsonData = parser.parseJson(getLevelString());
        return jsonData;
    }

    public void makeTriangles(List triangles) {
        for(int i = 0;i < triangles.size();++i) {
            Map triangle = (Map)triangles.get(i);

            float size = Float.parseFloat(triangle.get("size").toString()) * 0.01f;
            float x = Float.parseFloat(triangle.get("x").toString()) * 0.01f;
            float y = Float.parseFloat(triangle.get("y").toString()) * 0.01f;

            GameShape gs;
            gs = new GameShapeTriangle(new GLTriangle(size));
            gs.setColor(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 1);
            IBody body = gs.attachToNewBody(world, null, 0);
            body.setPosition(new Vec2(x,y));
            gsl.add(gs);
        }
    }
}
