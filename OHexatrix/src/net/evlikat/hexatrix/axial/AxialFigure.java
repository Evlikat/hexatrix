package net.evlikat.hexatrix.axial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jun 27, 2014)
 */
public class AxialFigure {

    private static interface CubeRotator {

        CubeCoordinates turn(CubeCoordinates xyz);
    }

    public AxialPosition position = new AxialPosition(0, 0);

    public Collection<AxialPosition> parts;

    public Collection<AxialPosition> getPartsPositions() {
        List<AxialPosition> result = new ArrayList<AxialPosition>();
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
        parts = new ArrayList<AxialPosition>();
    }

    public AxialFigure(Collection<AxialPosition> relativeParts) {
        parts = relativeParts;
    }

    public AxialFigure(AxialPosition position, Collection<AxialPosition> absoluteParts) {
        this.position = position;
        parts = ToRelatives(position, absoluteParts);
    }

    public AxialPosition getPosition() {
        return position;
    }

    public void setPosition(AxialPosition position) {
        this.position = position;
    }

    public void turn(Collection<AxialPosition> forbiddenPositions, RotateDirection direction) {
        if (direction == RotateDirection.LEFT) {
            turn(forbiddenPositions, new CubeRotator() {

                public CubeCoordinates turn(CubeCoordinates xyz) {
                    return new CubeCoordinates(-xyz.getZ(), -xyz.getX(), -xyz.getY());
                }
            });
        } else if (direction == RotateDirection.RIGHT) {
            turn(forbiddenPositions, new CubeRotator() {

                public CubeCoordinates turn(CubeCoordinates xyz) {
                    return new CubeCoordinates(-xyz.getY(), -xyz.getZ(), -xyz.getX());
                }
            });
        } else {
            throw new UnsupportedOperationException("Not implemented direction");
        }
    }

    public boolean move(Collection<AxialPosition> forbiddenPositions, AxialDirection direction) {
        AxialPosition newPosition = new AxialPosition(position.getQ() + direction.getDq(), position.getR() + direction.getDr());
        if (forbiddenPositions.contains(newPosition)) {
            // Figure center can't be moved
            return false;
        }
        for (AxialPosition partPos : parts) {
            if (forbiddenPositions.contains(partPos.relatedTo(newPosition))) {
                // Figure can't be moved
                return false;
            }
        }
        position = newPosition;
        return true;
    }

    private void turn(Collection<AxialPosition> forbiddenPositions, CubeRotator turner) {
        Collection<AxialPosition> newPartPositions = new ArrayList<AxialPosition>();
        for (AxialPosition partPos : parts) {
            AxialPosition newPos = From(turner.turn(From(partPos)));
            if (forbiddenPositions.contains(newPos.relatedTo(position))) {
                // Figure can't be turned
                return;
            }
            newPartPositions.add(newPos);
        }
        // If all positions are allowed then turn the figure
        parts = newPartPositions;
    }

    private static Collection<AxialPosition> ToRelatives(AxialPosition position, Collection<AxialPosition> absoluteParts) {
        Collection<AxialPosition> relatives = new ArrayList<AxialPosition>();
        for (AxialPosition absolutePosition : absoluteParts) {
            relatives.add(new AxialPosition(absolutePosition.getQ() - position.getQ(), absolutePosition.getR() - position.getR()));
        }
        return relatives;
    }

    private static AxialPosition From(CubeCoordinates cubeCoords) {
        return new AxialPosition(cubeCoords.getX(), cubeCoords.getZ());
    }

    private static CubeCoordinates From(AxialPosition axialCoords) {
        return new CubeCoordinates(axialCoords.getQ(), -axialCoords.getQ() - axialCoords.getR(), axialCoords.getR());
    }
}
