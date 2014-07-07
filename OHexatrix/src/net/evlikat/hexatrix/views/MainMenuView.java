package net.evlikat.hexatrix.views;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
public class MainMenuView extends GameView {

    private final IFont font;
    private final MenuCallback menuCallback;

    public MainMenuView(Engine engine, Camera camera, IFont font, MenuCallback callback) {
        super(engine, camera);
        this.font = font;
        this.menuCallback = callback;
    }

    @Override
    public void populate() {
        scene.setBackground(new Background(0, 0, 0));
        scene.attachChild(new Text(30, 30, font, "Main menu. Touch to start", engine.getVertexBufferObjectManager()));
    }

    @Override
    public void update() {
    }

    @Override
    protected void onTouch(TouchEvent pSceneTouchEvent) {
        menuCallback.toPlayView();
    }

}
