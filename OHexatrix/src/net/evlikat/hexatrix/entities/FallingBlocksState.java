package net.evlikat.hexatrix.entities;

import net.evlikat.hexatrix.axial.AxialDirection;
import net.evlikat.hexatrix.axial.MoveDirection;
import net.evlikat.hexatrix.axial.RotateDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RSProkhorov on 23.03.2015.
 */
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

    @Override
    public boolean turn(RotateDirection rotateDirection) {
        return hexagonalField.turnFigure(rotateDirection);
    }

    @Override
    public boolean move(MoveDirection direction, int steps) {
        AxialDirection axialDirection;
        if (direction == MoveDirection.LEFT) {
            axialDirection = AxialDirection.Left;
        } else if (direction == MoveDirection.RIGHT) {
            axialDirection = AxialDirection.RightBack;
        } else {
            throw new UnsupportedOperationException("Not recognized move direction");
        }
        return hexagonalField.moveFigure(axialDirection, steps);
    }

    @Override
    public boolean moving(MoveDirection direction, int steps) {
        return false;
    }
}
