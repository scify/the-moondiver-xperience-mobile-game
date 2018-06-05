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

import java.util.HashMap;
import java.util.Map;

public class ThemeController {

    private Skin skin;
    private ResourceLocator resourceLocator;
    private AppInfo appInfo;
    private TextureAtlas textureAtlas;
    private static ThemeController instance;
    private Map<String, Map<Integer, BitmapFont>> fonts;

    public static ThemeController getThemeController() {
        if (instance == null) {
            instance = new ThemeController();
            return instance;
        } else
            return instance;
    }

    protected ThemeController() {
        this.resourceLocator = new ResourceLocator();
        this.appInfo = AppInfo.getInstance();
        fonts = new HashMap<>();
    }

    protected BitmapFont initFontAndSkin(int fontSize, String fontId) {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "α  β  γ  δ  ε  ζ  η  θ  ι  κ  λ  μ  ν  ξ  ο  π  ρ  σ  τ  υ  φ  χ  ψ  ω  ς  Α  Β  Γ  Δ  Ε  Ζ  Η  Θ  Ι  Κ  Λ  Μ  Ν  Ξ  Ο  Π  Ρ  Σ  Τ  Υ  Φ  Χ  Ψ  Ω ϊ ά έ ή ί ό ύ ώ ΐ ΰ Ά Έ Ή Ί Ό Ύ Ώ";
        int width = appInfo.getScreenWidth();
        float ratio = width / 731f;
        parameter.size = (int) (ratio * fontSize);
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
        BitmapFont font = generator.generateFont(parameter);
        font.getData().markupEnabled = true;
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        skin = new Skin();
        skin.add("default-font", font, BitmapFont.class);
        textureAtlas = new TextureAtlas(Gdx.files.internal(resourceLocator.getFilePath("fonts/uiskin.atlas")));
        skin.addRegions(textureAtlas);
        skin.load(Gdx.files.internal(resourceLocator.getFilePath("fonts/uiskin.json")));
        generator.dispose();
        return font;
    }

    public BitmapFont getFont(int fontSize) {
        return getFont(fontSize, "dialog");
    }

    public BitmapFont getFont(int fontSize, String fontId) {
        if (fonts.containsKey(fontId)) {
            Map<Integer, BitmapFont> sizeToFont = fonts.get(fontId);
            if (sizeToFont.containsKey(fontSize))
                return sizeToFont.get(fontSize);
            else {
                BitmapFont font = initFontAndSkin(fontSize, fontId);
                sizeToFont.put(fontSize, font);
                return font;
            }
        }else {
            BitmapFont font = initFontAndSkin(fontSize, fontId);
            Map<Integer, BitmapFont> sizeToFont = new HashMap<>();
            sizeToFont.put(fontSize, font);
            return font;
        }
    }

    public BitmapFont getFont() {
        return getFont(20, "dialog");
    }

    public Skin getSkin() {
        return skin;
    }

    /*protected void dispose() {
        font.dispose();
        textureAtlas.dispose();
        skin.dispose();
    }*/

}
