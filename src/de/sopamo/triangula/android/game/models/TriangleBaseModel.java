package de.sopamo.triangula.android.game.models;

import de.sopamo.box2dbridge.IBody;
import de.sopamo.triangula.android.geometry.GameShapeTriangle;

import java.util.ArrayList;
import java.util.List;

abstract public class TriangleBaseModel {
    protected List<IBody> triangles = new ArrayList<IBody>();
}
