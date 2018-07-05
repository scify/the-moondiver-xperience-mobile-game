package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.TextLabelRenderable;
import java.util.HashSet;
import java.util.Set;

public class CreditsRenderable extends FadingTableRenderable {
    protected static final String ABOUT_TEXT = "Το παιχνίδι The Moondiver Xperience δημιουργήθηκε από τη SciFY στο Πλαίσιο του προγράμματος Stem Powering Youth με την ευγενική χορηγία του Ιδρύματος Vodafone.\n" +
            "\nGame creation: SciFY\n" +
            "Εικονογράφηση: Λέλα Στρούτση\n" +
            "Sounds/Audio FX: Λευτέρης Δούρος / Άννα Δρόσου\n" +
            "Fonts: Petros Vassiadis";
    //renderable image paths
    protected static final String ABOUT_BG_IMG_PATH = "img/episode_charge/conversation_bg.png";

    //renderable ids
    protected static final String ABOUT_LABEL_ID = "aboutLabel";
    protected static final String ABOUT_BG_ID = "aboutBG";

    protected ImageRenderable aboutBGRenderable;
    protected TextLabelRenderable aboutLabel;

    protected Set<Renderable> allRenderables;

    public Set<Renderable> getAllRenderables() {
        return allRenderables;
    }

    protected boolean inputEnabled;

    public CreditsRenderable(float xPos, float yPos, float width, float height, String id) {
        super(xPos, yPos, width, height, ACTOR_EPISODE_CREDITS, id, MainMenuRenderable.BG_IMG_PATH);
        inputEnabled = false;
        initSubRenderables();
    }

    private void initSubRenderables() {
        allRenderables = new HashSet<>();

        aboutLabel = createTextLabelRenderable(ABOUT_LABEL_ID, ABOUT_TEXT, false, true, 2);
        allRenderables.add(aboutLabel);

        aboutBGRenderable = createImageRenderable(ABOUT_BG_ID, ABOUT_BG_IMG_PATH, false, true, 1);
        allRenderables.add(aboutBGRenderable);
    }

    public boolean isReadyForInput() {
        return inputEnabled;
    }

    public ImageRenderable getTableBGRenderable() {
        return tableBGRenderable;
    }

    public void setInputEnabled(boolean inputEnabled) { this.inputEnabled = inputEnabled; }

    public TextLabelRenderable getAboutLabel() { return aboutLabel; }

    public ImageRenderable getAboutBGRenderable() { return aboutBGRenderable; }
}
