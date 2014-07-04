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
public class MenuView extends GameView {

    private final IFont font;

    public MenuView(Engine engine, Camera camera, IFont font) {
        super(engine, camera);
        this.font = font;
    }

    @Override
    public void populate() {
        scene.setBackground(new Background(0xEE, 0xEE, 0xEE));
        scene.attachChild(new Text(30, 30, font, "Main menu. Touch to start", engine.getVertexBufferObjectManager()));
    }

    @Override
    public void update() {
    }

    @Override
    protected void onTouch(TouchEvent pSceneTouchEvent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
