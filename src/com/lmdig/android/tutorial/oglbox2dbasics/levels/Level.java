package com.lmdig.android.tutorial.oglbox2dbasics.levels;

import com.kristianlm.robotanks.box2dbridge.IBody;
import com.kristianlm.robotanks.box2dbridge.IWorld;
import com.lmdig.android.tutorial.oglbox2dbasics.geometry.GameShape;

import java.util.List;

public interface Level {
    public void make(IWorld world,List<GameShape> gsl);
}
