package net.evlikat.hexatrix.entities;

import net.evlikat.hexatrix.axial.AxialPosition;
import org.andengine.opengl.texture.region.ITextureRegion;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
public class Hexagon extends AxialEntity {

    private AxialPosition position;

    public Hexagon(
        AxialPosition position,
        ITextureRegion pTextureRegion,
        SpriteContext spriteContext
    ) {
        super(
            getX(spriteContext.getSize(), position),
            getY(spriteContext.getSize(), position),
            spriteContext.getSize() * 2, spriteContext.getSize() * SQ3,
            pTextureRegion, spriteContext);
        this.position = position;
    }

    public AxialPosition getPosition() {
        return position;
    }

    public void setPosition(AxialPosition position) {
        this.position = position;
        onMoved(position);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.position != null ? this.position.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Hexagon other = (Hexagon) obj;
        if (this.position != other.position && (this.position == null || !this.position.equals(other.position))) {
            return false;
        }
        return true;
    }
}
