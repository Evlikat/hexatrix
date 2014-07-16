package net.evlikat.hexatrix;

import android.content.res.AssetManager;
import java.io.IOException;
import java.io.InputStream;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.util.adt.io.in.IInputStreamOpener;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 02, 2014)
 */
public class Textures {

    private TextureRegion hexagon0;
    private TextureRegion hexagon1;
    private TextureRegion borderBottom;
    private TextureRegion borderLeft;
    private TextureRegion borderRight;
    private TextureRegion figure;
    private TextureRegion startBtn;
    private TextureRegion leadersBtn;
    private TextureRegion quitBtn;
    //
    private TextureRegion background;

    public Textures(final TextureManager textureManager, final AssetManager assetManager) {
        loadTextures(textureManager, assetManager);
    }

    private void loadTextures(final TextureManager textureManager, final AssetManager assetManager) {
        this.borderBottom = loadTexture(textureManager, assetManager, "border");
        this.borderLeft = loadTexture(textureManager, assetManager, "border-left");
        this.borderRight = loadTexture(textureManager, assetManager, "border-right");
        this.hexagon0 = loadTexture(textureManager, assetManager, "hexagon0");
        this.hexagon1 = loadTexture(textureManager, assetManager, "hexagon1");
        this.figure = loadTexture(textureManager, assetManager, "figure");
        this.startBtn = loadTexture(textureManager, assetManager, "start-btn");
        this.leadersBtn = loadTexture(textureManager, assetManager, "leaders-btn");
        this.quitBtn = loadTexture(textureManager, assetManager, "quit-btn");
        //
        this.background = loadTexture(textureManager, assetManager, "background");
    }

    private TextureRegion loadTexture(final TextureManager textureManager, final AssetManager assetManager, final String id) {
        try {
            ITexture texture = new BitmapTexture(textureManager, new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return assetManager.open("gfx/" + id + ".png");
                }
            });
            texture.load();
            return TextureRegionFactory.extractFromTexture(texture);
        } catch (IOException ex) {
            throw new RuntimeException("Can not load texture for: " + id);
        }
    }

    public TextureRegion getHexagon0() {
        return hexagon0;
    }

    public TextureRegion getHexagon1() {
        return hexagon1;
    }

    public TextureRegion getBorderLeft() {
        return borderLeft;
    }

    public TextureRegion getBorderRight() {
        return borderRight;
    }

    public TextureRegion getBorderBottom() {
        return borderBottom;
    }

    public TextureRegion getFigure() {
        return figure;
    }

    public TextureRegion getStartBtn() {
        return startBtn;
    }

    public TextureRegion getLeadersBtn() {
        return leadersBtn;
    }

    public TextureRegion getQuitBtn() {
        return quitBtn;
    }

    public TextureRegion getBackground() {
        return background;
    }
}
