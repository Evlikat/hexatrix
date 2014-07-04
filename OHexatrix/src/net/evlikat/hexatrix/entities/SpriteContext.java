package net.evlikat.hexatrix.entities;

import net.evlikat.hexatrix.Textures;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 04, 2014)
 */
public class SpriteContext {

    private final float size;
    private final Camera camera;
    private final Textures textures;
    private final VertexBufferObjectManager vertexBufferObjectManager;
    private final Engine engine;

    public SpriteContext(float size, Camera camera, Textures textures, Engine engine) {
        this.size = size;
        this.camera = camera;
        this.textures = textures;
        this.engine = engine;
        this.vertexBufferObjectManager = engine.getVertexBufferObjectManager();
    }

    public Engine getEngine() {
        return engine;
    }

    public float getSize() {
        return size;
    }

    public Camera getCamera() {
        return camera;
    }

    public Textures getTextures() {
        return textures;
    }

    public VertexBufferObjectManager getVertexBufferObjectManager() {
        return vertexBufferObjectManager;
    }
}
