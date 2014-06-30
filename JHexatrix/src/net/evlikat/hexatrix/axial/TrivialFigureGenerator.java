package net.evlikat.hexatrix.axial;

import java.util.Arrays;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jun 30, 2014)
 */
public class TrivialFigureGenerator implements IFigureGenerator {

    public AxialFigure generate() {
        return new AxialFigure(
            Arrays.asList(
                new AxialPosition(0, 1),
                new AxialPosition(1, 1),
                new AxialPosition(-1, 0)
            )
        );
    }

}
