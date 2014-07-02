package net.evlikat.hexatrix.scenes;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import javax.microedition.khronos.opengles.GL10;
import net.evlikat.hexatrix.Scene;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 01, 2014)
 */
public class MainView extends GLSurfaceView implements SurfaceHolder.Callback {

    private Scene currentScene;

    public MainView(Context context) {
        super(context);
    }

    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return currentScene.onTouchEvent(event);
    }

    public void draw(GL10 dl) {
        if (currentScene != null) {
            currentScene.draw(dl);
        }
    }

    public void update() {
        if (currentScene != null) {
            currentScene.update();
        }
    }

    void checkInit(GL10 gl) {
        if (!currentScene.isInitialized()) {
            currentScene.init(gl);
        }
    }

}
