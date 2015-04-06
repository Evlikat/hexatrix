package net.evlikat.hexatrix.entities;

import net.evlikat.hexatrix.axial.MoveDirection;
import net.evlikat.hexatrix.axial.RotateDirection;

/**
 * Created by RSProkhorov on 23.03.2015.
 */
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
            return PauseState.create(this, hexagonalField, gameEventCallback);
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
