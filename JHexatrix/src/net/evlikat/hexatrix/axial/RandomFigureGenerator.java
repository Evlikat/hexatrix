package net.evlikat.hexatrix.axial;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jun 27, 2014)
 */
public class RandomFigureGenerator implements FigureGenerator {

    private static final List<AxialFigure> FIGURES = Arrays.asList(
        // Left lightning
        new AxialFigure(
            Arrays.asList(
                new AxialPosition(0, 1),
                new AxialPosition(-1, 0),
                new AxialPosition(-1, -1))
        ),
        // Right lightning
        new AxialFigure(
            Arrays.asList(
                new AxialPosition(0, 1),
                new AxialPosition(1, -1),
                new AxialPosition(1, -2))
        ),
        // Stick
        new AxialFigure(
            Arrays.asList(
                new AxialPosition(0, 1),
                new AxialPosition(0, -1),
                new AxialPosition(0, -2))
        ),
        // Left leg
        new AxialFigure(
            Arrays.asList(
                new AxialPosition(0, 1),
                new AxialPosition(0, -1),
                new AxialPosition(-1, -1))
        ),
        // Right leg
        new AxialFigure(
            Arrays.asList(
                new AxialPosition(0, 1),
                new AxialPosition(0, -1),
                new AxialPosition(1, -2))
        ),
        // Left bolt
        new AxialFigure(
            Arrays.asList(
                new AxialPosition(0, 1),
                new AxialPosition(0, -1),
                new AxialPosition(-1, 0))
        ),
        // Right bolt
        new AxialFigure(
            Arrays.asList(
                new AxialPosition(0, 1),
                new AxialPosition(0, -1),
                new AxialPosition(1, -1))
        ),
        // Square
        new AxialFigure(
            Arrays.asList(
                new AxialPosition(1, -1),
                new AxialPosition(-1, 0),
                new AxialPosition(0, -1))
        ),
        // Star
        new AxialFigure(
            Arrays.asList(
                new AxialPosition(0, 1),
                new AxialPosition(-1, 0),
                new AxialPosition(1, -1))
        )
    );
    private final Random rnd = new Random();
    private AxialFigure next = FIGURES.get(rnd.nextInt(FIGURES.size()));

    public AxialFigure generate() {
        AxialFigure current = next;
        next = FIGURES.get(rnd.nextInt(FIGURES.size()));
        return current;
    }

    public AxialFigure getNext() {
        return next;
    }
}
