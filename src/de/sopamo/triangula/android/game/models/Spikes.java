package de.sopamo.triangula.android.game.models;

import de.sopamo.box2dbridge.IBody;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.mechanics.Entity;
import de.sopamo.triangula.android.geometry.GLTriangle;
import de.sopamo.triangula.android.geometry.GameShapeTriangle;
import org.jbox2d.common.Vec2;

public class Spikes extends TriangleBaseModel implements Entity {

    public final static int TIME_FOR_DOWN = 500;

    private long timeT1 = 0;
    private long timeT2 = -TIME_FOR_DOWN;
    private long timeT3 = -2*TIME_FOR_DOWN;
    private float y;


    private long lastTick = 0;

    public Spikes(GameImpl game, float x, float y) {
        this.y = y;
        x++;
        for(int i=0;i<3;i++) {
            GameShapeTriangle triangle = new GameShapeTriangle(new GLTriangle(1));
            IBody triangleBody = triangle.attachToNewBody(game.getWorld(),null,0);
            triangleBody.setPosition(new Vec2(x+(2*i),y));
            triangles.add(triangleBody);
            game.getGsl().add(triangle);
        }
        game.getEntities().add(this);
    }

    @Override
    public void update() {
        if(lastTick == 0) {
            lastTick = System.currentTimeMillis();
            return;
        }
        long dt = System.currentTimeMillis()-lastTick;
        timeT1+=dt;
        timeT2+=dt;
        timeT3+=dt;

        if((int)(((float)timeT1)/TIME_FOR_DOWN) % 2 == 0) {
            movingDown(this.triangles.get(0),timeT1);
        } else {
            movingUp(this.triangles.get(0),timeT1);
        }

        if(timeT2 >= 0 && (int)(((float)timeT2)/TIME_FOR_DOWN) % 2 == 0) {
            movingDown(this.triangles.get(1),timeT2);
        } else if(timeT2 >= 0) {
            movingUp(this.triangles.get(1),timeT2);
        }

        if(timeT3 >= 0 && (int)(((float)timeT3)/TIME_FOR_DOWN) % 2 == 0) {
            movingDown(this.triangles.get(2),timeT3);
        } else if(timeT3 >= 0){
            movingUp(this.triangles.get(2),timeT3);
        }

        lastTick = System.currentTimeMillis();
    }

    private void movingUp(IBody triangle,long time) {
        float y = this.y-1;
        float t = time % TIME_FOR_DOWN;
        triangle.setPosition(new Vec2(triangle.getWorldCenter().x,
                y+(t/TIME_FOR_DOWN)));
    }

    private void movingDown(IBody triangle, long time) {
        float y = this.y;
        float t = time % TIME_FOR_DOWN;
        triangle.setPosition(new Vec2(triangle.getWorldCenter().x,
                y-(t/TIME_FOR_DOWN)));
    }

}
