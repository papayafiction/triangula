package de.sopamo.triangula.android.game;

import android.util.Log;
import de.sopamo.triangula.android.game.mechanics.Entity;
import de.sopamo.triangula.android.game.mechanics.Rewindable;

import java.util.List;

public class PhysicsTask extends Thread {
    private GameImpl mGame;
    private InputHandler mHandler;

    private List<Entity> mEntityList;
    private List<Rewindable> mRewindableList;

    private static boolean mUpdating = false;
    private boolean mCanceled = false;
    private boolean mWait = true;


    public PhysicsTask() {
        mGame = GameImpl.getInstance();
        mHandler = mGame.getHandler();
        mEntityList = mGame.getEntities();
        mRewindableList = mGame.getRewindables();
    }

    @Override
    public void run() {
        while(true) {
            while(mWait && !isCancelled());
            if(isCancelled()) {
                break;
            }
            mWait = true;
            if (mGame.getWorld() == null) {
                Log.e("pg", "World not initialized");
                continue;
            }
            mHandler.update();

            mUpdating = true;
            /** Save Rewindable Actions **/
            for (int i = 0; i < mRewindableList.size(); i++) {
                Rewindable rewindable = mRewindableList.get(i);
                rewindable.run();
            }

            /** DO SOME REWIND **/
            if (mHandler.longTouched) {
                for (int i = 0; i < mRewindableList.size(); i++) {
                    Rewindable rewindable = mRewindableList.get(i);
                    if (!rewindable.isRewinding()) rewindable.startRewind();
                }
            } else {
                for (int i = 0; i < mRewindableList.size(); i++) {
                    Rewindable rewindable = mRewindableList.get(i);
                    if (rewindable.isRewinding()) rewindable.stopRewind();
                }
            }

            mGame.getWorld().step(GameImpl.TIME_STEP, GameImpl.ITERATIONS);

            /** Update Entities **/
            for (int i = 0; i < mEntityList.size(); i++) {
                Entity entity = mEntityList.get(i);
                entity.update();
            }

            mUpdating = false;
            if(isCancelled()) {
                break;
            }
        }
        Thread.currentThread().interrupt();
    }

    private void cancel() {
        this.mCanceled = true;
    }

    public boolean isCancelled() {
        return mCanceled;
    }

    public void softCancel() {
        if(isCancelled()) return;
        cancel();
        while(isUpdating());
    }

    public boolean isWaiting() {
        return mWait;
    }

    public void setWaiting(boolean mWait) {
        this.mWait = mWait;
    }

    public static boolean isUpdating() {
        return mUpdating;
    }

}
