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

    private static final String TAG = TouchListener.class.getSimpleName();
    private final IHexagonalField field;
    private float downX = -1;
    private float downY = -1;

    public TouchListener(IHexagonalField field) {
        this.field = field;
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
        } else if (pSceneTouchEvent.isActionUp()) {
            if (downX > 0 && downY > 0) {
                onFling(downX, downY, pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
                downX = -1;
                downY = -1;
            }
        }
    }

    @Override
    public boolean onFling(float x1, float y1, float x2, float y2) {
        boolean result = false;
        try {
            float diffY = y2 - y1;
            float diffX = x2 - x1;
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (diffX > 0) {
                    field.move(MoveDirection.RIGHT);
                } else {
                    field.move(MoveDirection.LEFT);
                }
            } else {
                if (diffY < 0) {
                    if (diffX > 0) {
                        field.turn(RotateDirection.RIGHT);
                    } else {
                        field.turn(RotateDirection.LEFT);
                    }
                } else {
                    field.drop();
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
        }
        return result;
    }

};
