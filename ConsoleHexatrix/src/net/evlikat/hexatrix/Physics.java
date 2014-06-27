package net.evlikat.hexatrix;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.evlikat.hexatrix.axial.AxialFigure;
import net.evlikat.hexatrix.axial.AxialHexagonalField;
import net.evlikat.hexatrix.axial.AxialPosition;
import net.evlikat.hexatrix.axial.IFigureGenerator;
import net.evlikat.hexatrix.axial.MoveDirection;
import net.evlikat.hexatrix.axial.RotateDirection;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jun 27, 2014)
 */
public class Physics {

    private final AxialHexagonalField field;
    private final AxialPosition originPosition;
    private IFigureGenerator generator;

    public Physics(Collection<AxialPosition> initialFields) {
        field = AxialHexagonalField.generateJar(7, 15);
        field.addFields(initialFields);
        int originQ = field.getWidth() / 2;
        int originR = field.getDepth() - 3 - (originQ / 2);
        originPosition = new AxialPosition(originQ, originR);
    }

    public IFigureGenerator getGenerator() {
        return generator;
    }

    public void setGenerator(IFigureGenerator generator) {
        this.generator = generator;
    }

    public int getWidth() {
        return field.getWidth();
    }

    public int getDepth() {
        return field.getDepth();
    }

    public AxialHexagonalField getField() {
        return field;
    }

    public void tick() {
        boolean result = field.tick();
        if (result) {
            return;
        }
        OnFigureDropped();
    }

    private void OnFigureDropped() {
        // Convert old figure to hard block
        field.harden();
        // Clear full lines
        for (int x = 0; x < getDepth(); x++) {
            if (LineCompleted(x)) {
                dropAll(RemoveLine(x));
            }
        }
        // Generate new falling figure
        AxialFigure newFigure = generator.generate();
        newFigure.setPosition(new AxialPosition(originPosition));
        field.setFloatFigure(newFigure);
    }

    private boolean LineCompleted(int x) {
        for (int q = 0; q < getWidth(); q++) {
            if (!field.getFields().contains(new AxialPosition(q, x - q / 2))) {
                return false;
            }
        }
        return true;
    }

    private Map<Integer, Integer> RemoveLine(int x) {
        Map<Integer, Integer> demarkationPoints = new HashMap<Integer, Integer>();
        for (int q = 0; q < getWidth(); q++) {
            int axQ = q;
            int axR = x - q / 2;
            field.removeField(new AxialPosition(axQ, axR));
            demarkationPoints.put(axQ, axR);
        }
        return demarkationPoints;
    }

    private void dropAll(Map<Integer, Integer> demarkationPoints) {
        field.dropAll(demarkationPoints);
    }

    public void turn(RotateDirection direction) {
        field.turn(direction);
    }

    public void move(MoveDirection direction) {
        field.move(direction);
    }

    public void drop() {
        while (field.tick()) {
        }
        OnFigureDropped();
    }
}
