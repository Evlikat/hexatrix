package net.evlikat.hexatrix;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.evlikat.hexatrix.axial.AxialFigure;
import net.evlikat.hexatrix.axial.AxialHexagonalField;
import net.evlikat.hexatrix.axial.AxialPosition;
import net.evlikat.hexatrix.axial.FigureGenerator;
import net.evlikat.hexatrix.axial.InitialFieldGenerator;
import net.evlikat.hexatrix.axial.MoveDirection;
import net.evlikat.hexatrix.axial.RotateDirection;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jun 27, 2014)
 */
public class Physics {

    private static class Level {

        private final int framesPerTick;
        private final int scoresBarrier;

        public Level(int framesPerTick, int scoresBarrier) {
            this.framesPerTick = framesPerTick;
            this.scoresBarrier = scoresBarrier;
        }
    }

    private static final List<Level> LEVELS = Arrays.asList(
        new Level(50, 10),
        new Level(47, 20),
        new Level(44, 30),
        new Level(41, 50),
        new Level(38, 70),
        new Level(35, 90),
        new Level(32, 120),
        new Level(29, 150),
        new Level(26, 200),
        new Level(23, 250),
        new Level(20, 300),
        new Level(17, 375),
        new Level(14, 450),
        new Level(11, 550),
        new Level(10, Integer.MAX_VALUE)
    );
    private final AxialHexagonalField field;
    private final AxialPosition originPosition;
    private FigureGenerator figureGenerator;
    private final InitialFieldGenerator fieldGenerator;
    private GameSession gameSession;
    private volatile boolean active = true;
    private int currentLevel = 0;

    public Physics(InitialFieldGenerator fieldGenerator) {
        this.fieldGenerator = fieldGenerator;
        this.gameSession = new GameSession();
        field = AxialHexagonalField.generateJar(9, 21);
        field.addFields(this.fieldGenerator.generate());
        int originQ = field.getWidth() / 2;
        int originR = field.getDepth() - 3 - (originQ / 2);
        originPosition = new AxialPosition(originQ, originR);
    }

    public FigureGenerator getFigureGenerator() {
        return figureGenerator;
    }

    public void setFigureGenerator(FigureGenerator generator) {
        this.figureGenerator = generator;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getWidth() {
        return field.getWidth();
    }

    public int getDepth() {
        return field.getDepth();
    }

    public AxialHexagonalField getField() {
        return field;
    }

    public AxialFigure getNextFigure() {
        return figureGenerator.getNext();
    }

    public boolean isActive() {
        return active;
    }

    public void tick() {
        if (!active) {
            return;
        }
        boolean result = field.tick();
        if (result) {
            return;
        }
        onFigureDropped();
    }

    private void dropAll(Map<Integer, Integer> demarkationPoints) {
        if (!active) {
            return;
        }
        field.dropAll(demarkationPoints);
    }

    public void turn(RotateDirection direction) {
        if (!active) {
            return;
        }
        field.turn(direction);
    }

    public void move(MoveDirection direction) {
        if (!active) {
            return;
        }
        field.move(direction);
    }

    public void drop() {
        if (!active) {
            return;
        }
        while (field.tick()) {
        }
        onFigureDropped();
    }

    public void reset() {
        field.clear();
        field.addFields(fieldGenerator.generate());
        active = true;
        gameSession = new GameSession();
        currentLevel = 0;
    }

    private void onFigureDropped() {
        // Convert old figure to hard block
        field.harden();
        // Clear full lines
        int linesRemoved = 0;
        for (int x = 0; x < getDepth(); x++) {
            if (lineCompleted(x)) {
                dropAll(removeLine(x));
                linesRemoved++;
            }
        }
        if (linesRemoved > 0) {
            gameSession.addScores(linesRemoved);
            currentLevel = calculateCurrentLevel();
        }
        // Generate new falling figure
        AxialFigure newFigure = figureGenerator.generate();
        newFigure.setPosition(new AxialPosition(originPosition));
        boolean figureSet = field.setFloatFigure(newFigure);
        if (!figureSet) {
            active = false;
        }
    }

    private int calculateCurrentLevel() {
        int scores = gameSession.getScores();
        for (int i = 0; i < LEVELS.size(); i++) {
            Level level = LEVELS.get(i);
            if (level.scoresBarrier > scores) {
                return i;
            }
        }
        return LEVELS.size() - 1;
    }

    private boolean lineCompleted(int x) {
        for (int q = 0; q < getWidth(); q++) {
            if (!field.getFields().contains(new AxialPosition(q, x - q / 2))) {
                return false;
            }
        }
        return true;
    }

    private Map<Integer, Integer> removeLine(int x) {
        Map<Integer, Integer> demarkationPoints = new HashMap<Integer, Integer>();
        for (int q = 0; q < getWidth(); q++) {
            int axQ = q;
            int axR = x - q / 2;
            field.removeField(new AxialPosition(axQ, axR));
            demarkationPoints.put(axQ, axR);
        }
        return demarkationPoints;
    }

    public void update() {
        gameSession.frameLeft();
        if (gameSession.getFramesLeft() == LEVELS.get(currentLevel).framesPerTick) {
            tick();
            gameSession.resetFrames();
        }
    }
}
