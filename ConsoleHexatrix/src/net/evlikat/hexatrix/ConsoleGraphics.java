package net.evlikat.hexatrix;

import java.lang.Console;
import java.io.IOException;
import net.evlikat.hexatrix.axial.AxialPosition;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jun 27, 2014)
 */
public class ConsoleGraphics {

    private final Physics Game;
    private final MyConsole console;

    int getHeight() {
        return (Game.getDepth() + 1) * 2 + 1;
    }

    int getWidth() {
        return (Game.getWidth() + 2) * 2;
    }

    public ConsoleGraphics(Physics Game) {
        this.Game = Game;
        this.console = new MyConsole();
    }

    public void update() {
        console.clear();
        drawStackBorders();
        drawFields();
        drawFigure();
    }

    private void drawFigure() {
        if (Game.getField().getFloatFigure() == null) {
            return;
        }
        console.setForegroundColor(MyConsole.RED);
        for (AxialPosition field : Game.getField().getFloatFigure().getPartsPositions()) {
            DrawBlock(field);
        }
    }

    private void drawFields() {
        console.setForegroundColor(MyConsole.GREEN);
        for (AxialPosition field : Game.getField().getFields()) {
            DrawBlock(field);
        }
    }

    private void drawStackBorders() {
        console.setForegroundColor(MyConsole.GRAY);
        for (AxialPosition field : Game.getField().getBorders()) {
            DrawBlock(field);
        }
    }

    private void DrawBlock(AxialPosition position) {
        int x = (position.getQ() + 1) * 2;
        int y = getHeight() - (position.getR() + 2) * 2 - position.getQ();
        console.writeAt("╔╗", x, y);
        console.writeAt("╚╝", x, y + 1);
    }
}

class MyConsole {

    public final static String RED = "\u001B[31m";
    public final static String GREEN = "\u001B[32m";
    public final static String GRAY = "\u001B[0m";

    private String currentColor;
    private Console console;

    void clear() {
        console.flush();
    }

    void writeAt(String string, int x, int y) {
        console.
        System.out.print(this.currentColor + string);
    }

    void setForegroundColor(String color) {
        this.currentColor = color;
    }
}
