package net.evlikat.hexatrix;

import android.graphics.Color;
import android.graphics.Typeface;
import net.evlikat.hexatrix.scores.IScoreStorage;
import net.evlikat.hexatrix.views.*;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
public class SceneManager implements PlayCallback, MenuCallback, LeadersCallback {

    private GameView currentView;

    private final MainActivity activity;
    private final Engine engine;
    private final Camera camera;
    private final Textures textures;
    private final IFont font;
    private final IScoreStorage scoreStorage;
    // Views
    private MainMenuView menuView;
    private PlayView playView;
    private LeadersView leadersView;

    public SceneManager(MainActivity activity, Engine engine, Camera camera, Textures textures, IScoreStorage scoreStorage) {
        this.activity = activity;
        this.engine = engine;
        this.camera = camera;
        this.textures = textures;
        BitmapTextureAtlas fontTexture = new BitmapTextureAtlas(engine.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
        this.font = new Font(engine.getFontManager(), fontTexture,
                Typeface.create(Typeface.MONOSPACE, Typeface.BOLD), 48, true, Color.WHITE);
        this.font.load();
        this.scoreStorage = scoreStorage;
        this.currentView = getMainMenuView();
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

    public final MainMenuView getMainMenuView() {
        if (menuView == null) {
            menuView = new MainMenuView(engine, camera, textures, font, this, scoreStorage);
            menuView.populate();
        }
        return menuView;
    }

    public final PlayView getPlayView() {
        if (playView == null) {
            playView = new PlayView(engine, camera, textures, font, this, scoreStorage);
            playView.populate();
        }
        return playView;
    }

    public final LeadersView getLeadersView() {
        if (leadersView == null) {
            leadersView = new LeadersView(engine, camera, font, this, scoreStorage);
            leadersView.populate();
        }
        return leadersView;
    }

    public void quit() {
        activity.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    // --- Callbacks ---
    public void toMenuView(GameResults gameResults) {
        final MainMenuView mainMenuView = getMainMenuView();
        mainMenuView.registerResult(gameResults);
        setCurrentView(mainMenuView);
    }

    public void toPlayView() {
        PlayView playView = getPlayView();
        playView.startNewGame();
        setCurrentView(playView);
    }

    public void toLeadersView() {
        setCurrentView(getLeadersView());
    }

    public void toMenuView() {
        setCurrentView(getMainMenuView());
    }
}
