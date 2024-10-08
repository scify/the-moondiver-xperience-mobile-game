package org.scify.moonwalker.app.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

public class ThemeController {
    public static final String SKIN_DEFAULT = "fonts/uiskin.atlas";
    public static final String SKIN_CORRECT = "fonts/uiskin_correctAnswer.atlas";
    public static final String SKIN_WRONG = "fonts/uiskin_wrongAnswer.atlas";

    private Skin defaultSkin;
    protected Skin wrongAnswerSkin;
    protected Skin correctAnswerSkin;

    private ResourceLocator resourceLocator;
    private AppInfo appInfo;
    private static ThemeController instance;
    private Map<String, Map<Integer, FontData>> fonts;

    public static ThemeController getInstance() {
        if (instance == null) {
            instance = new ThemeController();
        }
        return instance;
    }

    protected ThemeController() {
        this.resourceLocator = ResourceLocator.getInstance();
        this.appInfo = AppInfo.getInstance();
        fonts = new HashMap<>();
        defaultSkin = new Skin();
        setDefaultFontOfSkin(defaultSkin, SKIN_DEFAULT);

        wrongAnswerSkin = new Skin();
        setDefaultFontOfSkin(wrongAnswerSkin, SKIN_WRONG);

        correctAnswerSkin = new Skin();
        setDefaultFontOfSkin(correctAnswerSkin, SKIN_CORRECT);
    }

    protected FontData initFontAndSkin(Skin skin, int fontSize, String fontId, String sSkin) {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "α  β  γ  δ  ε  ζ  η  θ  ι  κ  λ  μ  ν  ξ  ο  π  ρ  σ  τ  υ  φ  χ  ψ  ω  ς  Α  Β  Γ  Δ  Ε  Ζ  Η  Θ  Ι  Κ  Λ  Μ  Ν  Ξ  Ο  Π  Ρ  Σ  Τ  Υ  Φ  Χ  Ψ  Ω ϊ ά έ ή ί ό ύ ώ ΐ ΰ Ά Έ Ή Ί Ό Ύ Ώ ' \"";
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

        skin.add("default-font", font, BitmapFont.class);
        TextureAtlas textureAtlas;
        textureAtlas = new TextureAtlas(Gdx.files.internal(resourceLocator.getFilePath(sSkin)));

        skin.addRegions(textureAtlas);
        skin.load(Gdx.files.internal(resourceLocator.getFilePath("fonts/uiskin.json")));
        generator.dispose();

        return new FontData(textureAtlas, font);
    }

    public BitmapFont getFont(int fontSize, String fontId, String skin) {
        if (fonts.containsKey(fontId)) {
            Map<Integer, FontData> sizeToFont = fonts.get(fontId);
            if (sizeToFont.containsKey(fontSize))
                return sizeToFont.get(fontSize).getFont();
            else {
                FontData font = initFontAndSkin(new Skin(), fontSize, fontId, skin);
                sizeToFont.put(fontSize, font);
                return font.getFont();
            }
        } else {
            FontData font = initFontAndSkin(new Skin(), fontSize, fontId, skin);
            Map<Integer, FontData> sizeToFont = new HashMap<>();
            sizeToFont.put(fontSize, font);
            fonts.put(fontId, sizeToFont);
            return font.getFont();
        }
    }

    protected void setDefaultFontOfSkin(Skin skin, String sSkinType) {
        FontData font = initFontAndSkin(skin, 20, "dialog", sSkinType);
        Map<Integer, FontData> sizeToFont = new HashMap<>();
        sizeToFont.put(20, font);
        fonts.put("dialog", sizeToFont);
    }

    public BitmapFont getFont() {
        return getFont(20, "dialog", SKIN_DEFAULT);
    }

    public Skin getDefaultSkin() {
        return defaultSkin;
    }

    public Skin getSkinByType(String skinType) {
        switch (skinType) {
            case SKIN_DEFAULT:
                return defaultSkin;
            case SKIN_CORRECT:
                return correctAnswerSkin;
            case SKIN_WRONG:
                return wrongAnswerSkin;
            default:
                throw new InvalidParameterException("Cannot find skin for skin-type " + skinType);
        }
    }

    public Skin getWrongAnswerSkin() {
        return wrongAnswerSkin;
    }

    public void dispose() {
        for (Map.Entry<String, Map<Integer, FontData>> fontMap : fonts.entrySet()) {
            for (Map.Entry<Integer, FontData> font : fontMap.getValue().entrySet()) {
                font.getValue().dispose();
            }
        }
        fonts.clear();
        defaultSkin = null;
        wrongAnswerSkin = null;
        instance = null;
    }

}
