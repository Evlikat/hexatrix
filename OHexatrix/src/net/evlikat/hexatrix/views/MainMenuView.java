package net.evlikat.hexatrix.views;

import java.util.Date;
import java.util.List;
import net.evlikat.hexatrix.Textures;
import net.evlikat.hexatrix.scores.IScoreStorage;
import net.evlikat.hexatrix.scores.Scores;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
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
    private final Background background;
    private final ButtonSprite startButton;
    private final ButtonSprite leadersButton;
    private final ButtonSprite quitButton;

    public MainMenuView(Engine engine, Camera camera, Textures textures, IFont font, final MenuCallback callback, final IScoreStorage scoreStorage) {
        super(engine, camera);
        this.text = new Text(30, 30, font, "", 255, engine.getVertexBufferObjectManager());
        this.background = new SpriteBackground(new Sprite(0, 0, textures.getBackground(), engine.getVertexBufferObjectManager()));
        //
        float buttonHeight = camera.getHeight() / 5;
        float buttonLeft = (camera.getWidth() - textures.getStartBtn().getWidth()) / 2;
        // load scores
        this.lastScore = lastScore(scoreStorage.getTopScores());
        this.topScore = topScore(scoreStorage.getTopScores());
        //
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

    private static int lastScore(List<Scores> scores) {
        int lastScore = -1;
        Date latestDate = new Date(0);
        for (Scores sc : scores) {
            Date date = sc.getDate();
            if (date.after(latestDate)) {
                latestDate = date;
                lastScore = sc.getAmount();
            }
        }
        return lastScore;
    }

    private static int topScore(List<Scores> scores) {
        if (scores.isEmpty()) {
            return -1;
        }
        return scores.get(0).getAmount();
    }

    @Override
    public void populate() {
        scene.setBackground(background);
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
