package de.sopamo.triangula.android.game;

import android.util.Log;
import de.sopamo.triangula.android.PGRenderer;
import de.sopamo.triangula.android.game.mechanics.Entity;
import de.sopamo.triangula.android.game.mechanics.Rewindable;
import de.sopamo.triangula.android.game.raycasting.Raycaster;

import java.util.List;

public class PhysicsTask extends Thread {
    private GameImpl mGame;
    private InputHandler mHandler;

    private List<Entity> mEntityList;
    private List<Rewindable> mRewindableList;

    private static boolean mUpdating = false;
    private boolean mCanceled = false;
    private boolean mWait = true;
    private boolean mReinit = false;


    public PhysicsTask() {
        mGame = GameImpl.getInstance();
        mHandler = mGame.getHandler();
        mEntityList = mGame.getEntities();
        mRewindableList = mGame.getRewindables();
    }

    @Override
    public void run() {
        mUpdating = true;
        startWait();
        while(true) {
            if(isCancelled()) {
                break;
            }
            mHandler.update();

            /** Save Rewindable Actions **/
            for (int i = 0; i < mRewindableList.size(); i++) {
                Rewindable rewindable = mRewindableList.get(i);
                rewindable.updateRewindable();
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
            long time1 = System.currentTimeMillis();
            if(!PGRenderer.isDisableRayCast()) {
                Raycaster.cast();
            }
            Log.e("RayCast1",System.currentTimeMillis()-time1+"");
            long time2= System.currentTimeMillis();
            mGame.getWorld().step(GameImpl.TIME_STEP, GameImpl.ITERATIONS);
            Log.e("Physics", System.currentTimeMillis() - time2 + "");


            if(isCancelled()) {
                break;
            }
            startWait();

            /** Update Entities **/

            for (int i = 0; i < mEntityList.size(); i++) {
                Entity entity = mEntityList.get(i);
                entity.updateEntity();
            }


            if(isCancelled()) {
                break;
            }
            startWait();


        }
        mUpdating = false;
        mWait = true;
        Thread.currentThread().interrupt();
    }

    private synchronized void startWait() {
        mWait = true;
        try {
            Thread.currentThread().wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mWait = false;
    }


    private synchronized void cancel() {
        this.mCanceled = true;
        this.notify();
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

    public void reinit() {
        mReinit = true;
    }
}
