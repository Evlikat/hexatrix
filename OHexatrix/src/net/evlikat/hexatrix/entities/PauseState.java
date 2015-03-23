package net.evlikat.hexatrix.entities;

import net.evlikat.hexatrix.axial.MoveDirection;
import net.evlikat.hexatrix.axial.RotateDirection;

/**
 * Created by RSProkhorov on 23.03.2015.
 */
class PauseState extends GameState {

    private final GameState prevState;
    private GameState nextState = null;

    private PauseState(GameState prevState, HexagonalField hexagonalField, GameEventCallback gameEventCallback) {
        super(hexagonalField, gameEventCallback);
        this.prevState = prevState;
    }

    public static PauseState create(GameState prevState, HexagonalField hexagonalField, GameEventCallback gameEventCallback) {
        if (prevState instanceof PauseState) {
            // to avoid nested pause states
            return (PauseState) prevState;
        }
        return new PauseState(prevState, hexagonalField, gameEventCallback);
    }

    @Override
    public GameState next() {
        if (nextState != null) {
            hexagonalField.setVisibilityForPause(false);
            return nextState;
        }
        hexagonalField.setVisibilityForPause(true);
        return this;
    }

    @Override
    public void cancel() {
    }

    @Override
    public boolean turn(RotateDirection rotateDirection) {
        // next update restores pre-pause state
        nextState = prevState;
        return false;
    }

    @Override
    public boolean move(MoveDirection direction) {
        // next update restores pre-pause state
        nextState = prevState;
        return false;
    }
}
