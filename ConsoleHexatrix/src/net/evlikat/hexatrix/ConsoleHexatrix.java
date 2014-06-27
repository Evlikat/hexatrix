package net.evlikat.hexatrix;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import net.evlikat.hexatrix.axial.AxialPosition;
import net.evlikat.hexatrix.axial.MoveDirection;
import net.evlikat.hexatrix.axial.RandomFigureGenerator;
import net.evlikat.hexatrix.axial.RotateDirection;

/**
 *
 * @author RSProkhorov
 */
public class ConsoleHexatrix {

    static Collection<AxialPosition> initialFields = Arrays.asList(
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

    public static void main(String[] args) {
        Physics controller = new Physics(initialFields);
        controller.setGenerator(new RandomFigureGenerator());
        ConsoleGraphics cg = new ConsoleGraphics(controller);
        boolean running = true;
        while (running) {
            try {
                cg.update();
                char key = (char) System.in.read();
                switch (key) {
                    case 'j':
                        controller.move(MoveDirection.LEFT);
                        break;
                    case 'l':
                        controller.move(MoveDirection.RIGHT);
                        break;
                    case 'z':
                        controller.turn(RotateDirection.LEFT);
                        break;
                    case 'c':
                        controller.turn(RotateDirection.RIGHT);
                        break;
                    case 'k':
                        controller.tick();
                        break;
                    case ' ':
                        controller.drop();
                        break;
                    case 'q':
                        running = false;
                        break;
                    default:
                        break;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
