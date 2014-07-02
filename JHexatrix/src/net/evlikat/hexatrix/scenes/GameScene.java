package net.evlikat.hexatrix.scenes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import net.evlikat.hexatrix.GameSession;
import net.evlikat.hexatrix.Physics;
import net.evlikat.hexatrix.R;
import net.evlikat.hexatrix.Scene;
import net.evlikat.hexatrix.axial.AxialPosition;
import net.evlikat.hexatrix.axial.MoveDirection;
import net.evlikat.hexatrix.axial.RandomFigureGenerator;
import net.evlikat.hexatrix.axial.RotateDirection;
import net.evlikat.hexatrix.axial.TrivialFieldGenerator;
import net.evlikat.hexatrix.scenes.callbacks.GameMenuCallback;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jun 30, 2014)
 */
public class GameScene extends Scene {

    private static final String TAG = GameScene.class.getSimpleName();

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
    private Bitmap hexagon0Bmp;
    private Bitmap hexagon1Bmp;
    private Bitmap borderBmp;
    private Bitmap figureBmp;
    // Pre-calculated
    private static final int HEXAGON_BITMAP_WIDTH = 200;
    private static final int HEXAGON_BITMAP_HEIGHT = 173;
    private static final float SQ3 = (float) Math.sqrt(3);
    private final Rect srcRect = new Rect(0, 0, HEXAGON_BITMAP_WIDTH, HEXAGON_BITMAP_HEIGHT);
    private final float size;
    private final Paint hexPaint = new Paint();

    public GameScene(GameMenuCallback gameMenuCallback, MainView mainView) {
        this.gameMenuCallback = gameMenuCallback;
        this.physics = new Physics(new TrivialFieldGenerator());
        this.physics.setFigureGenerator(new RandomFigureGenerator());
        this.parentView = mainView;
        this.size = parentView.getHeight() / ((physics.getDepth() + 1) * (SQ3)); // pixels per hexagon
        //
        hexPaint.setAntiAlias(true);
        hexPaint.setFilterBitmap(true);
        hexPaint.setDither(true);
    }

    public void init() {
        gestureDetector = new GestureDetector(parentView.getContext(), gestureListener);
        loadBitmaps();
    }

    public void draw(Canvas canvas) {
        if (canvas != null) {
            canvas.drawColor(Color.LTGRAY);
            drawText(canvas);
            drawStackBorders(canvas);
            drawFields(canvas);
            drawFigure(canvas);
            //drawNextFigure(canvas);
        }
    }

    public void update() {
        if (!physics.isActive()) {
            final GameSession gameSession = physics.getGameSession();
            physics.reset();
            gameMenuCallback.toMainMenu(gameSession);
        }
        physics.update();
    }

    private void drawText(Canvas canvas) {
        final float stackWidth = size * 3 / 2 * (physics.getWidth() + 2) + size / 2;
        canvas.drawText("L: " + physics.getCurrentLevel(), stackWidth + 10, 20, new Paint(Paint.ANTI_ALIAS_FLAG));
        canvas.drawText("S: " + physics.getGameSession().getScores(), stackWidth + 10, 40, new Paint(Paint.ANTI_ALIAS_FLAG));
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
            if (field.getLine() % 2 == 0) {
                drawBlock(hexagon0Bmp, canvas, field);
            } else {
                drawBlock(hexagon1Bmp, canvas, field);
            }
        }
    }

    private void drawStackBorders(Canvas canvas) {
        for (AxialPosition field : physics.getField().getBorders()) {
            drawBlock(borderBmp, canvas, field);
        }
    }

    private void drawNextFigure(Canvas canvas) {
        for (AxialPosition field : physics.getNextFigure().getPartsPositions()) {
            drawBlock(figureBmp, canvas, field, 100, -100);
        }
    }

    private void drawBlock(Bitmap bitmap, Canvas canvas, AxialPosition position) {
        drawBlock(bitmap, canvas, position, 0, 0);
    }

    private void drawBlock(Bitmap bitmap, Canvas canvas, AxialPosition position, float leftShift, float topShift) {
        final float q = position.getQ();
        final float r = position.getR();
        final float x = leftShift + (size * 3 / 2 * q) + (size * 2);
        final float y = topShift + parentView.getHeight() - size * SQ3 * (r + q / 2) - (size * 2 * SQ3);
        canvas.drawBitmap(
            bitmap,
            srcRect,
            new RectF(x, y, x + 2 * size, y + size * SQ3),
            hexPaint
        );
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private void loadBitmaps() {
        hexagon0Bmp = BitmapFactory.decodeResource(parentView.getResources(), R.drawable.hexagon0);
        hexagon1Bmp = BitmapFactory.decodeResource(parentView.getResources(), R.drawable.hexagon1);
        figureBmp = BitmapFactory.decodeResource(parentView.getResources(), R.drawable.figure);
        borderBmp = BitmapFactory.decodeResource(parentView.getResources(), R.drawable.border);
    }
}
