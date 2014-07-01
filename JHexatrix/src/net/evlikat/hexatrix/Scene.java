package net.evlikat.hexatrix;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 01, 2014)
 */
public abstract class Scene {

    public abstract void draw(Canvas canvas);

    public abstract void init();

    public abstract void update();
    
    public abstract boolean onTouchEvent(MotionEvent event);
}
