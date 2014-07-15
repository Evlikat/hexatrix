package net.evlikat.hexatrix.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.evlikat.hexatrix.Textures;
import net.evlikat.hexatrix.axial.AxialDirection;
import net.evlikat.hexatrix.axial.AxialFigure;
import net.evlikat.hexatrix.axial.AxialPosition;
import net.evlikat.hexatrix.axial.FigureGenerator;
import net.evlikat.hexatrix.axial.MoveDirection;
import net.evlikat.hexatrix.axial.RandomFigureGenerator;
import net.evlikat.hexatrix.axial.RotateDirection;
import net.evlikat.hexatrix.views.Levels;
import org.andengine.engine.Engine;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;

import static net.evlikat.hexatrix.entities.AxialEntity.SQ3;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.color.Color;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
public class HexagonalField extends Entity implements IHexagonalField {

    private static final Collection<AxialPosition> TEST_INITIAL_FIELDS = Arrays.asList(
        new AxialPosition(0, 0),
        new AxialPosition(1, 0),
        new AxialPosition(2, -1),
        new AxialPosition(3, -1),
        //new AxialPosition(4, -2),
        new AxialPosition(5, -2),
        new AxialPosition(6, -3),
        new AxialPosition(7, -3),
        new AxialPosition(8, -4)
    //
    //        new AxialPosition(0, 1),
    //        new AxialPosition(1, 1),
    //        new AxialPosition(2, 0),
    //        new AxialPosition(3, 0),
    //        //new AxialPosition(4, -1),
    //        new AxialPosition(5, -1),
    //        new AxialPosition(6, -2),
    //        new AxialPosition(7, -2),
    //        new AxialPosition(8, -3)
    );

    private final Fields borders = new Fields();
    private final Fields fields = new Fields();
    private final List<Hexagon> bottomFields = new ArrayList<Hexagon>();
    // Properties
    private Figure floatFigure;
    private final Figure nextFigure;
    private final AxialDirection gravity;
    private final int width;
    private final int depth;
    private final AxialPosition originPosition;
    private final AxialPosition nextFigurePosition;
    private final FigureGenerator figureGenerator;
    //
    private final SpriteContext spriteContext;
    private boolean active = true;
    private Levels levels = new Levels();
    private final GameEventCallback gameEventCallback;

    private HexagonalField(int width, int depth, SpriteContext spriteContext, GameEventCallback gameEventCallback) {
        this.width = width;
        this.depth = depth;
        this.gravity = AxialDirection.Back;
        this.spriteContext = spriteContext;
        this.gameEventCallback = gameEventCallback;
        //
        int originQ = width / 2;
        int originR = depth - 3 - (originQ / 2);
        this.originPosition = new AxialPosition(originQ, originR);
        this.nextFigurePosition = new AxialPosition(width + 2, depth / 2 - width / 4);
        this.figureGenerator = new RandomFigureGenerator();
        AxialFigure generated = figureGenerator.generate();
        generated.setPosition(originPosition);
        this.floatFigure = new Figure(generated,
            spriteContext.getTextures().getFigure(),
            spriteContext
        );
        this.attachChild(floatFigure);
        onFloatFigureNewPosition();
        //
        this.nextFigure = new Figure(figureGenerator.getNext(), spriteContext.getTextures().getFigure(), spriteContext);
        this.nextFigure.setPosition(nextFigurePosition);
        this.attachChild(nextFigure);
        // test
//        for (AxialPosition axialPosition : TEST_INITIAL_FIELDS) {
//            addField(axialPosition, new ChangingHexagon(
//                axialPosition,
//                spriteContext.getTextures().getHexagon0(),
//                spriteContext.getTextures().getHexagon1(),
//                spriteContext));
//        }
    }

    public boolean isActive() {
        return active;
    }

    public int getWidth() {
        return width;
    }

    public int getDepth() {
        return depth;
    }

    public Collection<AxialPosition> getFields() {
        return fields.getPositions();
    }

    public IFigure getFloatFigure() {
        return floatFigure;
    }

