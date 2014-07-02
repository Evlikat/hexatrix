package net.evlikat.hexatrix;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import net.evlikat.hexatrix.scenes.SceneManager;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    
    private SceneManager sceneManager;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            );            
            sceneManager = new SceneManager(this);
            sceneManager.start();
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sceneManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();        
        sceneManager.onResume();
    }
}
