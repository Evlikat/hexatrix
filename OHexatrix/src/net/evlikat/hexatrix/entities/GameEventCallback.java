package net.evlikat.hexatrix.entities;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 07, 2014)
 */
public interface GameEventCallback {

    public void onLinesRemoved(int linesRemoved);

    public void reset();

    public long framesPerTick();

}
