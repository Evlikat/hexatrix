package net.evlikat.hexatrix.scenes;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import net.evlikat.hexatrix.Scene;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 01, 2014)
 */
public class MainView extends SurfaceView implements SurfaceHolder.Callback {

    private Scene currentScene;

    public MainView(Context context) {
        super(context);
    }

    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public void surfaceCreated(SurfaceHolder sh) {
        sh.addCallback(this);
    }

    public void surfaceChanged(SurfaceHolder sh, int i, int i1, int i2) {
    }

    public void surfaceDestroyed(SurfaceHolder sh) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return currentScene.onTouchEvent(event);
    }

    @Override
    public void draw(Canvas canvas) {
        if (currentScene != null) {
            currentScene.draw(canvas);
        }
    }

    public void update() {
        if (currentScene != null) {
            currentScene.update();
        }
    }

}
