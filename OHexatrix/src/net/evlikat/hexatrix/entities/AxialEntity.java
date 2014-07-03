package net.evlikat.hexatrix.entities;

import android.util.Log;
import net.evlikat.hexatrix.axial.AxialPosition;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
public abstract class AxialEntity extends Sprite {

    protected static final float SQ3 = (float) Math.sqrt(3);

    private final float size;
    private final Camera camera;

    public AxialEntity(float size,
        Camera camera,
        float pX, float pY,
        float width, float height,
        ITextureRegion pTextureRegion,
        VertexBufferObjectManager pVertexBufferObjectManager
    ) {
        super(pX, pY, width, height, pTextureRegion, pVertexBufferObjectManager);
        this.size = size;
        this.camera = camera;
    }

    protected static float getX(float size, AxialPosition position) {
        final float q = position.getQ();
        return (size * 3 / 2 * q) + (size * 2);
    }

    protected static float getY(float cameraHeight, float size, AxialPosition position) {
        final float q = position.getQ();
        final float r = position.getR();
        return cameraHeight - size * SQ3 * (r + q / 2) - (size * 2 * SQ3);
    }

    public void onMoved(float q, float r) {
        final float x = (size * 3 / 2 * q) + (size * 2);
        final float y = camera.getHeight() - size * SQ3 * (r + q / 2) - (size * 2 * SQ3);
        setPosition(x, y);
    }

    public void onMoved(AxialPosition newPosition) {
        onMoved(newPosition.getQ(), newPosition.getR());
    }
}
