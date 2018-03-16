package org.scify.moonwalker.app.ui;

import com.badlogic.gdx.Gdx;
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

    public ThemeController() {
        this.resourceLocator = new ResourceLocator();
        this.appInfo = AppInfo.getInstance();
        initFontAndSkin();
    }

    protected void initFontAndSkin() {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters=FreeTypeFontGenerator.DEFAULT_CHARS + "α  β  γ  δ  ε  ζ  η  θ  ι  κ  λ  μ  ν  ξ  ο  π  ρ  σ  τ  υ  ϕ  χ  ψ  ω ς Α  Β  Γ  Δ  Ε  Ζ  Η  Θ  Ι  Κ  Λ  Μ  Ν  Ξ  Ο  Π  Ρ  Σ  Τ  Υ  Φ  Χ  Ψ  Ω ά έ ή ί ό ύ ώ Ά Έ Ή Ί Ό Ύ Ώ";
        parameter.size = (int) appInfo.pixelsWithDensity(22);
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.minFilter = Texture.TextureFilter.Linear;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(resourceLocator.getFilePath("fonts/Anonymous_Pro.ttf")));
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
