package de.sopamo.triangula.android.levels;

import de.sopamo.box2dbridge.IWorld;
import de.sopamo.triangula.android.geometry.GameShape;

import javax.microedition.khronos.opengles.GL10;
import java.util.List;

public interface Level {
    public void drawBackground(GL10 gl);
    public void drawBackgroundElements(GL10 gl);
    public void make(IWorld world,List<GameShape> gsl);
}
