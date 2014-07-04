package net.evlikat.hexatrix.entities;

import net.evlikat.hexatrix.axial.AxialPosition;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
public abstract class AxialEntity extends Sprite {

    public static final float SQ3 = (float) Math.sqrt(3);
    protected final SpriteContext spriteContext;

    public AxialEntity(
        float pX, float pY,
        float width, float height,
        ITextureRegion pTextureRegion,
        SpriteContext spriteContext
    ) {
        super(pX, pY, width, height, pTextureRegion, spriteContext.getVertexBufferObjectManager());
        this.spriteContext = spriteContext;
    }

    protected static float getX(float size, AxialPosition position) {
        final float q = position.getQ();
        return (size * 3 / 2 * q);
    }

    protected static float getY(float size, AxialPosition position) {
        final float q = position.getQ();
        final float r = position.getR();
        return -size * SQ3 * (r + q / 2);
    }

    public void onMoved(float q, float r) {
        final float size = spriteContext.getSize();
        final float x = (size * 3 / 2 * q);
        final float y = -size * SQ3 * (r + q / 2);
        setPosition(x, y);
    }

    public void onMoved(AxialPosition newPosition) {
        onMoved(newPosition.getQ(), newPosition.getR());
    }

}
