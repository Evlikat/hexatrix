package net.evlikat.hexatrix.entities;

import net.evlikat.hexatrix.axial.MoveDirection;
import net.evlikat.hexatrix.axial.RotateDirection;

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

    // TODO: remove
    public abstract void cancel();

    public abstract boolean turn(RotateDirection rotateDirection);

    public abstract boolean move(MoveDirection direction);
}

