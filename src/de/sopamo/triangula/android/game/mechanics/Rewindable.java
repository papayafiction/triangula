package de.sopamo.triangula.android.game.mechanics;

public interface Rewindable {
    public void startRewind();
    public void stopRewind();
    public void run();
    public boolean isRewinding();
}
