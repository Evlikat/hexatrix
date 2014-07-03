package net.evlikat.hexatrix.entities;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.evlikat.hexatrix.Textures;
import net.evlikat.hexatrix.axial.AxialDirection;
import net.evlikat.hexatrix.axial.AxialPosition;
import net.evlikat.hexatrix.axial.RotateDirection;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
public class Figure extends AxialEntity implements IFigure {

    private static interface CubeRotator {

        CubeCoordinates turn(CubeCoordinates xyz);
    }
    private final Hexagon center;
    private final Collection<Hexagon> parts;

    public Figure(
        Hexagon center,
        Collection<Hexagon> parts,
        float size, Camera camera,
        float pX, float pY,
        ITextureRegion pTextureRegion,
        VertexBufferObjectManager pVertexBufferObjectManager
    ) {
        super(size, camera, pX, pY, size * 2, size * SQ3, pTextureRegion, pVertexBufferObjectManager);
        this.center = center;
        this.parts = parts;
    }

    @Override
    public AxialPosition getPosition() {
        return center.getPosition();
    }
    
    public Hexagon getCenter() {
        return center;
    }

    @Override
    public void turn(Collection<AxialPosition> forbiddenPositions, RotateDirection direction) {
        if (direction == RotateDirection.LEFT) {
            turn(forbiddenPositions, new CubeRotator() {

                public CubeCoordinates turn(CubeCoordinates xyz) {
                    return new CubeCoordinates(-xyz.getZ(), -xyz.getX(), -xyz.getY());
                }
            });
        } else if (direction == RotateDirection.RIGHT) {
            turn(forbiddenPositions, new CubeRotator() {

                public CubeCoordinates turn(CubeCoordinates xyz) {
                    return new CubeCoordinates(-xyz.getY(), -xyz.getZ(), -xyz.getX());
                }
            });
        } else {
            throw new UnsupportedOperationException("Not implemented direction");
        }
    }

    @Override
    public boolean move(Collection<AxialPosition> forbiddenPositions, AxialDirection direction) {
        AxialPosition newPosition = new AxialPosition(
            center.getPosition().getQ() + direction.getDq(),
            center.getPosition().getR() + direction.getDr()
        );
        if (forbiddenPositions.contains(newPosition)) {
            // Figure center can't be moved
            return false;
        }
        for (Hexagon partPos : parts) {
            if (forbiddenPositions.contains(partPos.getPosition().relatedTo(newPosition))) {
                // Figure can't be moved
                return false;
            }
        }
        onMoved(newPosition);
        return true;
    }

    private void turn(Collection<AxialPosition> forbiddenPositions, CubeRotator turner) {
        Map<Hexagon, AxialPosition> newPartPositions = new HashMap<Hexagon, AxialPosition>();
        for (Hexagon partPos : parts) {
            AxialPosition newPos = from(turner.turn(from(partPos.getPosition())));
            if (forbiddenPositions.contains(newPos.relatedTo(center.getPosition()))) {
                // Figure can't be turned
                return;
            }
            newPartPositions.put(partPos, newPos);
        }
        // If all positions are allowed then turn the figure
        for (Map.Entry<Hexagon, AxialPosition> entry : newPartPositions.entrySet()) {
            Hexagon hexagon = entry.getKey();
            AxialPosition newPosition = entry.getValue();
            hexagon.setPosition(newPosition);
        }
    }

    private static AxialPosition from(CubeCoordinates cubeCoords) {
        return new AxialPosition(cubeCoords.getX(), cubeCoords.getZ());
    }

    private static CubeCoordinates from(AxialPosition axialCoords) {
        return new CubeCoordinates(axialCoords.getQ(), -axialCoords.getQ() - axialCoords.getR(), axialCoords.getR());
    }
}
