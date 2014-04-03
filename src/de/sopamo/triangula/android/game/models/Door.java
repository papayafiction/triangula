package de.sopamo.triangula.android.game.models;

import de.sopamo.box2dbridge.IBody;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.mechanics.Entity;
import de.sopamo.triangula.android.geometry.GLTriangle;
import de.sopamo.triangula.android.geometry.GameShapeTriangle;
import de.sopamo.triangula.android.levels.BaseLevel;
import de.sopamo.triangula.android.tools.Util;
import org.jbox2d.common.Vec2;

public class Door extends TriangleBaseModel implements Entity {
    public final static int TIME_TO_OPEN = 2000;

    private boolean opening;
    private boolean opened;

    private IBody triangle;

    private long time=0;
    private Vec2 destination;
    private Vec2 pst;
    private float[] colors;

    public Door(GameImpl game,Vec2 pst,float size,float angle) {
        float radian = (float)Math.toRadians(angle);
        GameShapeTriangle triangle = new GameShapeTriangle(new GLTriangle(size,radian));
        colors = Util.getColorParts(BaseLevel.getTriangleColor());
        triangle.setColor(colors[0], colors[1], colors[2], 1);
        IBody triangleBody = triangle.attachToNewBody(game.getWorld(),null,0);
        triangleBody.setAngle(radian);
        triangleBody.setPosition(pst);
        this.triangle = triangleBody;
        this.pst = pst;
        this.destination = new Vec2(
                (float)(2*size*Math.sin(-radian)),
                (float)(2*size*Math.cos(-radian))
        );
        game.getEntities().add(this);
        game.getGsl().add(triangle);
    }

    public float[] getColors() {
        return colors;
    }

    @Override
    public void update() {
        if(opened) return;
        if(!opening) return;
        if(time >= TIME_TO_OPEN) {
            opened = true;
            opening = false;
            return;
        }
        time+=1000l/60;
        triangle.setPosition(pst.add(destination.mul(((float)time)/TIME_TO_OPEN)));
    }

    public void open() {
        opening = true;
    }
}
