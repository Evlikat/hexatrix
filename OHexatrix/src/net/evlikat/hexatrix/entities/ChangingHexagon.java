package net.evlikat.hexatrix.entities;

import net.evlikat.hexatrix.axial.AxialPosition;
import org.andengine.opengl.texture.region.ITextureRegion;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
public class ChangingHexagon extends Hexagon {

    private final ITextureRegion hexagon1;

    public ChangingHexagon(
        AxialPosition position,
        ITextureRegion hexagon0,
        ITextureRegion hexagon1,
        SpriteContext spriteContext
    ) {
        super(position, hexagon0, spriteContext);
        this.hexagon1 = hexagon1;
    }

    @Override
    public ITextureRegion getTextureRegion() {
        if (getPosition() == null || getPosition().getLine() % 2 == 0) {
            return super.getTextureRegion();
        }
        return hexagon1;
    }

}
