package de.sopamo.triangula.android.levels;

import de.sopamo.box2dbridge.IWorld;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.mechanics.Entity;
import de.sopamo.triangula.android.geometry.GameShape;
import org.json.JSONException;

import javax.microedition.khronos.opengles.GL10;
import java.io.Serializable;
import java.util.List;

public interface Level extends Serializable {
    public void drawBackground(GL10 gl);
    public void drawBackgroundElements(GL10 gl);
    public void make();
    public void end();
    public String getLevelString();
    public void setLevelString(String json);
    public List<String> getAchievements();
    public void postDraw();
    public void postSurfaceCreated();
    public String getCreatorTag();
    public void setCreatorTag(String creatorTag);
    public String getLevelName();
    public void setLevelName(String levelName);
    public boolean isOnlineLevel();
    public void setIsOnlineLevel(boolean isOnlineLevel);
    public void setLevelUrl(String url);
    public String getLevelUrl();

}
