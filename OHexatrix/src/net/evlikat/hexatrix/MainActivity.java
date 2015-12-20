package net.evlikat.hexatrix;

import android.view.KeyEvent;
import net.evlikat.hexatrix.scores.Leaderboard;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.ui.activity.BaseGameActivity;

public class MainActivity extends BaseGameActivity {

    private static final int CAMERA_WIDTH = 1080;
    private static final int CAMERA_HEIGHT = 1920;
    private Camera camera;
    private Textures textures;
    private SceneManager sceneManager;

    public EngineOptions onCreateEngineOptions() {
        this.camera = new ZoomCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        return new EngineOptions(
                true,
                ScreenOrientation.PORTRAIT_FIXED,
                new FillResolutionPolicy(),
                this.camera
        );
    }

    public void onCreateResources(OnCreateResourcesCallback ocrc) throws Exception {
        textures = new Textures(getTextureManager(), getAssets());
        sceneManager = new SceneManager(this, mEngine, camera, textures, new Leaderboard(
                "/data/data/" + MainActivity.class.getPackage().getName() + "/leaderboard.lb", 20));

        ocrc.onCreateResourcesFinished();
    }

    public void onCreateScene(OnCreateSceneCallback ocsc) throws Exception {
        this.mEngine.registerUpdateHandler(new FPSLogger());
        final Scene scene = sceneManager.getCurrentView().getScene();

        this.mEngine.registerUpdateHandler(new TimerHandler(0.05f, true, new ITimerCallback() {

            public void onTimePassed(TimerHandler pTimerHandler) {
                sceneManager.updateCurrentView();
            }
        }));
        ocsc.onCreateSceneFinished(scene);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            sceneManager.onBackPressed();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void onPopulateScene(Scene scene, OnPopulateSceneCallback opsc) throws Exception {
        opsc.onPopulateSceneFinished();
    }
}
