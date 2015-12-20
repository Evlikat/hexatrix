package net.evlikat.hexatrix.utils;

import org.andengine.entity.scene.background.Background;

public class SpriteUtils {

    public static final Background BACKGROUND = createBackground(253, 234, 202);

    public static Background createBackground(int r, int g, int b) {
        return new Background((float) r / 255, (float) g / 255, (float) b / 255);
    }
}
