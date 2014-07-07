package net.evlikat.hexatrix.views;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.exception.OutOfCharactersException;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
public class MainMenuView extends GameView {

    public static final String TOUCH_TO_BEGIN = "Touch to begin";
    private final MenuCallback menuCallback;
    private int lastScore = -1;
    private int topScore = -1;
    private final Text text;

    public MainMenuView(Engine engine, Camera camera, IFont font, MenuCallback callback) {
        super(engine, camera);
        this.menuCallback = callback;
        this.text = new Text(30, 30, font, TOUCH_TO_BEGIN, 255, engine.getVertexBufferObjectManager());
    }

    @Override
    public void populate() {
        scene.setBackground(new Background(0, 0, 0));
        scene.attachChild(text);
        displayResults();
    }

    @Override
    public void update() {
    }

    @Override
    protected void onTouch(TouchEvent pSceneTouchEvent) {
        menuCallback.toPlayView();
    }

    public void registerResult(GameResults gameResults) {
        lastScore = gameResults.getScores();
        topScore = Math.max(topScore, gameResults.getScores());
        displayResults();
    }

    private void displayResults() throws OutOfCharactersException {
        StringBuilder builder = new StringBuilder(TOUCH_TO_BEGIN);
        if (lastScore > -1 && topScore > -1) {
            builder.append("\n").append("Your scores: ").append(lastScore);
            builder.append("\n").append("Top scores: ").append(topScore);
        }
        text.setText(builder.toString());
    }

}