    public boolean setFloatFigure(IFigure floatFigure) {
        for (AxialPosition axialPosition : floatFigure.getPartsPositions()) {
            if (fields.contains(axialPosition)) {
                return false;
            }
        }
        this.floatFigure = new Figure(floatFigure, spriteContext.getTextures().getFigure(), spriteContext);
        onFloatFigureNewPosition();
        return true;
    }

    public static HexagonalField generateJar(int width, int depth, SpriteContext spriteContext, GameEventCallback gameEventCallback) {
        HexagonalField jar = new HexagonalField(width, depth, spriteContext, gameEventCallback);
        final Textures textures = spriteContext.getTextures();
        // Set borders: right hexa-corner
        Hexagon pos = new Hexagon(
            neighbour(new AxialPosition(0, 0), AxialDirection.Left),
            textures.getBorderLeft(),
            spriteContext
        );
        AxialDirection[] directions = {AxialDirection.Right, AxialDirection.RightBack};
        for (int i = -1; i < width + 1; i++) {
            // root cell
            jar.addBorder(pos);
            jar.bottomFields.add(pos);
            // stack cells for borders
            if (i == -1 || i == width) {
                Hexagon stackPos = pos;
                TextureRegion border;
                if (i == -1) {
                    border = textures.getBorderLeft();
                } else {
                    border = textures.getBorderRight();
                }
                for (int j = 1; j < depth; j++) {
                    stackPos = new Hexagon(
                        neighbour(stackPos.getPosition(), AxialDirection.Forward),
                        border,
                        spriteContext
                    );
                    jar.addBorder(stackPos);
                }
            }
            if (i == width) {
                break;
            }
            final TextureRegion borderBottom = (i < width - 1) ? textures.getBorderBottom() : textures.getBorderRight();
            // next stack
            pos = new Hexagon(
                neighbour(pos.getPosition(), directions[(i + 2) % directions.length]),
                borderBottom,
                spriteContext
            );
        }
        jar.setPosition(
            spriteContext.getSize() * 3 / 2,
            SQ3 * spriteContext.getSize() * depth - SQ3 * spriteContext.getSize() / 2
        );
        return jar;
    }

    private void addBorder(Hexagon border) {
        this.borders.put(border.getPosition(), border);
        this.attachChild(border);
    }

    private void addField(AxialPosition position, ChangingHexagon field) {
        this.fields.put(position, field);
        this.attachChild(field);
    }

    public void removeField(AxialPosition position) {
        final Hexagon hex = fields.get(position);
        if (hex != null) {
            detachSafely(hex);
            fields.remove(position);
        }
    }

    private static AxialPosition neighbour(AxialPosition initPosition, AxialDirection direction) {
        return new AxialPosition(initPosition.getQ() + direction.getQ(), initPosition.getR() + direction.getR());
    }

    public Collection<AxialPosition> getForbiddenFields() {
        ArrayList<AxialPosition> forbidden = new ArrayList<AxialPosition>();
        forbidden.addAll(borders.getPositions());
        forbidden.addAll(fields.getPositions());
        return forbidden;
    }

    @Override
    public boolean tick() {
        if (active && floatFigure != null) {
            boolean moved = floatFigure.move(getForbiddenFields(), gravity);
            onFloatFigureNewPosition();
            if (!moved) {
                onFigureDropped();
            } else {
                return true;
            }
        }
        return false;
    }

    public void restart() {
        for (Hexagon hexagon : fields.getHexagons()) {
            detachChild(hexagon);
        }
        fields.clear();
        gameEventCallback.reset();
        figureGenerator.reset();
        createNewFloatFigure();
        nextFigure.resetParts(figureGenerator.getNext().getParts());
        active = true;
    }

    @Override
    public boolean turn(RotateDirection direction) {
        if (floatFigure != null) {
            floatFigure.turn(getForbiddenFields(), direction);
            onFloatFigureNewPosition();
            return true;
        }
        return false;
    }

