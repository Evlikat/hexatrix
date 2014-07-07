package net.evlikat.hexatrix.views;

import net.evlikat.hexatrix.Textures;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.ButtonSprite;
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

    private int lastScore = -1;
    private int topScore = -1;
    private final Text text;
    private final ButtonSprite startButton;
    private final ButtonSprite leadersButton;
    private final ButtonSprite quitButton;

    public MainMenuView(Engine engine, Camera camera, Textures textures, IFont font, final MenuCallback callback) {
        super(engine, camera);
        this.text = new Text(30, 30, font, "", 255, engine.getVertexBufferObjectManager());
        //
        float buttonHeight = camera.getHeight() / 5;
        float buttonLeft = (camera.getWidth() - textures.getStartBtn().getWidth()) / 2;
        int i = 1;
        this.startButton = new ButtonSprite(buttonLeft, buttonHeight * i++, textures.getStartBtn(),
            engine.getVertexBufferObjectManager(), new ButtonSprite.OnClickListener() {

                public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                    callback.toPlayView();
                }
            });
        this.leadersButton = new ButtonSprite(buttonLeft, buttonHeight * i++, textures.getLeadersBtn(),
            engine.getVertexBufferObjectManager(), new ButtonSprite.OnClickListener() {

                public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                }
            });
        this.quitButton = new ButtonSprite(buttonLeft, buttonHeight * i++, textures.getQuitBtn(),
            engine.getVertexBufferObjectManager(), new ButtonSprite.OnClickListener() {

                public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                    callback.quit();
                }
            });
    }

    @Override
    public void populate() {
        scene.setBackground(new Background(0, 0, 0));
        scene.attachChild(text);
        scene.registerTouchArea(startButton);
        scene.registerTouchArea(leadersButton);
        scene.registerTouchArea(quitButton);
        scene.attachChild(startButton);
        scene.attachChild(leadersButton);
        scene.attachChild(quitButton);
        displayResults();
    }

    @Override
    public void update() {
    }

    public void registerResult(GameResults gameResults) {
        lastScore = gameResults.getScores();
        topScore = Math.max(topScore, gameResults.getScores());
        displayResults();
    }

    private void displayResults() throws OutOfCharactersException {
        StringBuilder builder = new StringBuilder();
        if (lastScore > -1 && topScore > -1) {
            builder.append("Your scores: ").append(lastScore).append("\n");
            builder.append("Top scores: ").append(topScore);
        }
        text.setText(builder.toString());
    }

    @Override
    protected void onTouch(TouchEvent pSceneTouchEvent) {
    }

}
