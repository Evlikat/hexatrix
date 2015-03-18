package net.evlikat.hexatrix.axial;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author Roman Prokhorov
 * @version 1.0 (Jun 27, 2014)
 */
public class RandomFigureGenerator implements FigureGenerator {

    public static final AxialFigure STICK = new AxialFigure(
            Arrays.asList(
                    new AxialPosition(0, 1),
                    new AxialPosition(0, -1),
                    new AxialPosition(0, -2))
    );
    private static final AxialFigure LEFT_LIGHTNING = new AxialFigure(
            Arrays.asList(
                    new AxialPosition(0, 1),
                    new AxialPosition(-1, 0),
                    new AxialPosition(-1, -1))
    );
    private static final AxialFigure RIGHT_LIGHTNING = new AxialFigure(
            Arrays.asList(
                    new AxialPosition(0, 1),
                    new AxialPosition(1, -1),
                    new AxialPosition(1, -2))
    );
    private static final AxialFigure LEFT_LEG = new AxialFigure(
            Arrays.asList(
                    new AxialPosition(0, 1),
                    new AxialPosition(0, -1),
                    new AxialPosition(-1, -1))
    );
    private static final AxialFigure RIGHT_LEG = new AxialFigure(
            Arrays.asList(
                    new AxialPosition(0, 1),
                    new AxialPosition(0, -1),
                    new AxialPosition(1, -2))
    );
    private static final AxialFigure LEFT_BOLT = new AxialFigure(
            Arrays.asList(
                    new AxialPosition(0, 1),
                    new AxialPosition(0, -1),
                    new AxialPosition(-1, 0))
    );
    private static final AxialFigure RIGHT_BOLT = new AxialFigure(
            Arrays.asList(
                    new AxialPosition(0, 1),
                    new AxialPosition(0, -1),
                    new AxialPosition(1, -1))
    );
    private static final AxialFigure SQUARE = new AxialFigure(
            Arrays.asList(
                    new AxialPosition(1, -1),
                    new AxialPosition(-1, 0),
                    new AxialPosition(0, -1))
    );
    private static final AxialFigure STAR = new AxialFigure(
            Arrays.asList(
                    new AxialPosition(0, 1),
                    new AxialPosition(-1, 0),
                    new AxialPosition(1, -1))
    );
    private static final AxialFigure RAINBOW = new AxialFigure(
            Arrays.asList(
                    new AxialPosition(0, 1),
                    new AxialPosition(-1, 1),
                    new AxialPosition(1, 1))
    );

    private static final List<AxialFigure> FIGURES = Arrays.asList(
            LEFT_LIGHTNING,
            RIGHT_LIGHTNING,
            STICK,
            LEFT_LEG,
            RIGHT_LEG,
            LEFT_BOLT,
            RIGHT_BOLT,
            SQUARE,
            STAR,
            RAINBOW);

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

    public void reset() {
        next = FIGURES.get(rnd.nextInt(FIGURES.size()));
    }
}
