package net.evlikat.hexatrix.entities;

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

    public GameSession(float pX, float pY, IFont pFont, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pFont, "SCORES:", pVertexBufferObjectManager);
        setColor(Color.WHITE);
    }

    public void addScores(int linesRemoved) {
        setScores(scores + (int) Math.pow(2, linesRemoved - 1));
    }

    private void setScores(int scores) {
        this.scores = scores;
        this.setText("SCORES:\n" + scores);
    }

    public int getScores() {
        return scores;
    }

    public void resetResults() {
        scores = 0;
    }
}
