package net.evlikat.hexatrix.scenes;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import net.evlikat.hexatrix.GameLoop;
import net.evlikat.hexatrix.GameSession;
import net.evlikat.hexatrix.Scene;
import net.evlikat.hexatrix.scenes.callbacks.GameMenuCallback;
import net.evlikat.hexatrix.scenes.callbacks.MainMenuCallback;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 01, 2014)
 */
public class SceneManager implements MainMenuCallback, GameMenuCallback {

    private final GameLoop loop;
    private final MainView mainView;
    private MainMenuScene mainMenuScene;
    private GameScene gameScene;

    public SceneManager(Activity context) {
        this.mainView = new MainView(context);
        this.mainView.setRenderer(new GlRenderer(mainView));
        this.loop = new GameLoop(mainView.getHolder(), mainView);
        context.setContentView(mainView);
    }

    public MainMenuScene getMainMenuScene() {
        if (mainMenuScene == null) {
            mainMenuScene = new MainMenuScene(this);
        }
        return mainMenuScene;
    }

    public GameScene getGameScene() {
        if (gameScene == null) {
            gameScene = new GameScene(this, mainView);
        }
        return gameScene;
    }

    public void start() {
        setScene(getMainMenuScene());
        loop.setRunning(true);
        loop.start();
    }

    private void setScene(Scene activeScene) {
        mainView.setCurrentScene(activeScene);
    }

    public void toGameScene() {
        setScene(getGameScene());
    }

    public void toMainMenu(GameSession gameSession) {
        final MainMenuScene newScene = getMainMenuScene();
        newScene.setScores(gameSession);
        setScene(newScene);
    }

    public void onResume() {
        mainView.onResume();
    }

    public void onPause() {
        mainView.onPause();
    }
}
