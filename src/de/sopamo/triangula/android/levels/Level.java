package de.sopamo.triangula.android.levels;

import de.sopamo.box2dbridge.IWorld;
import de.sopamo.triangula.android.geometry.GameShape;

import java.util.List;

public interface Level {
    public void make(IWorld world,List<GameShape> gsl);
}
