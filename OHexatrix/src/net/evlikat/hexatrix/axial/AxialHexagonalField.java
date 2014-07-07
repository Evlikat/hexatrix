package net.evlikat.hexatrix.axial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jun 27, 2014)
 */
public class AxialHexagonalField {

    // Fields
    Map<AxialPosition, AxialHexCell> borders = new HashMap<AxialPosition, AxialHexCell>();
    Map<AxialPosition, AxialHexCell> fields = new HashMap<AxialPosition, AxialHexCell>();

    // Properties
    public AxialFigure floatFigure = new AxialFigure();
    public final AxialDirection gravity;
    public final int width;
    public final int depth;

    public Collection<AxialPosition> getBorders() {
        return borders.keySet();
    }

    public Collection<AxialPosition> getFields() {
        return fields.keySet();
    }

    public int getWidth() {
        return width;
    }

    public int getDepth() {
        return depth;
    }

    public AxialFigure getFloatFigure() {
        return floatFigure;
    }

    public boolean setFloatFigure(AxialFigure floatFigure) {
        for (AxialPosition axialPosition : floatFigure.getPartsPositions()) {
            if (fields.containsKey(axialPosition)) {
                return false;
            }
        }
        this.floatFigure = floatFigure;
        return true;
    }

    public Collection<AxialPosition> getForbiddenFields() {
        ArrayList<AxialPosition> forbidden = new ArrayList<AxialPosition>();
        forbidden.addAll(borders.keySet());
        forbidden.addAll(fields.keySet());
        return forbidden;
    }

    private AxialHexagonalField(int width, int depth) {
        this.width = width;
        this.depth = depth;
        gravity = AxialDirection.Back;
    }

    public static AxialHexagonalField generateJar(int width, int depth) {
        AxialHexagonalField jar = new AxialHexagonalField(width, depth);
        // Set borders: right hexa-corner
        AxialPosition pos = newNeighbour(new AxialPosition(0, 0), AxialDirection.Left);
        AxialDirection[] directions = {AxialDirection.Right, AxialDirection.RightBack};
        for (int i = -1; i < width + 1; i++) {
            // root cell
            jar.borders.put(pos, new AxialHexCell());
            // stack cells for borders
            if (i == -1 || i == width) {
                AxialPosition stackPos = pos;
                for (int j = 1; j < depth; j++) {
                    stackPos = newNeighbour(stackPos, AxialDirection.Forward);
                    jar.borders.put(stackPos, new AxialHexCell());
                }
            }
            // next stack
            pos = newNeighbour(pos, directions[(i + 2) % directions.length]);
        }
        return jar;
    }

    private static AxialPosition newNeighbour(AxialPosition initPosition, AxialDirection direction) {
        return new AxialPosition(initPosition.getQ() + direction.getQ(), initPosition.getR() + direction.getR());
    }

    public boolean tick() {
        if (floatFigure != null) {
            return floatFigure.move(getForbiddenFields(), gravity);
        }
        return false;
    }

    public boolean turn(RotateDirection direction) {
        if (floatFigure != null) {
            floatFigure.turn(getForbiddenFields(), direction);
            return true;
        }
        return false;
    }

    public boolean move(MoveDirection direction) {
        if (floatFigure != null) {
            AxialDirection axialDirection;
            if (direction == MoveDirection.LEFT) {
                axialDirection = AxialDirection.Left;
            } else if (direction == MoveDirection.RIGHT) {
                axialDirection = AxialDirection.RightBack;
            } else {
                throw new UnsupportedOperationException("Not recognized move direction");
            }
            return floatFigure.move(getForbiddenFields(), axialDirection);
        }
        return false;
    }

    public void harden() {
        if (floatFigure == null) {
            return;
        }
        // Harden float figure
        for (AxialPosition position : floatFigure.getPartsPositions()) {
            fields.put(new AxialPosition(position), new AxialHexCell());
        }
    }

    public void addFields(Collection<AxialPosition> positions) {
        for (AxialPosition pos : positions) {
            if (!fields.containsKey(pos)) {
                fields.put(pos, new AxialHexCell());
            }
        }
    }

    public void removeField(AxialPosition position) {
        if (fields.containsKey(position)) {
            fields.remove(position);
        }
    }

    public void dropAll(Map<Integer, Integer> demarkationPositions) {
        if (fields.isEmpty()) {
            return;
        }

        List<AxialPosition> fallingPart = new ArrayList<AxialPosition>();
        for (AxialPosition pos : fields.keySet()) {
            if (pos.getR() > demarkationPositions.get(pos.getQ())) {
                fallingPart.add(pos);
            }
        }

        if (fallingPart.isEmpty()) {
            return;
        }
        AxialFigure fallingFigure = new AxialFigure(fallingPart.get(0), fallingPart);
        // move once
        Collection<AxialPosition> forbiddenFields = getForbiddenFields();
        forbiddenFields.removeAll(fallingPart);
        if (fallingFigure.move(forbiddenFields, gravity)) {
            for (AxialPosition fallenPart : fallingPart) {
                fields.remove(fallenPart);
            }
            addFields(fallingFigure.getPartsPositions());
        }
    }

    public void clear() {
        fields.clear();
        floatFigure = null;
    }

}
