package net.evlikat.hexatrix.views;

import android.content.Context;
import net.evlikat.hexatrix.Textures;
import net.evlikat.hexatrix.entities.GameEventCallback;
import net.evlikat.hexatrix.entities.GameSession;
import net.evlikat.hexatrix.entities.HexagonalField;
import net.evlikat.hexatrix.entities.SpriteContext;
import net.evlikat.hexatrix.scores.IScoreStorage;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
public class PlayView extends GameView {

    private static final class GameEventCallbackImpl implements GameEventCallback {

        private final Levels levels;
        private final GameSession gameSession;

        public GameEventCallbackImpl(Levels levels, GameSession gameSession) {
            this.levels = levels;
            this.gameSession = gameSession;
        }

        public void onLinesRemoved(int linesRemoved) {
            int totalScores = gameSession.addScores(linesRemoved);
            levels.setLevelByScores(totalScores);
        }

        public void reset() {
            levels.reset();
        }

        public long framesPerTick() {
            return levels.framesPerTick();
        }

        public int getLevel() {
            return levels.getCurrentLevel();
        }

        public int getScoresToNext() {
            return levels.getScoresToNext();
        }
    }

    private static final float SQ3 = (float) Math.sqrt(3);

    private static final int DEPTH = 21;
    private static final int WIDTH = 9;

    private final HexagonalField field;
    private final Background background;
    //private final Textures textures;
    private final float size;
    private final TouchListener touchListener;
    private final PlayCallback playCallback;
    private final IScoreStorage scoreStorage;
    private final GameSession gameSession;
    private final Levels levels = new Levels();

    public PlayView(Context context, Engine engine, Camera camera, Textures textures, IFont font,
        PlayCallback playCallback, IScoreStorage scoreStorage) {
        super(engine, camera);
        //this.textures = textures;
        this.size = camera.getHeight() / ((DEPTH + 1) * (SQ3)); // pixels per hexagon
        this.playCallback = playCallback;
        float leftShift = size + (WIDTH + 2) * size * 3 / 2;
        this.gameSession = new GameSession(levels, leftShift, 10, font, engine.getVertexBufferObjectManager());
        final GameEventCallbackImpl gameEventCallback = new GameEventCallbackImpl(levels, gameSession);
        this.field = HexagonalField.generateJar(
            WIDTH, DEPTH, new SpriteContext(size, camera, textures, engine, font), gameEventCallback);
        this.touchListener = new TouchListener(field);
        this.background = new SpriteBackground(new Sprite(0, 0, textures.getBackground(), engine.getVertexBufferObjectManager()));
        this.scoreStorage = scoreStorage;
    }

    @Override
    protected void onTouch(TouchEvent pSceneTouchEvent) {
        touchListener.onTouchEvent(pSceneTouchEvent);
    }

    @Override
    public void populate() {
        scene.setBackground(background);
        scene.attachChild(field);
        scene.attachChild(gameSession);
    }

    @Override
    public void update() {
        field.update();
        if (!field.isActive()) {
            field.restart();
            final GameResults gameResults = new GameResults(gameSession.getScores());
            playCallback.toMenuView(gameResults);
            scoreStorage.save(gameResults);
            gameSession.resetResults();
            levels.reset();
        }
    }
}
