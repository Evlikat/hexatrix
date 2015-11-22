package net.evlikat.hexatrix.entities;

import net.evlikat.hexatrix.axial.AxialPosition;
import net.evlikat.hexatrix.axial.MoveDirection;
import net.evlikat.hexatrix.axial.RotateDirection;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by RSProkhorov on 23.03.2015.
 */
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

    @Override
    public boolean turn(RotateDirection rotateDirection) {
        return false;
    }

    @Override
    public boolean move(MoveDirection direction, int steps) {
        return false;
    }

    @Override
    public boolean moving(MoveDirection direction, int steps) {
        return false;
    }
}
