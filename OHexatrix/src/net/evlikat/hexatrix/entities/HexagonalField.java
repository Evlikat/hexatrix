package net.evlikat.hexatrix.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.evlikat.hexatrix.axial.AxialDirection;
import net.evlikat.hexatrix.axial.AxialFigure;
import net.evlikat.hexatrix.axial.AxialPosition;
import net.evlikat.hexatrix.axial.FigureGenerator;
import net.evlikat.hexatrix.axial.GameSession;
import net.evlikat.hexatrix.axial.MoveDirection;
import net.evlikat.hexatrix.axial.RandomFigureGenerator;
import net.evlikat.hexatrix.axial.RotateDirection;
import static net.evlikat.hexatrix.entities.AxialEntity.SQ3;
import org.andengine.engine.Engine;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;

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
    );

    Map<AxialPosition, Hexagon> borders = new HashMap<AxialPosition, Hexagon>();
    Map<AxialPosition, Hexagon> fields = new HashMap<AxialPosition, Hexagon>();
    // Properties
    private Figure floatFigure;
    private final AxialDirection gravity;
    private final int width;
    private final int depth;
    private final AxialPosition originPosition;
    private final FigureGenerator figureGenerator;
    //
    private final SpriteContext spriteContext;
    private final GameSession gameSession = new GameSession();
    private boolean active = true;

    private HexagonalField(int width, int depth, SpriteContext spriteContext) {
        this.width = width;
        this.depth = depth;
        this.gravity = AxialDirection.Back;
        this.spriteContext = spriteContext;
        //
        int originQ = width / 2;
        int originR = depth - 3 - (originQ / 2);
        this.originPosition = new AxialPosition(originQ, originR);
        this.figureGenerator = new RandomFigureGenerator();
        AxialFigure generated = figureGenerator.generate();
        generated.setPosition(originPosition);
        this.floatFigure = new Figure(generated,
            spriteContext.getTextures().getFigure(),
            spriteContext
        );
        this.attachChild(floatFigure);
        // test
        for (AxialPosition axialPosition : TEST_INITIAL_FIELDS) {
            addField(axialPosition, new Hexagon(axialPosition, spriteContext.getTextures().getHexagon0(), spriteContext));
        }
    }

    public int getWidth() {
        return width;
    }

    public int getDepth() {
        return depth;
    }

    public Collection<AxialPosition> getFields() {
        return fields.keySet();
    }

    public IFigure getFloatFigure() {
        return floatFigure;
    }

    public boolean setFloatFigure(IFigure floatFigure) {
        for (AxialPosition axialPosition : floatFigure.getPartsPositions()) {
            if (fields.containsKey(axialPosition)) {
                return false;
            }
        }
        this.floatFigure = new Figure(floatFigure, spriteContext.getTextures().getFigure(), spriteContext);
        this.attachChild(this.floatFigure);
        return true;
    }

    public static HexagonalField generateJar(int width, int depth, SpriteContext spriteContext) {
        HexagonalField jar = new HexagonalField(width, depth, spriteContext);
        // Set borders: right hexa-corner
        Hexagon pos = new Hexagon(
            neighbour(new AxialPosition(0, 0), AxialDirection.Left),
            spriteContext.getTextures().getBorder(),
            spriteContext
        );
        AxialDirection[] directions = {AxialDirection.Right, AxialDirection.RightBack};
        for (int i = -1; i < width + 1; i++) {
            // root cell
            jar.addBorder(pos);
            // stack cells for borders
            if (i == -1 || i == width) {
                Hexagon stackPos = pos;
                for (int j = 1; j < depth; j++) {
                    stackPos = new Hexagon(
                        neighbour(stackPos.getPosition(), AxialDirection.Forward),
                        spriteContext.getTextures().getBorder(),
                        spriteContext
                    );
                    jar.addBorder(stackPos);
                }
            }
            // next stack
            pos = new Hexagon(
                neighbour(pos.getPosition(), directions[(i + 2) % directions.length]),
                spriteContext.getTextures().getBorder(),
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

    private void addField(AxialPosition position, Hexagon field) {
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
        return new AxialPosition(initPosition.getQ() + direction.getDq(), initPosition.getR() + direction.getDr());
    }

    public Collection<AxialPosition> getForbiddenFields() {
        ArrayList<AxialPosition> forbidden = new ArrayList<AxialPosition>();
        forbidden.addAll(borders.keySet());
        forbidden.addAll(fields.keySet());
        return forbidden;
    }

    @Override
    public boolean tick() {
        if (floatFigure != null) {
            boolean moved = floatFigure.move(getForbiddenFields(), gravity);
            if (!moved) {
                onFigureDropped();
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean turn(RotateDirection direction) {
        if (floatFigure != null) {
            floatFigure.turn(getForbiddenFields(), direction);
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
                new Hexagon(axialPosition, spriteContext.getTextures().getHexagon0(), spriteContext)
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
                linesRemoved++;
            }
        }
        if (linesRemoved > 0) {
            gameSession.addScores(linesRemoved);
            //currentLevel = calculateCurrentLevel();
        }
        // Generate new falling figure
        AxialFigure newFigure = figureGenerator.generate();
        newFigure.setPosition(new AxialPosition(originPosition));
        boolean figureSet = setFloatFigure(newFigure);
        if (!figureSet) {
            active = false;
        }
    }
//
//    private int calculateCurrentLevel() {
//         int scores = gameSession.getScores();
//         for (int i = 0; i < LEVELS.size(); i++) {
//         Physics.Level level = LEVELS.get(i);
//         if (level.scoresBarrier > scores) {
//         return i;
//         }
//         }
//         return LEVELS.size() - 1;
//        return 0;
//    }

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
        for (AxialPosition pos : fields.keySet()) {
            if (pos.getR() > demarkationPoints.get(pos.getQ())) {
                fallingPart.add(pos);
            }
        }

        if (fallingPart.isEmpty()) {
            return;
        }
        // We won't rotate this figure so it does not matter which block is the center
        AxialFigure fallingFigure = new AxialFigure(fallingPart.get(0), fallingPart);
        // move once
        Collection<AxialPosition> forbiddenFields = getForbiddenFields();
        forbiddenFields.removeAll(fallingPart);
        if (fallingFigure.move(forbiddenFields, gravity)) {
            Map<AxialPosition, Hexagon> map = new HashMap<AxialPosition, Hexagon>();
            for (AxialPosition fallenPart : fallingPart) {
                Hexagon hexagon = fields.get(fallenPart);
                AxialPosition newPosition = fallingFigure.getPosition().plus(fallenPart);
                map.put(newPosition, hexagon);
                fields.remove(fallenPart);
            }
            for (Map.Entry<AxialPosition, Hexagon> entry : map.entrySet()) {
                addField(entry.getKey(), entry.getValue());
            }
        }
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

    public void update() {
        gameSession.frameLeft();
        if (gameSession.getFramesLeft() == 50) {
            tick();
            gameSession.resetFrames();
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
            return floatFigure.move(getForbiddenFields(), axialDirection);
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
}
