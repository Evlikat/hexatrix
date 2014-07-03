package net.evlikat.hexatrix.entities;

import net.evlikat.hexatrix.axial.AxialPosition;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
public class Hexagon extends AxialEntity {

    private AxialPosition position;

    public Hexagon(
        AxialPosition position,
        float size,
        Camera camera,
        ITextureRegion pTextureRegion,
        VertexBufferObjectManager pVertexBufferObjectManager
    ) {
        super(size, camera,
            getX(size, position), getY(camera.getHeight(), size, position),
            size * 2, size * SQ3,
            pTextureRegion, pVertexBufferObjectManager);
        this.position = position;
    }

    public AxialPosition getPosition() {
        return position;
    }

    public void setPosition(AxialPosition position) {
        this.position = position;
        onMoved(position);
    }
}
