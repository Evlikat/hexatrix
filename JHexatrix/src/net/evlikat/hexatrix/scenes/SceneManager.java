package net.evlikat.hexatrix.scenes;

import android.app.Activity;
import net.evlikat.hexatrix.GameLoop;
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
        this.loop = new GameLoop(mainView.getHolder(), mainView);
        context.setContentView(mainView);
    }

    public MainMenuScene getMainMenuScene() {
        if (mainMenuScene == null) {
            mainMenuScene = new MainMenuScene(this);
            mainMenuScene.init();
        }
        return mainMenuScene;
    }

    public GameScene getGameScene() {
        if (gameScene == null) {
            gameScene = new GameScene(this, mainView, loop);
            gameScene.init();
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

    public void toMainMenu() {
        setScene(getMainMenuScene());
    }
}
