package net.evlikat.hexatrix.views;

import net.evlikat.hexatrix.Textures;
import net.evlikat.hexatrix.axial.AxialPosition;
import net.evlikat.hexatrix.axial.Physics;
import net.evlikat.hexatrix.axial.RandomFigureGenerator;
import net.evlikat.hexatrix.axial.TrivialFieldGenerator;
import net.evlikat.hexatrix.entities.Hexagon;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
public class PlayView extends GameView {

    private static final float SQ3 = (float) Math.sqrt(3);

    private final Physics physics;
    private final Textures textures;
    private final float size;

    public PlayView(Engine engine, Camera camera, Textures textures) {
        super(engine, camera);
        this.physics = new Physics(new TrivialFieldGenerator());
        this.physics.setFigureGenerator(new RandomFigureGenerator());
        this.textures = textures;
        this.size = camera.getHeight() / ((physics.getDepth() + 1) * (SQ3)); // pixels per hexagon
    }

    @Override
    public void populate() {
        scene.setBackground(new Background(0, 0, 0));
        for (AxialPosition border : physics.getField().getBorders()) {
            scene.attachChild(
                new Hexagon(
                    border, size, camera, textures.getBorder(), engine.getVertexBufferObjectManager()
                )
            );
        }
    }

    @Override
    public void update() {
        physics.update();
    }
}
