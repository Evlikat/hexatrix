package net.evlikat.hexatrix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import net.evlikat.hexatrix.axial.AxialPosition;
import net.evlikat.hexatrix.axial.MoveDirection;
import net.evlikat.hexatrix.axial.RotateDirection;
import net.evlikat.hexatrix.axial.TrivialFigureGenerator;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jun 30, 2014)
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = GamePanel.class.getSimpleName();

    private final GameLoop loop;
    private final Physics physics;
    // 
    private GestureDetector gestureDetector;
    private final GestureDetector.OnGestureListener gestureListener = new SimpleOnGestureListener() {

        private static final int SWIPE_MAX = 20;

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

    public GamePanel(Context context) {
        super(context);
        this.physics = new Physics();
        this.physics.setGenerator(new TrivialFigureGenerator());
        this.loop = new GameLoop(physics, getHolder());
        setFocusable(true);
    }

    public void init() {
        getHolder().addCallback(this);
        gestureDetector = new GestureDetector(getContext(), gestureListener);
        loadBitmaps();
        loop.setOutput(this);
        loop.setRunning(true);
        loop.start();
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

    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas != null) {
            canvas.drawColor(Color.BLACK);
            drawStackBorders(canvas);
            drawFields(canvas);
            drawFigure(canvas);
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
        float y = getHeight() - (SIZE * 3) - SIZE * ((float) Math.sqrt(3)) * (r + q / 2);
        canvas.drawBitmap(bitmap, x, y, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private void loadBitmaps() {
        hexagonBmp = BitmapFactory.decodeResource(getResources(), R.drawable.hexagon);
        figureBmp = BitmapFactory.decodeResource(getResources(), R.drawable.figure);
        borderBmp = BitmapFactory.decodeResource(getResources(), R.drawable.border);
    }
}
