package net.evlikat.hexatrix.entities;

/**
 * Created by RSProkhorov on 18.03.2015.
 */
abstract class GameState {

    public abstract GameState next();

    public abstract void cancel();
}

class FallingBlocksState extends GameState {

    private final IDiscreteTime discreteTime;
    private final GameEventCallback gameEventCallback;
    private long framesPassed = 0;

    public FallingBlocksState(IDiscreteTime discreteTime, GameEventCallback gameEventCallback) {
        this.discreteTime = discreteTime;
        this.gameEventCallback = gameEventCallback;
    }

    @Override
    public GameState next() {
        framesPassed++;
        if (framesPassed >= gameEventCallback.framesPerTick()) {
            boolean tick = discreteTime.tick();
            framesPassed = 0;
            //if (tick) {
                return this;
            //} else {
            //    return new RemovingLinesState();
            //}
        }
        return this;
    }

    @Override
    public void cancel() {
        this.framesPassed = 0;
    }
}

class RemovingLinesState extends GameState {

    private int framesPassed = 0;

    @Override
    public GameState next() {
        return null;
    }

    @Override
    public void cancel() {

    }
}
