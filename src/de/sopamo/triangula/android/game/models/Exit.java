package de.sopamo.triangula.android.game.models;

import android.content.Intent;
import de.sopamo.box2dbridge.IBody;
import de.sopamo.triangula.android.GameActivity;
import de.sopamo.triangula.android.SuccessScreenActivity;
import de.sopamo.triangula.android.game.GameImpl;
import de.sopamo.triangula.android.game.mechanics.Entity;
import de.sopamo.triangula.android.game.mechanics.UserData;
import de.sopamo.triangula.android.geometry.GLCircle;
import de.sopamo.triangula.android.geometry.GLRectangle;
import de.sopamo.triangula.android.geometry.GameShapeCircle;
import de.sopamo.triangula.android.geometry.GameShapeRectangle;
import org.jbox2d.common.Vec2;

import java.io.Serializable;

public class Exit implements Entity {

    public final static int FRAMES_TO_END = 1;

    private IBody exitBody;
    private IBody suckerBody;
    private boolean removeSucker;
    private boolean endGame;
    private int frames;
    private GameImpl game;

    public Exit(Vec2 pst) {
        game = GameImpl.getInstance();
        GameShapeRectangle rectangle = new GameShapeRectangle(new GLRectangle(.4f,.8f));
        IBody rectangleBody = rectangle.attachToNewBody(game.getWorld(),null,0);
        rectangleBody.setPosition(pst);
        UserData data = new UserData();
        data.type = "exit";
        data.obj = this;
        rectangleBody.setUserData(data);
        rectangle.setColor(0, 0, 0, 1);
        game.getGsl().add(rectangle);
        this.exitBody = rectangleBody;

        GameShapeCircle circle = new GameShapeCircle(new GLCircle(1.5f));
        IBody sucker = circle.attachToNewBody(game.getWorld(),null,0);
        sucker.setPosition(pst);
        UserData suckerData = new UserData();
        suckerData.type = "sucker";
        suckerData.obj = this;
        sucker.setUserData(suckerData);
        this.suckerBody = sucker;

        game.getEntities().add(this);
        game.getNoRayCast().add(sucker);

    }

    public void endGame() {
        endGame = true;
    }

    public void removeSucker() {
        this.removeSucker = true;
    }

    @Override
    public void updateEntity() {
        if (removeSucker) {
            removeSucker = false;
            game.getWorld().destroyBody(suckerBody);
        }
        if(endGame) {
            frames++;
            if(frames == FRAMES_TO_END) {
                GameActivity activity = GameActivity.getInstance();
                Intent successScreen = new Intent(activity, SuccessScreenActivity.class);
                GameImpl.getInstance().getLevel().end();
                successScreen.putExtra("level",GameImpl.getInstance().getLevel());
                if(GameImpl.getInstance().getLevel().getAchievements().size() != 0) {
                    successScreen.putExtra("achievements",(Serializable) GameImpl.getInstance().getLevel().getAchievements());
                }
                activity.startActivity(successScreen);
                activity.finish();
            }
        }
    }

    public IBody getExitBody() {
        return exitBody;
    }
}
