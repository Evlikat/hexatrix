package net.evlikat.hexatrix;

import android.content.res.AssetManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 02, 2014)
 */
public class Textures {

    private TextureRegion hexagon0;
    private TextureRegion hexagon1;
    private TextureRegion borderBottom;
    private TextureRegion brick;
    private TextureRegion borderLeft;
    private TextureRegion borderRight;
    private TextureRegion figure;
    private TextureRegion shadow;
    private TextureRegion pause;
    private TextureRegion startBtn;
    private TextureRegion leadersBtn;
    private TextureRegion quitBtn;

    public Textures(final TextureManager textureManager, final AssetManager assetManager) {
        loadTextures(textureManager, assetManager);
    }

    private void loadTextures(final TextureManager textureManager, final AssetManager assetManager) {
        this.borderBottom = loadTexture(textureManager, assetManager, "border", 420, 364);
        this.borderLeft = loadTexture(textureManager, assetManager, "border-left", 420, 364);
        this.borderRight = loadTexture(textureManager, assetManager, "border-right", 420, 364);
        this.hexagon0 = loadTexture(textureManager, assetManager, "hexagon0", 420, 364);
        this.hexagon1 = loadTexture(textureManager, assetManager, "hexagon1", 420, 364);
        this.brick = loadTexture(textureManager, assetManager, "brick", 420, 364);
        this.figure = loadTexture(textureManager, assetManager, "figure", 420, 364);
        this.pause = loadTexture(textureManager, assetManager, "pause", 420, 364);
        this.startBtn = loadTexture(textureManager, assetManager, "start-btn", 420, 364);
        this.leadersBtn = loadTexture(textureManager, assetManager, "leaders-btn", 420, 364);
        this.quitBtn = loadTexture(textureManager, assetManager, "quit-btn", 420, 364);
        this.shadow = loadTexture(textureManager, assetManager, "shadow", 420, 364);
    }

    private TextureRegion loadTexture(final TextureManager textureManager, final AssetManager assetManager,
                                      final String id, final int width, final int height) {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        final BitmapTextureAtlas texture = new BitmapTextureAtlas(textureManager, width, height, TextureOptions.REPEATING_BILINEAR_PREMULTIPLYALPHA);
        final TextureRegion textureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(texture, assetManager, id + ".png", 0, 0);
        texture.load();
        return textureRegion;
    }

    public TextureRegion getBrick() {
        return brick;
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

    public TextureRegion getPause() {
        return pause;
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

    public TextureRegion getShadow() {
        return shadow;
    }
}
