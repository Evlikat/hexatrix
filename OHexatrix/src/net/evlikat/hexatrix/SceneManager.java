package net.evlikat.hexatrix;

import android.graphics.Typeface;
import net.evlikat.hexatrix.views.GameView;
import net.evlikat.hexatrix.views.MenuView;
import net.evlikat.hexatrix.views.PlayView;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.IFont;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
public class SceneManager {

    private GameView currentView;

    private final MainActivity activity;
    private final Engine engine;
    private final Camera camera;
    private final Textures textures;
    private final IFont font;
    // Views
    private MenuView menuView;
    private PlayView playView;

    public SceneManager(MainActivity activity, Engine engine, Camera camera, Textures textures) {
        this.activity = activity;
        this.engine = engine;
        this.camera = camera;
        this.textures = textures;
        this.font = FontFactory.create(
            engine.getFontManager(),
            engine.getTextureManager(),
            256, 256,
            Typeface.create(Typeface.DEFAULT, Typeface.BOLD),
            32
        );
        this.font.load();
        this.currentView = getPlayView();
    }

    public void populateCurrentView() {
        currentView.populate();
    }

    public void updateCurrentView() {
        currentView.update();
    }

    public GameView getCurrentView() {
        return currentView;
    }

    public void setCurrentView(GameView currentView) {
        this.currentView = currentView;
        this.engine.setScene(currentView.getScene());
    }

    public final MenuView getMainMenuView() {
        if (menuView == null) {
            menuView = new MenuView(engine, camera, font);
        }
        return menuView;
    }

    public final PlayView getPlayView() {
        if (playView == null) {
            playView = new PlayView(activity, engine, camera, textures);
        }
        return playView;
    }
}
