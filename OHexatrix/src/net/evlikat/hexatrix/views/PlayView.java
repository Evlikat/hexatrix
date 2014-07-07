package net.evlikat.hexatrix.views;

import android.content.Context;
import net.evlikat.hexatrix.Textures;
import net.evlikat.hexatrix.entities.GameEventCallback;
import net.evlikat.hexatrix.entities.GameSession;
import net.evlikat.hexatrix.entities.HexagonalField;
import net.evlikat.hexatrix.entities.SpriteContext;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
public class PlayView extends GameView {

    private static final float SQ3 = (float) Math.sqrt(3);

    private static final int DEPTH = 21;
    private static final int WIDTH = 9;

    private final HexagonalField field;
    //private final Textures textures;
    private final float size;
    private final TouchListener touchListener;
    private final PlayCallback playCallback;
    private final GameSession gameSession;

    public PlayView(Context context, Engine engine, Camera camera, Textures textures, IFont font, PlayCallback playCallback) {
        super(engine, camera);
        //this.textures = textures;
        this.size = camera.getHeight() / ((DEPTH + 1) * (SQ3)); // pixels per hexagon
        this.playCallback = playCallback;
        float leftShift = size + (WIDTH + 2) * size * 3 / 2;
        this.gameSession = new GameSession(leftShift, 10, font, engine.getVertexBufferObjectManager());
        this.field = HexagonalField.generateJar(
            WIDTH, DEPTH,
            new SpriteContext(size, camera, textures, engine, font),
            new GameEventCallback() {

                public void onLinesRemoved(int linesRemoved) {
                    gameSession.addScores(linesRemoved);
                }
            }
        );
        this.touchListener = new TouchListener(field);
    }

    @Override
    protected void onTouch(TouchEvent pSceneTouchEvent) {
        touchListener.onTouchEvent(pSceneTouchEvent);
    }

    @Override
    public void populate() {
        scene.setBackground(new Background(0, 0, 0));
        scene.attachChild(field);
        scene.attachChild(gameSession);
    }

    @Override
    public void update() {
        field.update();
        if (!field.isActive()) {
            field.restart();
            gameSession.resetResults();
            playCallback.toMenuView();
        }
    }
}
