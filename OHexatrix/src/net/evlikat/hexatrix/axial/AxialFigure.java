package net.evlikat.hexatrix.axial;

import net.evlikat.hexatrix.entities.IFigure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Logical hexagonal figure
 * @author Roman Prokhorov
 * @version 1.0 (Jun 27, 2014)
 */
public class AxialFigure implements IFigure {

    private static interface CubeRotator {

        CubeCoordinates turn(CubeCoordinates xyz);
    }

    public AxialPosition position = new AxialPosition(0, 0);

    public Collection<AxialPosition> parts;

    public Collection<AxialPosition> getPartsPositions() {
        List<AxialPosition> result = new ArrayList<>();
        result.add(position);
        for (AxialPosition partPos : parts) {
            result.add(new AxialPosition(partPos.getQ() + position.getQ(), partPos.getR() + position.getR()));
        }
        return result;
    }

    public Collection<AxialPosition> getParts() {
        return parts;
    }

    public AxialFigure() {
        parts = new ArrayList<>();
    }

    public AxialFigure(Collection<AxialPosition> relativeParts) {
        parts = relativeParts;
    }

    public AxialFigure(AxialPosition position, Collection<AxialPosition> absoluteParts) {
        this.position = position;
        parts = toRelatives(position, absoluteParts);
    }

    public AxialPosition getPosition() {
        return position;
    }

    public void setPosition(AxialPosition position) {
        this.position = position;
    }

    @Override
    public boolean turn(Collection<AxialPosition> forbiddenPositions, RotateDirection direction) {
        if (direction == RotateDirection.COUNTERCLOCKWISE) {
            return turn(forbiddenPositions, new CubeRotator() {

                public CubeCoordinates turn(CubeCoordinates xyz) {
                    return new CubeCoordinates(-xyz.getZ(), -xyz.getX(), -xyz.getY());
                }
            });
        } else if (direction == RotateDirection.CLOCKWISE) {
            return turn(forbiddenPositions, new CubeRotator() {

                public CubeCoordinates turn(CubeCoordinates xyz) {
                    return new CubeCoordinates(-xyz.getY(), -xyz.getZ(), -xyz.getX());
                }
            });
        } else {
            throw new UnsupportedOperationException("Not implemented direction");
        }
    }

    public boolean move(Collection<AxialPosition> forbiddenPositions, AxialDirection direction) {
        AxialPosition newPosition = new AxialPosition(position.getQ() + direction.getQ(), position.getR() + direction.getR());
        if (forbiddenPositions.contains(newPosition)) {
            // Figure center can't be moved
            return false;
        }
        for (AxialPosition partPos : parts) {
            if (forbiddenPositions.contains(partPos.plus(newPosition))) {
                // Figure can't be moved
                return false;
            }
        }
        position = newPosition;
        return true;
    }

    private boolean turn(Collection<AxialPosition> forbiddenPositions, CubeRotator turner) {
        Collection<AxialPosition> newPartPositions = new ArrayList<>();
        for (AxialPosition partPos : parts) {
            AxialPosition newPos = fromCubeToAxial(turner.turn(fromAxialToCube(partPos)));
            if (forbiddenPositions.contains(newPos.plus(position))) {
                // Figure can't be turned
                return false;
            }
            newPartPositions.add(newPos);
        }
        // If all positions are allowed then turn the figure
        parts = newPartPositions;
        return true;
    }

    private static Collection<AxialPosition> toRelatives(AxialPosition position, Collection<AxialPosition> absoluteParts) {
        Collection<AxialPosition> relatives = new ArrayList<>();
        for (AxialPosition absolutePosition : absoluteParts) {
            relatives.add(new AxialPosition(absolutePosition.getQ() - position.getQ(), absolutePosition.getR() - position.getR()));
        }
        return relatives;
    }

    private static AxialPosition fromCubeToAxial(CubeCoordinates cubeCoords) {
        return new AxialPosition(cubeCoords.getX(), cubeCoords.getZ());
    }

    private static CubeCoordinates fromAxialToCube(AxialPosition axialCoords) {
        return new CubeCoordinates(axialCoords.getQ(), -axialCoords.getQ() - axialCoords.getR(), axialCoords.getR());
    }
}
