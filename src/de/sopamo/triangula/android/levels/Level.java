package de.sopamo.triangula.android.levels;

import de.sopamo.box2dbridge.IWorld;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.mechanics.Entity;
import de.sopamo.triangula.android.geometry.GameShape;
import org.json.JSONException;

import javax.microedition.khronos.opengles.GL10;
import java.io.Serializable;
import java.util.List;

public interface Level {
    public void drawBackground(GL10 gl);
    public void drawBackgroundElements(GL10 gl);
    public void make();
    public String getLevelString();
}
