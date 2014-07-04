package net.evlikat.hexatrix.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.evlikat.hexatrix.axial.AxialDirection;
import net.evlikat.hexatrix.axial.AxialPosition;
import net.evlikat.hexatrix.axial.RotateDirection;
import org.andengine.opengl.texture.region.ITextureRegion;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
public class Figure extends AxialEntity implements IFigure {

    private static interface Rotator {

        AxialPosition turn(AxialPosition qr);
    }
    private final Map<AxialPosition, Hexagon> parts;
    private AxialPosition center;

    public Figure(
        IFigure prototype,
        ITextureRegion pTextureRegion,
        SpriteContext spriteContext
    ) {
        super(
            getX(spriteContext.getSize(), prototype.getPosition()),
            getY(spriteContext.getSize(), prototype.getPosition()),
            spriteContext.getSize() * 2, spriteContext.getSize() * SQ3,
            pTextureRegion, spriteContext);
        this.center = prototype.getPosition();
        this.parts = new HashMap<AxialPosition, Hexagon>(prototype.getParts().size());
        for (AxialPosition position : prototype.getParts()) {
            final Hexagon hexagon = new Hexagon(position, pTextureRegion, spriteContext);
            addPart(position, hexagon);
            hexagon.setPosition(position);
        }
    }

    @Override
    public AxialPosition getPosition() {
        return center;
    }

    public Collection<AxialPosition> getParts() {
        return parts.keySet();
    }

    public Collection<Hexagon> getHexagons() {
        return parts.values();
    }

    public void setPosition(AxialPosition newPosition) {
        this.center = newPosition;
        onMoved(newPosition);
    }

    public final void addPart(AxialPosition relativePosition, Hexagon hexagon) {
        attachChild(hexagon);
        parts.put(relativePosition, hexagon);
    }

    public final void removePart(AxialPosition relativePosition) {
        Hexagon hexagon = parts.get(relativePosition);
        if (hexagon != null) {
            detachChild(hexagon);
            parts.remove(relativePosition);
        }
    }

    @Override
    public void turn(Collection<AxialPosition> forbiddenPositions, RotateDirection direction) {
        if (direction == RotateDirection.LEFT) {
            turn(forbiddenPositions, new Rotator() {

                public AxialPosition turn(AxialPosition pos) {
                    return new AxialPosition(-pos.getR(), pos.getQ() + pos.getR());
                }
            });
        } else if (direction == RotateDirection.RIGHT) {
            turn(forbiddenPositions, new Rotator() {

                public AxialPosition turn(AxialPosition pos) {
                    return new AxialPosition(pos.getQ() + pos.getR(), -pos.getQ());
                }
            });
        } else {
            throw new UnsupportedOperationException("Not implemented direction");
        }
    }

    @Override
    public boolean move(Collection<AxialPosition> forbiddenPositions, AxialDirection direction) {
        AxialPosition newFigurePosition = new AxialPosition(
            center.getQ() + direction.getDq(),
            center.getR() + direction.getDr()
        );
        if (forbiddenPositions.contains(newFigurePosition)) {
            // Figure center can't be moved
            return false;
        }
        for (AxialPosition partPos : parts.keySet()) {
            if (forbiddenPositions.contains(partPos.plus(newFigurePosition))) {
                // Figure can't be moved
                return false;
            }
        }
        setPosition(newFigurePosition);
        return true;
    }

    @Override
    public Collection<AxialPosition> getPartsPositions() {
        List<AxialPosition> result = new ArrayList<AxialPosition>();
        final AxialPosition centerPos = getPosition();
        result.add(centerPos);
        for (AxialPosition partPos : parts.keySet()) {
            int q = partPos.getQ();
            int r = partPos.getR();
            result.add(new AxialPosition(q + centerPos.getQ(), r + centerPos.getR()));
        }
        return result;
    }

    private void turn(Collection<AxialPosition> forbiddenPositions, Rotator rotator) {
        Map<Hexagon, AxialPosition> newPartPositions
            = new HashMap<Hexagon, AxialPosition>();
        for (Map.Entry<AxialPosition, Hexagon> entry : parts.entrySet()) {
            AxialPosition newPos = rotator.turn(entry.getKey());
            if (forbiddenPositions.contains(newPos.plus(center))) {
                // Figure can't be turned
                return;
            }
            newPartPositions.put(entry.getValue(), newPos);
        }
        // If all positions are allowed then turn the figure
        parts.clear();
        for (Map.Entry<Hexagon, AxialPosition> entry : newPartPositions.entrySet()) {
            Hexagon hexagon = entry.getKey();
            AxialPosition newPosition = entry.getValue();
            parts.put(newPosition, hexagon);
            hexagon.setPosition(newPosition);
        }
    }
}
