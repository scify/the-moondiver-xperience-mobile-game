package org.scify.moonwalker.app.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
        parameter.characters=FreeTypeFontGenerator.DEFAULT_CHARS + "α  β  γ  δ  ε  ζ  η  θ  ι  κ  λ  μ  ν  ξ  ο  π  ρ  σ  τ  υ  ϕ  χ  ψ  ω ς Α  Β  Γ  Δ  Ε  Ζ  Η  Θ  Ι  Κ  Λ  Μ  Ν  Ξ  Ο  Π  Ρ  Σ  Τ  Υ  Φ  Χ  Ψ  Ω ά έ ή ί ό ύ ώ Ά Έ Ή Ί Ό Ύ Ώ";
        int width = appInfo.getScreenWidth();
        float ratio = width /731f;
        parameter.size = (int)(ratio * fontSize);
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.minFilter = Texture.TextureFilter.Linear;
        FreeTypeFontGenerator generator;
        if (fontId.equals("dialog")) {
            //generator = new FreeTypeFontGenerator(Gdx.files.internal(resourceLocator.getFilePath("fonts/CYN_EdraMedium.otf")));
            generator = new FreeTypeFontGenerator(Gdx.files.internal(resourceLocator.getFilePath("fonts/CYN_EdraFY.otf")));
            //generator = new FreeTypeFontGenerator(Gdx.files.internal(resourceLocator.getFilePath("fonts/PVF_ClimaxTXT.otf")));
        }else if (fontId.equals("controls")){
            generator = new FreeTypeFontGenerator(Gdx.files.internal(resourceLocator.getFilePath("fonts/PVF_ClimaxTXT.otf")));
            //generator = new FreeTypeFontGenerator(Gdx.files.internal(resourceLocator.getFilePath("fonts/CYN_EdraFY.otf")));
        }else {
            generator = new FreeTypeFontGenerator(Gdx.files.internal(resourceLocator.getFilePath("fonts/CYN_EdraFY.otf")));
        }
        //FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(resourceLocator.getFilePath("fonts/Anonymous_Pro.ttf")));
        //FreeTypeFontGenerator
        font = generator.generateFont(parameter);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        skin = new Skin();
        skin.add("default-font", font, BitmapFont.class);
        skin.addRegions(new TextureAtlas(Gdx.files.internal(resourceLocator.getFilePath("fonts/uiskin.atlas"))));
        skin.load(Gdx.files.internal(resourceLocator.getFilePath("fonts/uiskin.json")));
        generator.dispose();
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
