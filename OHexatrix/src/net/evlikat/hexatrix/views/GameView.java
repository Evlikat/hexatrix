package net.evlikat.hexatrix.views;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
public abstract class GameView {

    protected final Scene scene;
    protected final Engine engine;
    protected final Camera camera;

    public GameView(Engine engine, Camera camera) {
        this.engine = engine;
        this.camera = camera;
        this.scene = new Scene();
    }

    public Scene getScene() {
        return scene;
    }

    public abstract void populate();

    public abstract void update();
}
