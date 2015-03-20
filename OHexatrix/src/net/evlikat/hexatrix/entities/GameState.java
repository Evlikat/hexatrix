package net.evlikat.hexatrix.entities;

import net.evlikat.hexatrix.axial.AxialPosition;

import java.util.*;

/**
 * Created by RSProkhorov on 18.03.2015.
 */
abstract class GameState {

    protected final HexagonalField hexagonalField;
    protected final GameEventCallback gameEventCallback;

    public GameState(HexagonalField hexagonalField, GameEventCallback gameEventCallback) {
        this.hexagonalField = hexagonalField;
        this.gameEventCallback = gameEventCallback;
    }

    public abstract GameState next();

    public abstract void cancel();
}

class FallingBlocksState extends GameState {

    private long framesPassed = 0;

    public FallingBlocksState(HexagonalField hexagonalField, GameEventCallback gameEventCallback) {
        super(hexagonalField, gameEventCallback);
    }

    @Override
    public GameState next() {
        framesPassed++;
        if (framesPassed >= gameEventCallback.framesPerTick()) {
            GameState state = tick();
            framesPassed = 0;
            return state;
        }
        return this;
    }

    private GameState tick() {
        if (hexagonalField.getFloatFigure() == null) {
            return this;
        }
        if (hexagonalField.fall()) {
            return this;
        }
        List<Integer> lineNumbersToRemove = hexagonalField.calcLinesToRemove();
        if (lineNumbersToRemove.isEmpty()) {
            return hexagonalField.onFigureDropped(0) ? this : new GameFinishedState(hexagonalField, gameEventCallback);
        }
        List<List<Hexagon>> hexagonLines = new ArrayList<>(lineNumbersToRemove.size());
        for (Integer lineNumber : lineNumbersToRemove) {
            hexagonLines.add(hexagonalField.getHexagons(lineNumber));
        }
        return new RemovingLinesState(hexagonalField, gameEventCallback, hexagonLines);
    }


    @Override
    public void cancel() {
        this.framesPassed = gameEventCallback.framesPerTick() / 2;
    }
}

class RemovingLinesState extends GameState {

    private static final int FRAMES_PER_BLOCK = 3;

    private int framesPassed = 0;
    private final CompositeIterator<Hexagon> compositeIterator;
    private Integer currentLineIndex = null;
    private int linesRemoved;
    private Map<Integer, Integer> lastDemarkationPoints = null;

    public RemovingLinesState(final HexagonalField hexagonalField, GameEventCallback gameEventCallback, List<List<Hexagon>> hexagonLinesToRemove) {
        super(hexagonalField, gameEventCallback);
        this.linesRemoved = hexagonLinesToRemove.size();
        this.compositeIterator = new CompositeIterator<Hexagon>(hexagonLinesToRemove.iterator()) {
            @Override
            public void afterLine() {
                if (lastDemarkationPoints != null) {
                    hexagonalField.dropAll(1, lastDemarkationPoints);
                }
            }
        };
    }

    private void putLastDemarkationPointsFor(AxialPosition position) {
        if (lastDemarkationPoints == null || position.getLine() != currentLineIndex) {
            // position is from new line
            lastDemarkationPoints = new LinkedHashMap<>();
            currentLineIndex = position.getLine();
        }
        lastDemarkationPoints.put(position.getQ(), position.getR());
    }

    @Override
    public GameState next() {
        framesPassed++;
        if (framesPassed >= FRAMES_PER_BLOCK) {
            if (compositeIterator.hasNext()) {
                // while there is a block to remove we remove it
                Hexagon hexagon = compositeIterator.next();
                hexagonalField.removeField(hexagon.getPosition());
                putLastDemarkationPointsFor(hexagon.getPosition());
                return this;
            } else {
                // if there is no more block to remove we drop all other blocks
                if (lastDemarkationPoints != null) {
                    hexagonalField.dropAll(1, lastDemarkationPoints);
                }
                return hexagonalField.onFigureDropped(this.linesRemoved) ?
                        new FallingBlocksState(hexagonalField, gameEventCallback) :
                        new GameFinishedState(hexagonalField, gameEventCallback);
            }
        }
        return this;
    }

    @Override
    public void cancel() {
    }
}

class GameFinishedState extends GameState {

    private int framesPassed = 0;

    public GameFinishedState(HexagonalField hexagonalField, GameEventCallback gameEventCallback) {
        super(hexagonalField, gameEventCallback);
    }

    @Override
    public GameState next() {
        framesPassed++;
        if (framesPassed >= 50) {
            hexagonalField.onGameFinished();
            return new NullState(hexagonalField, gameEventCallback);
        }
        return this;
    }

    @Override
    public void cancel() {
    }
}

class NullState extends GameState {

    public NullState(HexagonalField hexagonalField, GameEventCallback gameEventCallback) {
        super(hexagonalField, gameEventCallback);
    }

    @Override
    public GameState next() {
        return this;
    }

    @Override
    public void cancel() {
    }
}
