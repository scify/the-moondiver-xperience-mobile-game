package org.scify.moonwalker.app.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;

public class ThemeController {

    private BitmapFont font;
    private Skin skin;
    private ResourceLocator resourceLocator;
    private AppInfo appInfo;
    private TextureAtlas textureAtlas;

    public ThemeController(int fontSize, String fontId) {
        this.resourceLocator = new ResourceLocator();
        this.appInfo = AppInfo.getInstance();
        initFontAndSkin(fontSize, fontId);
    }

    public ThemeController(int fontSize) {
        this.resourceLocator = new ResourceLocator();
        this.appInfo = AppInfo.getInstance();
        initFontAndSkin(fontSize, "dialog");
    }

    public ThemeController() {
        this.resourceLocator = new ResourceLocator();
        this.appInfo = AppInfo.getInstance();
        initFontAndSkin(20, "dialog");
    }

    protected void initFontAndSkin(int fontSize, String fontId) {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters=FreeTypeFontGenerator.DEFAULT_CHARS + "α  β  γ  δ  ε  ζ  η  θ  ι  κ  λ  μ  ν  ξ  ο  π  ρ  σ  τ  υ  φ  χ  ψ  ω  ς  Α  Β  Γ  Δ  Ε  Ζ  Η  Θ  Ι  Κ  Λ  Μ  Ν  Ξ  Ο  Π  Ρ  Σ  Τ  Υ  Φ  Χ  Ψ  Ω ά έ ή ί ό ύ ώ ΐ ΰ Ά Έ Ή Ί Ό Ύ Ώ";
        int width = appInfo.getScreenWidth();
        float ratio = width /731f;
        parameter.size = (int)(ratio * fontSize);
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.minFilter = Texture.TextureFilter.Linear;
        FreeTypeFontGenerator generator;
        switch (fontId) {
            case "dialog":
                generator = new FreeTypeFontGenerator(Gdx.files.internal(resourceLocator.getFilePath("fonts/CYN_EdraNR.otf")));
                break;
            case "controls":
                generator = new FreeTypeFontGenerator(Gdx.files.internal(resourceLocator.getFilePath("fonts/PVF_ClimaxTXT.otf")));
                break;
            default:
                generator = new FreeTypeFontGenerator(Gdx.files.internal(resourceLocator.getFilePath("fonts/CYN_EdraNR.otf")));
                break;
        }
        if(font != null) {
            System.out.println("disposing font");
            dispose();
        }
        font = generator.generateFont(parameter);
        font.getData().markupEnabled = true;
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        skin = new Skin();
        skin.add("default-font", font, BitmapFont.class);
        textureAtlas = new TextureAtlas(Gdx.files.internal(resourceLocator.getFilePath("fonts/uiskin.atlas")));
        skin.addRegions(textureAtlas);
        skin.load(Gdx.files.internal(resourceLocator.getFilePath("fonts/uiskin.json")));
        generator.dispose();
    }

    public BitmapFont getFont() {
        return font;
    }

    public Skin getSkin() {
        return skin;
    }

    protected void dispose() {
        font.dispose();
        textureAtlas.dispose();
        skin.dispose();
    }

}
