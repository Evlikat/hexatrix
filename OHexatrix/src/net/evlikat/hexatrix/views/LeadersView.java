package net.evlikat.hexatrix.views;

import net.evlikat.hexatrix.scores.IScoreStorage;
import net.evlikat.hexatrix.scores.Score;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;

import java.text.SimpleDateFormat;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Oct 20, 2014)
 */
public class LeadersView extends GameView {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private final LeadersCallback leadersCallback;
    private final IScoreStorage scoreStorage;
    private final Text table;
    private String resultsTable;

    public LeadersView(Engine engine, Camera camera, IFont font, LeadersCallback leadersCallback, IScoreStorage scoreStorage) {
        super(engine, camera);
        this.leadersCallback = leadersCallback;
        this.scoreStorage = scoreStorage;
        this.table = new Text(30, 30, font, "", 400, engine.getVertexBufferObjectManager());
    }

    @Override
    public void populate() {
        scene.attachChild(table);
    }

    @Override
    protected void onTouch(TouchEvent pSceneTouchEvent) {
        leadersCallback.toMenuView();
    }

    @Override
    public void update() {
    }

    public void updateResults() {
        StringBuilder result = new StringBuilder("##   SCORE          DATE\n");
        int i = 1;
        for (Score score : scoreStorage.getTopScores()) {
            result.append(
                    String.format(
                            "%02d    %04d    %s", i++, score.getAmount(), DATE_FORMAT.format(score.getDate())
                    )
            ).append("\n");
        }
        resultsTable = result.toString();
        table.setText(resultsTable);
    }

    @Override
    public void onBackPressed() {
        leadersCallback.toMenuView();
    }
}
