package net.evlikat.hexatrix;

import java.util.Arrays;
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

    static Collection<AxialPosition> INITIAL_FIELDS = Arrays.asList(
        new AxialPosition(0, 1),
        new AxialPosition(1, 1),
        new AxialPosition(2, 0),
        new AxialPosition(4, -1),
        new AxialPosition(5, -1),
        new AxialPosition(6, -2),
        //
        new AxialPosition(0, 2),
        new AxialPosition(1, 2),
        new AxialPosition(2, 1),
        new AxialPosition(4, 0),
        new AxialPosition(6, -1)
    );
    private final AxialHexagonalField field;
    private final AxialPosition originPosition;
    private IFigureGenerator generator;
    private volatile boolean active = true;

    public Physics() {
        this(INITIAL_FIELDS);
    }

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

    public boolean isActive() {
        return active;
    }

    public void tick() {
        if (!active) {
            return;
        }
        boolean result = field.tick();
        if (result) {
            return;
        }
        onFigureDropped();
    }

    private void dropAll(Map<Integer, Integer> demarkationPoints) {
        if (!active) {
            return;
        }
        field.dropAll(demarkationPoints);
    }

    public void turn(RotateDirection direction) {
        if (!active) {
            return;
        }
        field.turn(direction);
    }

    public void move(MoveDirection direction) {
        if (!active) {
            return;
        }
        field.move(direction);
    }

    public void drop() {
        if (!active) {
            return;
        }
        while (field.tick()) {
        }
        onFigureDropped();
    }
    
    public void reset() {
        field.clear();
        field.addFields(INITIAL_FIELDS);
        active = true;
    }

    private void onFigureDropped() {
        // Convert old figure to hard block
        field.harden();
        // Clear full lines
        for (int x = 0; x < getDepth(); x++) {
            if (lineCompleted(x)) {
                dropAll(removeLine(x));
            }
        }
        // Generate new falling figure
        AxialFigure newFigure = generator.generate();
        newFigure.setPosition(new AxialPosition(originPosition));
        boolean figureSet = field.setFloatFigure(newFigure);
        if (!figureSet) {
            active = false;
        }
    }

    private boolean lineCompleted(int x) {
        for (int q = 0; q < getWidth(); q++) {
            if (!field.getFields().contains(new AxialPosition(q, x - q / 2))) {
                return false;
            }
        }
        return true;
    }

    private Map<Integer, Integer> removeLine(int x) {
        Map<Integer, Integer> demarkationPoints = new HashMap<Integer, Integer>();
        for (int q = 0; q < getWidth(); q++) {
            int axQ = q;
            int axR = x - q / 2;
            field.removeField(new AxialPosition(axQ, axR));
            demarkationPoints.put(axQ, axR);
        }
        return demarkationPoints;
    }
}
