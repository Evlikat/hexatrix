package net.evlikat.hexatrix.views;

import android.util.Log;
import net.evlikat.hexatrix.axial.MoveDirection;
import net.evlikat.hexatrix.axial.RotateDirection;
import net.evlikat.hexatrix.entities.IHexagonalField;
import org.andengine.input.touch.TouchEvent;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 04, 2014)
 */
public class TouchListener implements ITouchListener {

    private static final int CLICK_RADIUS = 30;
    private static final String TAG = TouchListener.class.getSimpleName();
    private final IHexagonalField field;
    private final int pixelsPerStep;
    private float downX = -1;
    private float downY = -1;

    public TouchListener(IHexagonalField field, int pixelsPerStep) {
        this.field = field;
        this.pixelsPerStep = pixelsPerStep;
    }

    /**
     * Define how to handle event here
     *
     * @param pSceneTouchEvent
     */
    public void onTouchEvent(TouchEvent pSceneTouchEvent) {
        if (pSceneTouchEvent.isActionDown()) {
            downX = pSceneTouchEvent.getX();
            downY = pSceneTouchEvent.getY();
        } else if (pSceneTouchEvent.isActionMove()) {
            onMove(downX, downY, pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
        } else if (pSceneTouchEvent.isActionUp()) {
            if (downX > 0 && downY > 0) {
                onFling(downX, downY, pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
                downX = -1;
                downY = -1;
            }
        }
    }

    @Override
    public boolean onMove(float x1, float y1, float x2, float y2) {
        float diffY = y2 - y1;
        float diffX = x2 - x1;
        if (diffX * diffX + diffY * diffY > CLICK_RADIUS * CLICK_RADIUS) {
            if (Math.abs(diffX) > Math.abs(diffY)) {
                int steps = calcSteps(diffX);
                if (diffX > 0) {
                    field.moving(MoveDirection.RIGHT, steps);
                } else {
                    field.moving(MoveDirection.LEFT, steps);
                }
            }
        }
        return false;
    }

    @Override
    public boolean onFling(float x1, float y1, float x2, float y2) {
        boolean result = false;
        try {
            float diffY = y2 - y1;
            float diffX = x2 - x1;
            // if fling is out of single click circle
            if (diffX * diffX + diffY * diffY > CLICK_RADIUS * CLICK_RADIUS) {
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    int steps = calcSteps(diffX);
                    if (diffX > 0) {
                        field.move(MoveDirection.RIGHT, steps);
                    } else {
                        field.move(MoveDirection.LEFT, steps);
                    }
                } else {
                    if (diffY < 0) {
                        if (diffX > 0) {
                            field.turn(RotateDirection.CLOCKWISE);
                        } else {
                            field.turn(RotateDirection.COUNTERCLOCKWISE);
                        }
                    } else {
                        field.drop();
                    }
                }
            } else {
                // handle like single click
                field.turn(RotateDirection.CLOCKWISE);
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
        }
        return result;
    }

    private int calcSteps(float diffX) {
        int steps = ((int)Math.abs(diffX) / pixelsPerStep);
        if (steps == 0) {
            steps = 1; // handle short slides
        }
        return steps;
    }

}
