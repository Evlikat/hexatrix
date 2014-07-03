package net.evlikat.hexatrix.axial;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 01, 2014)
 */
public class GameSession {

    private int scores = 0;
    private int framesLeft = 0;

    public void addScores(int linesRemoved) {
        scores += (int) Math.pow(2, linesRemoved - 1);
    }
    
    public void frameLeft() {
        framesLeft++;
    }
    
    public void resetFrames() {
        framesLeft = 0;
    }

    public int getFramesLeft() {
        return framesLeft;
    }

    public int getScores() {
        return scores;
    }
}
