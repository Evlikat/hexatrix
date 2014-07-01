package net.evlikat.hexatrix.scenes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import net.evlikat.hexatrix.GameLoop;
import net.evlikat.hexatrix.Physics;
import net.evlikat.hexatrix.R;
import net.evlikat.hexatrix.Scene;
import net.evlikat.hexatrix.axial.AxialPosition;
import net.evlikat.hexatrix.axial.MoveDirection;
import net.evlikat.hexatrix.axial.RandomFigureGenerator;
import net.evlikat.hexatrix.axial.RotateDirection;
import net.evlikat.hexatrix.scenes.callbacks.GameMenuCallback;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jun 30, 2014)
 */
public class GameScene extends Scene {

    private static final String TAG = GameScene.class.getSimpleName();

    private final GameLoop loop;
    private final Physics physics;
    // 
    private GestureDetector gestureDetector;
    private final GameMenuCallback gameMenuCallback;
    private final MainView parentView;
    private final GestureDetector.OnGestureListener gestureListener = new SimpleOnGestureListener() {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            physics.tick();
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (diffX > 0) {
                        physics.move(MoveDirection.RIGHT);
                    } else {
                        physics.move(MoveDirection.LEFT);
                    }
                } else {
                    if (diffY < 0) {
                        if (diffX > 0) {
                            physics.turn(RotateDirection.RIGHT);
                        } else {
                            physics.turn(RotateDirection.LEFT);
                        }
                    } else {
                        physics.drop();
                    }
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage(), ex);
            }
            return result;
        }

    };
    // Resources
    private Bitmap hexagonBmp;
    private Bitmap borderBmp;
    private Bitmap figureBmp;

    public GameScene(GameMenuCallback gameMenuCallback, MainView mainView, GameLoop loop) {
        this.gameMenuCallback = gameMenuCallback;
        this.physics = new Physics();
        this.physics.setGenerator(new RandomFigureGenerator());
        this.loop = loop;
        this.parentView = mainView;
    }

    public void init() {
        gestureDetector = new GestureDetector(parentView.getContext(), gestureListener);
        loadBitmaps();
    }

    public void draw(Canvas canvas) {
        if (canvas != null) {
            canvas.drawColor(Color.BLACK);
            drawStackBorders(canvas);
            drawFields(canvas);
            drawFigure(canvas);
        }
    }

    public void update() {
        if (!physics.isActive()) {
            physics.reset();
            gameMenuCallback.toMainMenu();
        }
    }

    public void surfaceCreated(SurfaceHolder sh) {
    }

    public void surfaceChanged(SurfaceHolder sh, int i, int i1, int i2) {
    }

    public void surfaceDestroyed(SurfaceHolder sh) {
        boolean retry = true;
        while (retry) {
            try {
                loop.join();
                retry = false;
            } catch (InterruptedException e) {
                // OK
            }
        }
    }

    private void drawFigure(Canvas canvas) {
        if (physics.getField().getFloatFigure() == null) {
            return;
        }
        for (AxialPosition field : physics.getField().getFloatFigure().getPartsPositions()) {
            drawBlock(figureBmp, canvas, field);
        }
    }

    private void drawFields(Canvas canvas) {
        for (AxialPosition field : physics.getField().getFields()) {
            drawBlock(hexagonBmp, canvas, field);
        }
    }

    private void drawStackBorders(Canvas canvas) {
        for (AxialPosition field : physics.getField().getBorders()) {
            drawBlock(borderBmp, canvas, field);
        }
    }

    private void drawBlock(Bitmap bitmap, Canvas canvas, AxialPosition position) {
        final int SIZE = 20;
        float q = position.getQ();
        float r = position.getR();
        float x = (SIZE * 3 / 2 * q) + (SIZE * 2);
        float y = parentView.getHeight() - (SIZE * 3) - SIZE * ((float) Math.sqrt(3)) * (r + q / 2);
        canvas.drawBitmap(bitmap, x, y, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private void loadBitmaps() {
        hexagonBmp = BitmapFactory.decodeResource(parentView.getResources(), R.drawable.hexagon);
        figureBmp = BitmapFactory.decodeResource(parentView.getResources(), R.drawable.figure);
        borderBmp = BitmapFactory.decodeResource(parentView.getResources(), R.drawable.border);
    }
}