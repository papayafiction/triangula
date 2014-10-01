package de.sopamo.triangula.android.game;

import android.os.AsyncTask;
import android.util.Log;
import de.sopamo.triangula.android.game.mechanics.Entity;
import de.sopamo.triangula.android.game.mechanics.Rewindable;

public class PhysicsTask extends AsyncTask<Void,Void,Void> {
    private GameImpl mGame;
    private InputHandler mHandler;
    private static boolean mUpdating = false;
    private boolean mWait = true;


    public PhysicsTask() {
        mGame = GameImpl.getInstance();
        mHandler = mGame.getHandler();
    }

    @Override
    protected Void doInBackground(Void... params) {
        while(true) {
            if(isCancelled()) {
                break;
            }
            while(mWait && !isCancelled());
            mWait = true;
            if (mGame.getWorld() == null) {
                Log.e("pg", "World not initialized");
                continue;
            }

            mHandler.update();

            mUpdating = true;
            /** Save Rewindable Actions **/
            for (int i = 0; i < mGame.getRewindables().size(); i++) {
                Rewindable rewindable = mGame.getRewindables().get(i);
                rewindable.run();
            }

            /** DO SOME REWIND **/
            if (mHandler.longTouched) {
                for (int i = 0; i < mGame.getRewindables().size(); i++) {
                    Rewindable rewindable = mGame.getRewindables().get(i);
                    if (!rewindable.isRewinding()) rewindable.startRewind();
                }
            } else {
                for (int i = 0; i < mGame.getRewindables().size(); i++) {
                    Rewindable rewindable = mGame.getRewindables().get(i);
                    if (rewindable.isRewinding()) rewindable.stopRewind();
                }
            }

            mGame.getWorld().step(GameImpl.TIME_STEP, GameImpl.ITERATIONS);

            /** Update Entities **/
            for (int i = 0; i < mGame.getEntities().size(); i++) {
                Entity entity = mGame.getEntities().get(i);
                entity.update();
            }
            mUpdating = false;
        }
        return null;
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
