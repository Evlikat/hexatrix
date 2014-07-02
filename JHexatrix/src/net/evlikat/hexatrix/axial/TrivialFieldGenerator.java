package net.evlikat.hexatrix.axial;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 01, 2014)
 */
public class TrivialFieldGenerator implements InitialFieldGenerator {

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

    public Collection<AxialPosition> generate() {
        return Collections.emptyList();
    }

}
