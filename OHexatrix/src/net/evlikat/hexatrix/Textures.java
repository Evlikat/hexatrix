package net.evlikat.hexatrix;

import android.content.res.AssetManager;
import android.graphics.Color;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.IFont;
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
    private Font font;

    public Textures(final FontManager fontManager, final TextureManager textureManager, final AssetManager assetManager) {
        loadTextures(fontManager, textureManager, assetManager);
    }

    private void loadTextures(final FontManager fontManager, final TextureManager textureManager, final AssetManager assetManager) {
        this.borderBottom = loadTexture(textureManager, assetManager, "border", 180, 157);
        this.borderLeft = loadTexture(textureManager, assetManager, "border-left", 180, 157);
        this.borderRight = loadTexture(textureManager, assetManager, "border-right", 180, 157);
        this.hexagon0 = loadTexture(textureManager, assetManager, "hexagon0", 180, 157);
        this.hexagon1 = loadTexture(textureManager, assetManager, "hexagon1", 180, 157);
        this.brick = loadTexture(textureManager, assetManager, "brick", 180, 157);
        this.figure = loadTexture(textureManager, assetManager, "figure", 180, 157);
        this.shadow = loadTexture(textureManager, assetManager, "shadow", 180, 157);

        this.pause = loadTexture(textureManager, assetManager, "pause", 420, 364);
        this.startBtn = loadTexture(textureManager, assetManager, "start-btn", 420, 364);
        this.leadersBtn = loadTexture(textureManager, assetManager, "leaders-btn", 420, 364);
        this.quitBtn = loadTexture(textureManager, assetManager, "quit-btn", 420, 364);

        FontFactory.setAssetBasePath("font/");
        BitmapTextureAtlas fontTexture = new BitmapTextureAtlas(textureManager, 1024, 1024, TextureOptions.BILINEAR);
        this.font = FontFactory.createFromAsset(fontManager, fontTexture,
            assetManager, "consola.ttf", 144, true, Color.BLACK);
        this.font.load();
    }

    private TextureRegion loadTexture(final TextureManager textureManager,
                                      final AssetManager assetManager,
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

    public IFont getFont() {
        return font;
    }
}
