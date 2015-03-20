package net.evlikat.hexatrix.views;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 15, 2014)
 */
public class Levels {

    /**
     * Scores to next level -> milliseconds per tick
     */
    private static final LinkedHashMap<Integer, Integer> LEVELS = new LinkedHashMap<Integer, Integer>() {
        {
            put(0, 52);
            put(30, 50);
            put(60, 48);
            put(100, 46);
            put(150, 44);
            put(200, 42);
            put(275, 40);
            put(350, 38);
            put(500, 36);
            put(650, 34);
            put(800, 32);
            put(1000, 30);
            put(1200, 28);
            put(1500, 26);
            put(1750, 24);
            put(2000, 22);
            put(2500, 20);
            put(99999, 20);
        }
    };

    private Iterator<Map.Entry<Integer, Integer>> iterator = LEVELS.entrySet().iterator();
    private int currentLevelNum = 0;
    private Map.Entry<Integer, Integer> currentLevel;
    private Map.Entry<Integer, Integer> nextLevel;

    public Levels() {
        this.currentLevel = iterator.next();
        this.nextLevel = iterator.next();
    }

    public void reset() {
        this.currentLevelNum = 0;
        this.iterator = LEVELS.entrySet().iterator();
        this.currentLevel = iterator.next();
        this.nextLevel = iterator.next();
    }

    public long framesPerTick() {
        return currentLevel.getValue();
    }

    public void setLevelByScore(int totalScore) {
        if (totalScore >= nextLevel.getKey()) {
            this.currentLevel = this.nextLevel;
            this.currentLevelNum++;
            this.nextLevel = iterator.hasNext() ? iterator.next() : this.nextLevel;
        }
    }

    public int getCurrentLevel() {
        return currentLevelNum;
    }

    public int getScoreToNext() {
        return nextLevel.getKey();
    }
}
