package net.evlikat.hexatrix.views;

import android.content.Context;
import android.view.GestureDetector;
import net.evlikat.hexatrix.Textures;
import net.evlikat.hexatrix.entities.HexagonalField;
import net.evlikat.hexatrix.entities.SpriteContext;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.input.touch.TouchEvent;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
public class PlayView extends GameView {

    private static final float SQ3 = (float) Math.sqrt(3);

    private static final int DEPTH = 21;
    private static final int WIDTH = 9;

    private final HexagonalField field;
    //private final Textures textures;
    private final float size;
    private final TouchListener touchListener;

    public PlayView(Context context, Engine engine, Camera camera, Textures textures) {
        super(engine, camera);
        //this.textures = textures;
        this.size = camera.getHeight() / ((DEPTH + 1) * (SQ3)); // pixels per hexagon
        this.field = HexagonalField.generateJar(
            WIDTH,
            DEPTH,
            new SpriteContext(size, camera, textures, engine)
        );
        touchListener = new TouchListener(field);
    
    }

    @Override
    protected void onTouch(TouchEvent pSceneTouchEvent) {
        touchListener.onTouchEvent(pSceneTouchEvent);
    }

    @Override
    public void populate() {
        scene.setBackground(new Background(0, 0, 0));
        scene.attachChild(field);
    }

    @Override
    public void update() {
        field.update();
    }
}
