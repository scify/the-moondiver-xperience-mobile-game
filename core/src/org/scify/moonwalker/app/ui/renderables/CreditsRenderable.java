package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.TextLabelRenderable;
import org.scify.engine.renderables.effects.*;

import java.util.HashSet;
import java.util.Set;

public class CreditsRenderable extends FadingTableRenderable {
    protected static final String ABOUT_TEXT1 = "[BLUE]Το παιχνίδι The Moondiver Xperience δημιουργήθηκε από τη SciFY στο Πλαίσιο του προγράμματος Stem Powering Youth με την ευγενική χορηγία του Ιδρύματος Vodafone.";

    protected static final String ABOUT_TEXT2 = "[BLUE]Game creation: SciFY\n" +
            "Εικονογράφηση: Λέλα Στρούτση\n" +
            "Sounds/Audio FX: Λευτέρης Δούρος / Άννα Δρόσου\n" +
            "Fonts: Petros Vassiadis";
    //renderable image paths
    protected static final String ABOUT_BG_IMG_PATH = "img/episode_credits/bg.png";
    protected static final String ABOUT_SCIFY_IMG_PATH = "img/episode_credits/SciFY.png";
    protected static final String ABOUT_VODAFONE_IMG_PATH = "img/episode_credits/vodafone.png";
    protected static final String ABOUT_STEM_YOUTH_IMG_PATH = "img/episode_credits/stem_youth.png";

    //renderable ids
    protected static final String ABOUT_LABEL1_ID = "aboutLabel1";
    protected static final String ABOUT_LABEL2_ID = "aboutLabel2";
    protected static final String ABOUT_SCIFY_IMG_ID = "scify";
    protected static final String ABOUT_VODAFONE_IMG_ID = "vodafone";
    protected static final String ABOUT_STEM_YOUTH_IMG_ID = "stem_youth";
    protected static final String ABOUT_BG_ID = "aboutBG";

    protected ImageRenderable aboutBGRenderable;
    protected ImageRenderable aboutSciFYRenderable;
    protected ImageRenderable aboutVodafoneRenderable;
    protected ImageRenderable aboutStemYouthRenderable;
    protected TextLabelRenderable aboutLabel1;
    protected TextLabelRenderable aboutLabel2;

    protected Set<Renderable> allRenderables;

    public Set<Renderable> getAllRenderables() {
        return allRenderables;
    }

    protected boolean inputEnabled;
    protected int aboutMode;

    public CreditsRenderable(float xPos, float yPos, float width, float height, String id) {
        super(xPos, yPos, width, height, ACTOR_EPISODE_CREDITS, id, MainMenuRenderable.BG_IMG_PATH);
        inputEnabled = false;
        aboutMode = 1;
        initSubRenderables();
    }

    private void initSubRenderables() {
        allRenderables = new HashSet<>();

        aboutBGRenderable = createImageRenderable(ABOUT_BG_ID, ABOUT_BG_IMG_PATH, false, true, 1);
        allRenderables.add(aboutBGRenderable);

        aboutLabel1 = createTextLabelRenderable(ABOUT_LABEL1_ID, ABOUT_TEXT1, false, true, 2);
        allRenderables.add(aboutLabel1);

        aboutLabel2 = createTextLabelRenderable(ABOUT_LABEL2_ID, ABOUT_TEXT2, false, false, 3);
        allRenderables.add(aboutLabel2);

        aboutSciFYRenderable = createImageRenderable(CreditsRenderable.ABOUT_SCIFY_IMG_ID, CreditsRenderable.ABOUT_SCIFY_IMG_PATH, false, true, 2);
        allRenderables.add(aboutSciFYRenderable);

        aboutVodafoneRenderable = createImageRenderable(CreditsRenderable.ABOUT_VODAFONE_IMG_ID, CreditsRenderable.ABOUT_VODAFONE_IMG_PATH, false, true, 2);
        allRenderables.add(aboutVodafoneRenderable);

        aboutStemYouthRenderable = createImageRenderable(CreditsRenderable.ABOUT_STEM_YOUTH_IMG_ID, CreditsRenderable.ABOUT_STEM_YOUTH_IMG_PATH, false, true, 2);
        allRenderables.add(aboutStemYouthRenderable);
    }

    public boolean isReadyForInput() {
        return inputEnabled;
    }

    public ImageRenderable getTableBGRenderable() {
        return tableBGRenderable;
    }

    public void setInputEnabled(boolean inputEnabled) { this.inputEnabled = inputEnabled; }

    public TextLabelRenderable getAboutLabel1() { return aboutLabel1; }

    public TextLabelRenderable getAboutLabel2() { return aboutLabel2; }

    public ImageRenderable getAboutBGRenderable() { return aboutBGRenderable; }

    public ImageRenderable getAboutSciFYRenderable() {
        return aboutSciFYRenderable;
    }

    public ImageRenderable getAboutVodafoneRenderable() {
        return aboutVodafoneRenderable;
    }

    public ImageRenderable getAboutStemYouthRenderable() {
        return aboutStemYouthRenderable;
    }

    public void showAbout2() {
        inputEnabled = false;
        final EffectSequence aboutBGEffects = new EffectSequence();
        aboutBGEffects.addEffect(new DelayEffect(300));
        aboutBGEffects.addEffect(new FadeEffect(1.0, 0.0, 300));
        aboutBGEffects.addEffect(new VisibilityEffect(false));
        aboutBGEffects.addEffect(new FunctionEffect(new Runnable() {
            @Override
            public void run() {
                EffectSequence aboutLabelEffects = new EffectSequence();
                aboutLabelEffects.addEffect(new FadeEffect(1.0, 0.0, 0.0));
                aboutLabelEffects.addEffect(new VisibilityEffect(true));
                aboutLabelEffects.addEffect(new FadeEffect(0, 1.0, 300));
                aboutLabelEffects.addEffect(new FunctionEffect(new Runnable() {
                    @Override
                    public void run() {
                        inputEnabled = true;
                        aboutMode = 2;
                    }
                }));
                aboutLabel2.addEffect(aboutLabelEffects);
            }
        }));
        aboutLabel1.addEffect(aboutBGEffects);
    }

    public int getAboutMode() {
        return aboutMode;
    }
}
