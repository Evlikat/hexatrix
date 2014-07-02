package net.evlikat.hexatrix;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import net.evlikat.hexatrix.scenes.MainView;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jun 30, 2014)
 */
public class GameLoop extends Thread {

    private static final String TAG = GameLoop.class.getSimpleName();

    private final static int MAX_FPS = 50;
    private final static int MAX_FRAME_SKIPS = 5;
    private final static int FRAME_PERIOD = 1000 / MAX_FPS;

    private final SurfaceHolder surfaceHolder;
    private final MainView mainView;

    private boolean running;

    public GameLoop(SurfaceHolder surfaceHolder, MainView mainView) {
        this.surfaceHolder = surfaceHolder;
        this.mainView = mainView;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        long beginTime;     // the time when the cycle begun
        long timeDiff;      // the time it took for the cycle to execute
        int sleepTime;      // ms to sleep (<0 if we're behind)
        int framesSkipped;  // number of frames being skipped

        try {
            while (running) {
                beginTime = System.currentTimeMillis();
                framesSkipped = 0;
                update();
                draw();
                timeDiff = System.currentTimeMillis() - beginTime;
                sleepTime = (int) (FRAME_PERIOD - timeDiff);

                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        // OK
                    }
                }

                while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
                    update();
                    sleepTime += FRAME_PERIOD;
                    framesSkipped++;
                }
            }
        } catch (Throwable ex) {
            Log.e(TAG, "Error", ex);
        }
    }

    private void draw() {
        Canvas canvas = null;
        try {
            canvas = this.surfaceHolder.lockCanvas();
            this.mainView.draw(canvas);
        } catch (Throwable ex) {
            Log.d(TAG, ex.getMessage(), ex);
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void update() {
        mainView.update();
    }
}
