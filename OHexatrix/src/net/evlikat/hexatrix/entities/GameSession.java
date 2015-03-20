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

    private int score = 0;
    private final Levels levels;

    public GameSession(Levels levels, float pX, float pY, IFont pFont, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pFont, "", 80, pVertexBufferObjectManager);
        setColor(Color.WHITE);
        this.levels = levels;
        setScore(0);
    }

    public int addScore(int linesRemoved) {
        setScore(score + (int) Math.pow(2, linesRemoved - 1));
        return score;
    }

    private void setScore(int score) {
        this.score = score;
        this.levels.setLevelByScore(score);
        int level = levels.getCurrentLevel();
        int scoreToNext = levels.getScoreToNext();
        this.setText(String.format(
            "SCORE\n"
            + "%d\n"
            + "LEVEL\n"
            + "%d\n"
            + "TO NEXT\n"
            + "%d\n",
            this.score, level, scoreToNext));
    }

    public int getScore() {
        return score;
    }

    public void resetResults() {
        setScore(0);
    }
}