    public void harden() {
        if (floatFigure == null) {
            return;
        }
        // Harden float figure
        for (AxialPosition axialPosition : floatFigure.getPartsPositions()) {
            addField(new AxialPosition(axialPosition),
                new ChangingHexagon(
                    axialPosition,
                    spriteContext.getTextures().getHexagon0(),
                    spriteContext.getTextures().getHexagon1(),
                    spriteContext)
            );
        }
        detachSafely(floatFigure);
    }

    private void onFigureDropped() {
        // Convert old figure to hard block
        harden();
        // Clear full lines
        int linesRemoved = 0;
        for (int x = 0; x < getDepth(); x++) {
            if (lineCompleted(x)) {
                dropAll(removeLine(x));
                x--;
                linesRemoved++;
            }
        }
        if (linesRemoved > 0) {
            gameEventCallback.onLinesRemoved(linesRemoved);
        }
        boolean figureSet = createNewFloatFigure();
        if (!figureSet) {
            // game over
            active = false;
            return;
        }
        this.nextFigure.resetParts(figureGenerator.getNext().getParts());
    }

    private boolean createNewFloatFigure() {
        AxialFigure newFigure = figureGenerator.generate();
        newFigure.setPosition(new AxialPosition(originPosition));
        boolean figureSet = setFloatFigure(newFigure);
        if (figureSet) {
            this.attachChild(this.floatFigure);
        }
        return figureSet;
    }

    private boolean lineCompleted(int x) {
        for (int q = 0; q < getWidth(); q++) {
            if (!getFields().contains(new AxialPosition(q, x - q / 2))) {
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
            removeField(new AxialPosition(axQ, axR));
            demarkationPoints.put(axQ, axR);
        }
        return demarkationPoints;
    }

    private void dropAll(Map<Integer, Integer> demarkationPoints) {
        if (!active) {
            return;
        }
        if (fields.isEmpty()) {
            return;
        }

        List<AxialPosition> fallingPart = new ArrayList<AxialPosition>();
        for (AxialPosition pos : fields.getPositions()) {
            if (pos.getR() > demarkationPoints.get(pos.getQ())) {
                fallingPart.add(pos);
            }
        }

        if (fallingPart.isEmpty()) {
            return;
        }
        fields.moveBatch(fallingPart, gravity);
    }

    private void detachSafely(IEntity entity) {
        Engine.EngineLock lock = spriteContext.getEngine().getEngineLock();
        lock.lock();
        try {
            this.detachChild(entity);
        } finally {
            lock.unlock();
        }
    }

    private long framesLeft = 0;

    public void update() {
        framesLeft++;
        if (framesLeft >= gameEventCallback.framesPerTick()) {
            tick();
            framesLeft = 0;
        }
    }

    public boolean move(MoveDirection direction) {
        if (!active) {
            return false;
        }
        if (floatFigure != null) {
            AxialDirection axialDirection;
            if (direction == MoveDirection.LEFT) {
                axialDirection = AxialDirection.Left;
            } else if (direction == MoveDirection.RIGHT) {
                axialDirection = AxialDirection.RightBack;
            } else {
                throw new UnsupportedOperationException("Not recognized move direction");
            }
            boolean moved = floatFigure.move(getForbiddenFields(), axialDirection);
            onFloatFigureNewPosition();
            return moved;
        }
        return false;
    }

    public void drop() {
        if (!active) {
            return;
        }
        while (tick()) {
        }
    }
    
    private void onFloatFigureNewPosition() {
        highlightBottomBorders();
    }

    private void highlightBottomBorders() {
        Set<Integer> coveredColumnNumbers = new HashSet<Integer>();
        for (AxialPosition axialPosition : floatFigure.getPartsPositions()) {
            coveredColumnNumbers.add(axialPosition.getQ());
        }
        for (Hexagon hexagon : bottomFields) {
            if (coveredColumnNumbers.contains(hexagon.getPosition().getQ())) {
                hexagon.setColor(new Color(0x88, 0x00, 0x00));
            } else {
                hexagon.setColor(Color.WHITE);
            }
        }
    }
}
