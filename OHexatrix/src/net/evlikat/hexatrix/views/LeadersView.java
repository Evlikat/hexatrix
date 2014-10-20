package net.evlikat.hexatrix.views;

import java.text.SimpleDateFormat;
import net.evlikat.hexatrix.scores.IScoreStorage;
import net.evlikat.hexatrix.scores.Scores;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;

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
        StringBuilder result = new StringBuilder("##   SCORES         DATE\n");
        int i = 1;
        for (Scores scores : scoreStorage.getTopScores()) {
            result.append(
                String.format(
                    "%02d    %04d    %s", i++, scores.getAmount(), DATE_FORMAT.format(scores.getDate())
                )
            ).append("\n");
        }
        table.setText(result.toString());
    }

}
