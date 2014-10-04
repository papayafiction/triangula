package de.sopamo.triangula.android.game.mechanics;

public interface Rewindable {
    public void startRewind();
    public void stopRewind();
    public void updateRewindable();
    public boolean isRewinding();
}
