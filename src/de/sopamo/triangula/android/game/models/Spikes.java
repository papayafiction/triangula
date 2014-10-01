package de.sopamo.triangula.android.game.models;

import android.graphics.Color;
import de.sopamo.box2dbridge.IBody;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.mechanics.Entity;
import de.sopamo.triangula.android.game.mechanics.Rewindable;
import de.sopamo.triangula.android.game.mechanics.UserData;
import de.sopamo.triangula.android.geometry.GLTriangle;
import de.sopamo.triangula.android.geometry.GameShapeTriangle;
import de.sopamo.triangula.android.levels.BaseLevel;
import de.sopamo.triangula.android.tools.Util;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;
import java.util.List;

public class Spikes extends TriangleBaseModel implements Entity,Rewindable {

    public final static int TIME_FOR_DOWN = 500;

    private List<Long> times = new ArrayList<Long>();
    private Vec2 direction;
    private Vec2 pstVec;
    private Vec2 pst;
    private boolean isRewinding;

    public Spikes(int count, float size, Vec2 pst,float angle) {
        GameImpl game = GameImpl.getInstance();
        float radian = (float)Math.toRadians(angle);
        this.pst = pst.add(new Vec2((float)(Math.sqrt(2)*size*Math.cos(radian-Math.PI/4)),
                                    (float)(Math.sqrt(2)*size*Math.sin(radian-Math.PI/4))));
        pstVec = new Vec2((float)(size*Math.cos(radian)),
                          (float)(size*Math.sin(radian)));

        direction = new Vec2((float)(2*Math.sin(radian)),
                             (float)(-2*Math.cos(radian)));

        for(int i=0;i<count;i++) {
            GameShapeTriangle triangle = new GameShapeTriangle(new GLTriangle(size,radian));
            float[] colors = Util.getColorParts(BaseLevel.getTriangleColor());
            triangle.setColor(colors[0], colors[1], colors[2], 1);
            IBody triangleBody = triangle.attachToNewBody(game.getWorld(),null,0);
            triangleBody.setPosition(this.pst.add(pstVec.mul(2*i)));
            triangleBody.setAngle(radian);
            UserData data = new UserData();
            data.type = "spike";
            data.color = Color.rgb((int) (colors[0] * 255), (int) (colors[1] * 255), (int) (colors[2] * 255));
            triangleBody.setUserData(data);
            game.getGsl().add(triangle);
            times.add(i%2==0?0l:-TIME_FOR_DOWN);
            triangles.add(triangleBody);
        }

        game.getEntities().add(this);
        game.getRewindables().add(this);
    }

    @Override
    public void update() {
        Long dt = 1000l/60;
        for(int i=0;i<times.size();i++) {
            IBody triangle = triangles.get(i);
            Long time = times.get(i);
            if (isRewinding()) time += 2*dt;
            else time += dt;
            times.set(i,time);
            if(time >= 0 && (int)(((float)time)/TIME_FOR_DOWN) % 2 == 0) {
                movingDown(triangle,i,time);
            } else if(time >= 0) {
                movingUp(triangle,i,time);
            }
        }
    }

    private void movingUp(IBody triangle,int i, long time) {
        Vec2 pst = this.pst.add(pstVec.mul(2*i));
        triangle.setPosition(pst.add(direction.mul(1.0f-(float)(time%TIME_FOR_DOWN)/TIME_FOR_DOWN)));
    }

    private void movingDown(IBody triangle,int i, long time) {
        Vec2 pst = this.pst.add(pstVec.mul(2 * i));
        triangle.setPosition(pst.add(direction.mul((float)(time%TIME_FOR_DOWN)/TIME_FOR_DOWN)));
    }

    @Override
    public void startRewind() {
        this.isRewinding = true;
        negateDirection();
    }

    @Override
    public void stopRewind() {
        this.isRewinding = false;
        negateDirection();
    }

    private void negateDirection() {
        for(int i = 0;i< times.size(); i++) {
            Long time = times.get(i);
            long timeDiff = TIME_FOR_DOWN-(time%TIME_FOR_DOWN);
            time += 2*timeDiff;
            times.set(i,time);
        }
    }

    @Override
    public void run() {

    }

    @Override
    public boolean isRewinding() {
        return isRewinding;
    }
}
