package net.evlikat.hexatrix.scenes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import static android.view.View.VISIBLE;
import net.evlikat.hexatrix.Scene;
import net.evlikat.hexatrix.scenes.callbacks.MainMenuCallback;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 01, 2014)
 */
public class MainMenuScene extends Scene {

    private final MainMenuCallback mainMenuCallback;

    public MainMenuScene(MainMenuCallback mainMenuCallback) {
        this.mainMenuCallback = mainMenuCallback;
    }

    public void draw(Canvas canvas) {
        if (canvas != null) {
            canvas.drawColor(Color.WHITE);
            canvas.drawText("Main menu. Touch to start", 30, 30, new Paint(VISIBLE));
        }
    }

    public void update() {
    }

    public boolean onTouchEvent(MotionEvent event) {
        mainMenuCallback.toGameScene();
        return true;
    }

    public void init() {
    }

}
