package net.evlikat.hexatrix.scenes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
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

    public void init(GL10 gl) {
        gestureDetector = new GestureDetector(parentView.getContext(), gestureListener);
        loadBitmaps(gl);
        initialized = true;
    }

    public void draw(GL10 gl) {
        if (gl != null) {
            drawText(gl);
            drawStackBorders(gl);
            drawFields(gl);
            drawFigure(gl);
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

    private void drawText(GL10 gl) {
        final float stackWidth = size * 3 / 2 * (physics.getWidth() + 2) + size / 2;
        //gl.drawText("L: " + physics.getCurrentLevel(), stackWidth + 10, 20, new Paint(Paint.ANTI_ALIAS_FLAG));
        //gl.drawText("S: " + physics.getGameSession().getScores(), stackWidth + 10, 40, new Paint(Paint.ANTI_ALIAS_FLAG));
    }

    private void drawFigure(GL10 gl) {
        if (physics.getField().getFloatFigure() == null) {
            return;
        }
        for (AxialPosition field : physics.getField().getFloatFigure().getPartsPositions()) {
            drawBlock(figureBmp, gl, field);
        }
    }

    private void drawFields(GL10 gl) {
        for (AxialPosition field : physics.getField().getFields()) {
            if (field.getLine() % 2 == 0) {
                drawBlock(hexagon0Bmp, gl, field);
            } else {
                drawBlock(hexagon1Bmp, gl, field);
            }
        }
    }

    private void drawStackBorders(GL10 gl) {
        for (AxialPosition field : physics.getField().getBorders()) {
            drawBlock(borderBmp, gl, field);
        }
    }

    private void drawNextFigure(GL10 gl) {
        for (AxialPosition field : physics.getNextFigure().getPartsPositions()) {
            drawBlock(figureBmp, gl, field, 100, -100);
        }
    }

    private void drawBlock(Bitmap bitmap, GL10 gl, AxialPosition position) {
        drawBlock(bitmap, gl, position, 0, 0);
    }

    private void drawBlock(Bitmap bitmap, GL10 gl, AxialPosition position, float leftShift, float topShift) {
        final float q = position.getQ();
        final float r = position.getR();
        final float x = leftShift + (size * 3 / 2 * q) + (size * 2);
        final float y = topShift + parentView.getHeight() - size * SQ3 * (r + q / 2) - (size * 2 * SQ3);
        drawBitmap(gl , 0);
//            bitmap,
//            srcRect,
//            new RectF(x, y, x + 2 * size, y + size * SQ3),
//            hexPaint
//        );
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private void loadBitmaps(GL10 gl) {
        hexagon0Bmp = BitmapFactory.decodeResource(parentView.getResources(), R.drawable.hexagon0);
        hexagon1Bmp = BitmapFactory.decodeResource(parentView.getResources(), R.drawable.hexagon1);
        figureBmp = BitmapFactory.decodeResource(parentView.getResources(), R.drawable.figure);
        borderBmp = BitmapFactory.decodeResource(parentView.getResources(), R.drawable.border);
        int i = 0;
        loadTexture(gl, hexagon0Bmp, i++);
        loadTexture(gl, hexagon1Bmp, i++);
        loadTexture(gl, figureBmp, i++);
        loadTexture(gl, borderBmp, i++);
    }

    private static final int TEXTURE_NUMBER = 4;
    private final int[] textures = new int[TEXTURE_NUMBER];

    private void loadTexture(GL10 gl, Bitmap bitmap, int i) {
        // generate one texture pointer
        gl.glGenTextures(TEXTURE_NUMBER, textures, i);
        // ...and bind it to our array
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[i]);

        // create nearest filtered texture
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        // Use Android GLUtils to specify a two-dimensional texture image from our bitmap
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        // Clean up
        bitmap.recycle();
    }
    private FloatBuffer textureBuffer;
    private FloatBuffer vertexBuffer;
    private final float texture[] = {
        0.0f, 1.0f, // top left		(V2)
        0.0f, 0.0f, // bottom left	(V1)
        1.0f, 1.0f, // top right	(V4)
        1.0f, 0.0f // bottom right	(V3)
    };
    private final float vertices[] = {
			-1.0f, -1.0f,  0.0f,		// V1 - bottom left
			-1.0f,  1.0f,  0.0f,		// V2 - top left
			 1.0f, -1.0f,  0.0f,		// V3 - bottom right
			 1.0f,  1.0f,  0.0f			// V4 - top right
	};

    private void drawBitmap(GL10 gl, int index) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        byteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuffer.asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);
        // bind the previously generated texture
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[index]);

        // Point to our buffers
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        // Set the face rotation
        gl.glFrontFace(GL10.GL_CW);

        // Point to our vertex buffer
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

        // Draw the vertices as triangle strip
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);

        //Disable the client state before leaving
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }
}
