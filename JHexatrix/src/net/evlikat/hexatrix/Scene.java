package net.evlikat.hexatrix;

import android.view.MotionEvent;
import javax.microedition.khronos.opengles.GL10;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 01, 2014)
 */
public abstract class Scene {

    protected boolean initialized = false;
    
    public abstract void draw(GL10 gl);

    public abstract void init(GL10 gl);

    public abstract void update();
    
    public abstract boolean onTouchEvent(MotionEvent event);

    public boolean isInitialized() {
        return initialized;
    }
}
