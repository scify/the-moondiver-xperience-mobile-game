package org.scify.moonwalker.app.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.scify.moonwalker.app.helpers.ResourceLocator;

public class ThemeController {

    private BitmapFont font;
    private Skin skin;
    private ResourceLocator resourceLocator;

    public ThemeController() {
        this.resourceLocator = new ResourceLocator();
        initFontAndSkin();
    }

    public void initFontAndSkin() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(resourceLocator.getFilePath("fonts/Starjedi.ttf")));
        font = createFont(generator, 13);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        skin = new Skin();
        skin.add("default-font", font, BitmapFont.class);
        skin.addRegions(new TextureAtlas(Gdx.files.internal(resourceLocator.getFilePath("fonts/uiskin.atlas"))));
        skin.load(Gdx.files.internal(resourceLocator.getFilePath("fonts/uiskin.json")));
        generator.dispose();
    }

    private BitmapFont createFont(FreeTypeFontGenerator generator, float fontSize) {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        float screenDPI = 160.0f * Gdx.graphics.getDensity();
        int pixelSize = (int)(fontSize * screenDPI / 96.0f); // Reference size based on 96 DPI screen
        parameter.size = pixelSize;
        //Gdx.app.log(TAG, "Font size: "+pixelSize+"px");
        return generator.generateFont(parameter);
    }

    public BitmapFont getFont() {
        return font;
    }

    public Skin getSkin() {
        return skin;
    }

    public void dispose() {
        font.dispose();
    }
}
