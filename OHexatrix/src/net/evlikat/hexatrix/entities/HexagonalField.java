package net.evlikat.hexatrix.entities;

import net.evlikat.hexatrix.Textures;
import net.evlikat.hexatrix.axial.*;
import org.andengine.engine.Engine;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.opengl.texture.region.TextureRegion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static net.evlikat.hexatrix.entities.AxialEntity.SQ3;

/**
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
public class HexagonalField extends Entity implements IHexagonalField, Serializable {

    private final Fields borders = new Fields();
    private final Fields backWall = new Fields();
    private final Fields fields = new Fields();
    // Properties
    private Figure floatFigure;
    private Figure droppedShadowFigure;
    private Figure movedShadowFigure;
    private Figure nextFigure;
    //
    private MoveDirection moveDirection;
    private int moveShift = 0;
    //
    private final AxialDirection gravity;
    private final int width;
    private final int depth;
    private final AxialPosition originPosition;
    private final AxialPosition nextFigurePosition;
    private final FigureGenerator figureGenerator;
    //
    private GameState currentState;
    private final SpriteContext spriteContext;
    private final GameEventCallback gameEventCallback;
    //
    private List<GameFinishedListener> gameFinishedListeners = new ArrayList<>();
    private List<GamePausedListener> gamePausedListeners = new ArrayList<>();

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
        this.currentState = new FallingBlocksState(this, this.gameEventCallback);
    }

    private boolean createNewFloatFigure() {
        AxialFigure newFigure = figureGenerator.generate();
        newFigure.setPosition(new AxialPosition(originPosition));

        if (this.floatFigure == null) {
            this.floatFigure = new Figure(newFigure,
                    spriteContext.getTextures().getFigure(),
                    spriteContext
            );
            this.attachChild(this.floatFigure);
            onFloatFigureNewPosition();

            this.nextFigure = new Figure(figureGenerator.getNext(), spriteContext.getTextures().getFigure(), spriteContext);
            this.nextFigure.setPosition(nextFigurePosition);
            this.attachChild(nextFigure);

            return true;
        } else {
            this.detachSafely(this.floatFigure);
            boolean figureSet = setFloatFigure(newFigure);
            if (figureSet) {
                this.attachChild(this.floatFigure);
                onFloatFigureNewPosition();
            }
            return figureSet;
        }
    }

    public void addGameFinishedListener(GameFinishedListener listener) {
        gameFinishedListeners.add(listener);
    }

    public void addPausedFinishedListener(GamePausedListener listener) {
        gamePausedListeners.add(listener);
    }

    void onGameFinished() {
        for (GameFinishedListener listener : gameFinishedListeners) {
            listener.onGameFinished();
        }
    }

    void onGamePaused() {
        for (GamePausedListener gamePausedListener : gamePausedListeners) {
            gamePausedListener.onGamePaused();
        }
    }

    void onGameResumed() {
        for (GamePausedListener gamePausedListener : gamePausedListeners) {
            gamePausedListener.onGameResumed();
        }
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

    public static HexagonalField generateJar(int width, int depth,
                                             SpriteContext spriteContext,
                                             GameEventCallback gameEventCallback,
                                             InitialFieldGenerator initialFieldGenerator
    ) {
        HexagonalField jar = new HexagonalField(width, depth, spriteContext, gameEventCallback);
        final Textures textures = spriteContext.getTextures();
        // Set borders: right hexa-corner
        Hexagon pos = new Hexagon(
                new AxialPosition(0, 0).plus(AxialDirection.Left),
                textures.getBorderLeft(),
                spriteContext
        );
        AxialDirection[] directions = {AxialDirection.Right, AxialDirection.RightBack};
        // borders
        for (int i = -1; i < width + 1; i++) {
            // root cell
            jar.addBorder(pos);
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
                            stackPos.getPosition().plus(AxialDirection.Forward),
                            border,
                            spriteContext
                    );
                    jar.addBorder(stackPos);
                }
            }
            if (i == width) {
                break;
            }
            // right bottom border should be cut
            final TextureRegion borderBottom = (i < width - 1) ? textures.getBorderBottom() : textures.getBorderRight();
            // next stack
            pos = new Hexagon(
                    pos.getPosition().plus(directions[(i + 2) % directions.length]),
                    borderBottom,
                    spriteContext
            );
        }
        // back wall
        for (int i = 0; i < width; i++) {
            for (int h = 0; h < depth - 1; h++) {
                final Hexagon brick = new Hexagon(new AxialPosition(i, h - i / 2), textures.getBrick(), spriteContext);
                jar.addBackWallBrick(brick);
            }
        }
        // setting initial fields
        for (AxialPosition axialPosition : initialFieldGenerator.generate()) {
            jar.addField(axialPosition, new ChangingHexagon(
                    axialPosition,
                    spriteContext.getTextures().getHexagon0(),
                    spriteContext.getTextures().getHexagon1(),
                    spriteContext));
        }
        // setting start position
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

    private void addBackWallBrick(Hexagon brick) {
        this.backWall.put(brick.getPosition(), brick);
        this.attachChild(brick);
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

    Figure getFloatFigure() {
        return floatFigure;
    }

    List<Hexagon> getHexagons(Integer lineNumber) {
        final List<Hexagon> result = new ArrayList<>();
        for (int axQ = 0; axQ < getWidth(); axQ++) {
            int axR = lineNumber - axQ / 2;
            Hexagon hexagon = fields.get(new AxialPosition(axQ, axR));
            if (hexagon != null) {
                result.add(hexagon);
            }
        }
        return result;
    }

    boolean fall() {
        if (floatFigure.move(getForbiddenFields(), gravity)) {
            onFloatFigureNewPosition();
            return true;
        }
        harden();
        return false;
    }

    List<Integer> calcLinesToRemove() {
        List<Integer> lineNumbersToRemove = new ArrayList<>();
        for (int x = 0; x < getDepth(); x++) {
            if (lineCompleted(x)) {
                lineNumbersToRemove.add(x);
            }
        }
        return lineNumbersToRemove;
    }

    public Collection<AxialPosition> getForbiddenFields() {
        ArrayList<AxialPosition> forbidden = new ArrayList<>();
        forbidden.addAll(borders.getPositions());
        forbidden.addAll(fields.getPositions());
        return forbidden;
    }

    public void update() {
        currentState = currentState.next();
    }

    public void restart(InitialFieldGenerator initialFieldGenerator) {
        for (Hexagon hexagon : fields.getHexagons()) {
            detachChild(hexagon);
        }
        fields.clear();
        gameEventCallback.reset();
        figureGenerator.reset();
        // re-init with new fields
        for (AxialPosition position : initialFieldGenerator.generate()) {
            addField(position, new ChangingHexagon(
                    position,
                    spriteContext.getTextures().getHexagon0(),
                    spriteContext.getTextures().getHexagon1(),
                    spriteContext));
        }
        createNewFloatFigure();
        nextFigure.resetParts(figureGenerator.getNext().getParts());
        currentState = new FallingBlocksState(this, gameEventCallback);
    }

    @Override
    public void pause() {
        currentState = PauseState.create(currentState, this, gameEventCallback);
    }

    @Override
    public boolean turn(RotateDirection direction) {
        return currentState.turn(direction);
    }

    @Override
    public boolean move(MoveDirection direction, int steps) {
        moveShift = 0;
        boolean moved = currentState.move(direction, steps);
        return moved;
    }

    @Override
    public boolean moving(MoveDirection direction, int steps) {
        moveDirection = direction;
        moveShift = steps;
        boolean moving = currentState.moving(direction, steps);
        onFloatFigureNewPosition();
        return moving;
    }

    boolean turnFigure(RotateDirection direction) {
        if (floatFigure != null) {
            if (floatFigure.turn(getForbiddenFields(), direction)) {
                onFloatFigureNewPosition();
            }
            return true;
        }
        return false;
    }

    boolean moveFigure(AxialDirection axialDirection, int steps) {
        if (floatFigure != null) {
            boolean movedAtAll = false;
            for (int i = 0; i < steps; i++) {
                boolean moved = floatFigure.move(getForbiddenFields(), axialDirection);
                if (!moved) {
                    break;
                }
                movedAtAll = true;
            }
            onFloatFigureNewPosition();
            return movedAtAll;
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
        onFloatFigureNewPosition();
    }

    /**
     * @param linesRemoved
     * @return true if new float figure was set properly, false if there was no room to set new figure.
     */
    public boolean onFigureDropped(int linesRemoved) {
        if (linesRemoved > 0) {
            gameEventCallback.onLinesRemoved(linesRemoved);
        }
        boolean figureSet = createNewFloatFigure();
        if (!figureSet) {
            // game over
            return false;
        }
        this.nextFigure.resetParts(figureGenerator.getNext().getParts());
        return true;
    }

    private boolean lineCompleted(int x) {
        for (int q = 0; q < getWidth(); q++) {
            if (!getFields().contains(new AxialPosition(q, x - q / 2))) {
                return false;
            }
        }
        return true;
    }

    public void dropAll(int steps, Map<Integer, Integer> demarkationPoints) {
        if (fields.isEmpty()) {
            return;
        }

        List<AxialPosition> fallingPart = new ArrayList<>();
        for (AxialPosition pos : fields.getPositions()) {
            if (pos.getR() > demarkationPoints.get(pos.getQ())) {
                fallingPart.add(pos);
            }
        }

        if (fallingPart.isEmpty()) {
            return;
        }
        for (int i = 0; i < steps; i++) {
            fallingPart = fields.moveBatch(fallingPart, gravity);
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

    @Override
    public void drop() {
        boolean moved = false;
        while (this.floatFigure.move(getForbiddenFields(), gravity)) {
            moved = true;
        }
        // Avoid multi-drops abused
        if (moved) {
            onFloatFigureNewPosition();
            currentState.cancel();
        }
    }

    private void onFloatFigureNewPosition() {
        highlightMovedShadowHexagons();
        highlightShadowHexagons(this.movedShadowFigure == null ? this.floatFigure : this.movedShadowFigure);
    }

    private void highlightShadowHexagons(Figure source) {
        if (this.droppedShadowFigure != null) {
            this.detachSafely(this.droppedShadowFigure);
        }
        if (source.getParent() == null) {
            this.droppedShadowFigure = null;
            return;
        }
        this.droppedShadowFigure = new Figure(source,
                spriteContext.getTextures().getShadow(),
                spriteContext);
        while (this.droppedShadowFigure.move(getForbiddenFields(), gravity)) {
        }
        this.attachChild(this.droppedShadowFigure);
    }

    private void highlightMovedShadowHexagons() {
        if (this.movedShadowFigure != null) {
            this.detachSafely(this.movedShadowFigure);
        }
        if (this.floatFigure.getParent() == null) {
            this.movedShadowFigure = null;
            return;
        }
        this.movedShadowFigure = new Figure(floatFigure,
                spriteContext.getTextures().getShadow(),
                spriteContext);

        for (int i = 0; i < moveShift; i++) {
            boolean moved = this.movedShadowFigure.move(getForbiddenFields(),
                    moveDirection == MoveDirection.LEFT
                            ? AxialDirection.Left
                            : AxialDirection.RightBack);
            if (!moved) {
                break;
            }
        }
        this.attachChild(this.movedShadowFigure);
    }
}
