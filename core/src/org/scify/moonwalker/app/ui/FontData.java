package org.scify.moonwalker.app.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
public class FontData {

    protected TextureAtlas textureAtlas;
    protected BitmapFont font;

    public FontData(TextureAtlas textureAtlas, BitmapFont font) {
        this.textureAtlas = textureAtlas;
        this.font = font;
    }

    public BitmapFont getFont(){
        return font;
    }

    public void dispose() {
        font.dispose();
        textureAtlas.dispose();
    }
}
