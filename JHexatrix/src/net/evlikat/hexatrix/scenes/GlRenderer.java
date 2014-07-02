package net.evlikat.hexatrix.scenes;

import android.opengl.GLSurfaceView.Renderer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 02, 2014)
 */
public class GlRenderer implements Renderer {

    private final MainView mainView;

    public GlRenderer(MainView mainView) {
        this.mainView = mainView;
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig eglc) {
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
    }

    public void onSurfaceChanged(GL10 gl, int i, int i1) {
        mainView.checkInit(gl);
    }

    public void onDrawFrame(GL10 gl) {
        // clear Screen and Depth Buffer
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Reset the Modelview Matrix
        gl.glLoadIdentity();

        // Drawing
        gl.glTranslatef(0.0f, 0.0f, -5.0f);
        mainView.draw(gl);
    }

}
