package net.evlikat.hexatrix.views;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 04, 2014)
 */
public interface ITouchListener {

    boolean onFling(float x1, float y1, float x2, float y2);

    boolean onMove(float x1, float y1, float x2, float y2);
}
