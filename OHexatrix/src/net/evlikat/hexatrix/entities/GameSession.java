package net.evlikat.hexatrix.entities;

import net.evlikat.hexatrix.views.Levels;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 01, 2014)
 */
public class GameSession extends Text {

    private int scores = 0;
    private final Levels levels;

    public GameSession(Levels levels, float pX, float pY, IFont pFont, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pFont, "", 80, pVertexBufferObjectManager);
        setColor(Color.WHITE);
        this.levels = levels;
        setScores(0);
    }

    public int addScores(int linesRemoved) {
        setScores(scores + (int) Math.pow(2, linesRemoved - 1));
        return scores;
    }

    private void setScores(int scores) {
        this.scores = scores;
        this.levels.setLevelByScores(scores);
        int level = levels.getCurrentLevel();
        int scoresToNext = levels.getScoresToNext();
        this.setText(String.format(
            "SCORES:\n"
            + "%d\n"
            + "LEVEL\n"
            + "%d\n"
            + "TO NEXT\n"
            + "%d\n",
            this.scores, level, scoresToNext));
    }

    public int getScores() {
        return scores;
    }

    public void resetResults() {
        setScores(0);
    }
}
