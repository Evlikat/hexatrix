package net.evlikat.hexatrix.entities;

import java.util.Collection;
import net.evlikat.hexatrix.axial.AxialDirection;
import net.evlikat.hexatrix.axial.AxialPosition;
import net.evlikat.hexatrix.axial.RotateDirection;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
public interface IFigure {

    AxialPosition getPosition();

    boolean move(Collection<AxialPosition> forbiddenPositions, AxialDirection direction);

    void turn(Collection<AxialPosition> forbiddenPositions, RotateDirection direction);

    Collection<AxialPosition> getPartsPositions();

    Collection<AxialPosition> getParts();
}
