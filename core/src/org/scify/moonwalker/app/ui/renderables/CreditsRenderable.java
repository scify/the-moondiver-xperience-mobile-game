package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.TextLabelRenderable;
import org.scify.engine.renderables.effects.*;

import java.util.HashSet;
import java.util.Set;

public class CreditsRenderable extends FadingTableRenderable {
    //renderable image paths
    protected static final String ABOUT_BG_IMG_PATH_1 = "img/episode_credits/bg1.png";
    protected static final String ABOUT_BG_IMG_PATH_2 = "img/episode_credits/bg2.png";

    //renderable ids
    protected static final String ABOUT_BG_ID_1 = "aboutBG1";
    protected static final String ABOUT_BG_ID_2 = "aboutBG2";

    protected ImageRenderable aboutBGRenderable1;
    protected ImageRenderable aboutBGRenderable2;

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

        aboutBGRenderable1 = createImageRenderable(ABOUT_BG_ID_1, ABOUT_BG_IMG_PATH_1, false, true, 1);
        allRenderables.add(aboutBGRenderable1);

        aboutBGRenderable2 = createImageRenderable(ABOUT_BG_ID_2, ABOUT_BG_IMG_PATH_2, false, false, 1);
        allRenderables.add(aboutBGRenderable2);
    }

    public boolean isReadyForInput() {
        return inputEnabled;
    }

    public ImageRenderable getTableBGRenderable() {
        return tableBGRenderable;
    }

    public void setInputEnabled(boolean inputEnabled) { this.inputEnabled = inputEnabled; }

    public ImageRenderable getAboutBGRenderable1() { return aboutBGRenderable1; }

    public ImageRenderable getAboutBGRenderable2() {
        return aboutBGRenderable2;
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
                aboutBGRenderable2.addEffect(aboutLabelEffects);
            }
        }));
        aboutBGRenderable1.addEffect(aboutBGEffects);
    }

    public int getAboutMode() {
        return aboutMode;
    }
}
