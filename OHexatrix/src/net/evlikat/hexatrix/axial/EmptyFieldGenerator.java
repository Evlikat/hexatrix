package net.evlikat.hexatrix.axial;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 01, 2014)
 */
public class EmptyFieldGenerator implements InitialFieldGenerator {

    static Collection<AxialPosition> INITIAL_FIELDS = Arrays.asList(
            new AxialPosition(0, 0),
            new AxialPosition(1, 0),
            new AxialPosition(2, -1),
            new AxialPosition(3, -1),
            //new AxialPosition(4, -2),
            new AxialPosition(5, -2),
            new AxialPosition(6, -3),
            new AxialPosition(7, -3),
            new AxialPosition(8, -4),
            new AxialPosition(9, -4),
            new AxialPosition(10, -5),

            new AxialPosition(0, 2),
            new AxialPosition(1, 2),
            new AxialPosition(2, 1),
            new AxialPosition(3, 1),
            //new AxialPosition(4, -1),
            new AxialPosition(5, 0),
            new AxialPosition(6, -1),
            new AxialPosition(7, -1),
            new AxialPosition(8, -2),
            new AxialPosition(9, -2),
            new AxialPosition(10, -3)
    );

    public Collection<AxialPosition> generate() {
        return Collections.emptyList();
    }

}
