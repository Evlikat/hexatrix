package net.evlikat.hexatrix;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.ui.activity.BaseGameActivity;

public class MainActivity extends BaseGameActivity {

    private static final int CAMERA_WIDTH = 720;
    private static final int CAMERA_HEIGHT = 1280;
    private Camera camera;
    private Textures textures;
    private SceneManager sceneManager;

    public EngineOptions onCreateEngineOptions() {
        this.camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        return new EngineOptions(
            true,
            ScreenOrientation.PORTRAIT_FIXED,
            new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
            this.camera
        );
    }

    public void onCreateResources(OnCreateResourcesCallback ocrc) throws Exception {
        textures = new Textures(getTextureManager(), getAssets());
        sceneManager = new SceneManager(this, mEngine, camera, textures);

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

    public void onPopulateScene(Scene scene, OnPopulateSceneCallback opsc) throws Exception {
        opsc.onPopulateSceneFinished();
    }

    public Textures getTextures() {
        return textures;
    }
}
