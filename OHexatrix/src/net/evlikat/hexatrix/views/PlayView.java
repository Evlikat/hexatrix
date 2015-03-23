package net.evlikat.hexatrix.views;

import net.evlikat.hexatrix.Textures;
import net.evlikat.hexatrix.axial.EmptyFieldGenerator;
import net.evlikat.hexatrix.entities.*;
import net.evlikat.hexatrix.scores.IScoreStorage;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.region.TextureRegion;

/**
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
            int totalScore = gameSession.addScore(linesRemoved);
            levels.setLevelByScore(totalScore);
        }

        public void reset() {
            levels.reset();
        }

        public long framesPerTick() {
            return levels.framesPerTick();
        }
    }

    private static final float SQ3 = (float) Math.sqrt(3);

    private static final int DEPTH = 25;
    private static final int WIDTH = 11;

    private final HexagonalField field;
    private final Background background;
    //private final Textures textures;
    private final float size;
    private final TouchListener touchListener;
    private final GameSession gameSession;
    private final Sprite pauseSprite;
    private final Levels levels = new Levels();

    public PlayView(Engine engine, Camera camera, Textures textures, IFont font,
                    final PlayCallback playCallback, final IScoreStorage scoreStorage) {
        super(engine, camera);
        this.size = camera.getHeight() / ((DEPTH + 1) * (SQ3)); // pixels per hexagon
        float leftShift = size + (WIDTH + 2) * size * 3 / 2;
        this.gameSession = new GameSession(levels, leftShift, 10, font, engine.getVertexBufferObjectManager());
        final GameEventCallbackImpl gameEventCallback = new GameEventCallbackImpl(levels, gameSession);
        this.field = HexagonalField.generateJar(
                WIDTH, DEPTH, new SpriteContext(size, camera, textures, engine),
                gameEventCallback,
                new EmptyFieldGenerator());
        TextureRegion pauseTexture = textures.getPause();
        this.pauseSprite = new Sprite(
                (camera.getWidth() - pauseTexture.getWidth()) / 2,
                (camera.getHeight() - pauseTexture.getHeight()) / 2,
                pauseTexture,
                engine.getVertexBufferObjectManager());
        this.pauseSprite.setVisible(false);
        this.touchListener = new TouchListener(field);
        this.background = new Background(0.1f, 0.1f, 0.1f);
        //new SpriteBackground(new Sprite(0, 0, textures.getBackground(), engine.getVertexBufferObjectManager()));
        //
        this.field.addGameFinishedListener(new GameFinishedListener() {
            @Override
            public void onGameFinished() {
                final GameResults gameResults = new GameResults(gameSession.getScore());
                playCallback.toMenuView(gameResults);
                scoreStorage.save(gameResults);
                pauseSprite.setVisible(false);
            }
        });
        this.field.addPausedFinishedListener(new GamePausedListener() {
            @Override
            public void onGamePaused() {
                pauseSprite.setVisible(true);
            }

            @Override
            public void onGameResumed() {
                pauseSprite.setVisible(false);
            }
        });
    }

    public void startNewGame() {
        field.restart(new EmptyFieldGenerator());
        gameSession.resetResults();
        levels.reset();
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
        scene.attachChild(pauseSprite);
    }

    @Override
    public void update() {
        field.update();
    }

    @Override
    public void onBackPressed() {
        field.pause();
    }
}
