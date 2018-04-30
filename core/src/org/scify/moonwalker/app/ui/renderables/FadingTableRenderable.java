package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.TableRenderable;
import org.scify.engine.renderables.effects.EffectSequence;
import org.scify.engine.renderables.effects.FadeEffect;
import org.scify.engine.renderables.effects.FunctionEffect;
import org.scify.engine.renderables.effects.VisibilityEffect;

import java.util.ArrayList;
import java.util.List;

public class FadingTableRenderable extends TableRenderable {
    protected List<Runnable> beforeFadeIn = new ArrayList<>();
    protected List<Runnable> afterFadeIn = new ArrayList<>();
    protected List<Runnable> beforeFadeOut = new ArrayList<>();
    protected List<Runnable> afterFadeOut = new ArrayList<>();

    public void addBeforeFadeOut(Runnable beforeFadeOut) {
        this.beforeFadeOut.add(beforeFadeOut);
    }

    public void addAfterFadeOut(Runnable afterFadeOut) {
        this.afterFadeOut.add(afterFadeOut);
    }

    public FadingTableRenderable(String type, String id) {
        super(type, id);
    }

    /**
     * Create a fading renderable that starts in invisible state.
     * @param xPos The x position.
     * @param yPos The y position.
     * @param width The width of the renderable.
     * @param height The height of the renderable.
     * @param type The type of the renderable.
     * @param id The id of the renderable.
     * @param bgImagePath The background image path.
     */
    public FadingTableRenderable(float xPos, float yPos, float width, float height, String type, String id, String bgImagePath) {
        super(xPos, yPos, width, height, type, id, bgImagePath);
        setVisible(false);
    }

    public FadingTableRenderable(float xPos, float yPos, float width, float height, String type, String id, String bgImagePath, boolean bStartVisibility) {
        super(xPos, yPos, width, height, type, id);

        tableBGRenderable = new ImageRenderable("bg", bgImagePath);
        setVisible(bStartVisibility);
    }

    public void fadeIn () {
        EffectSequence fadeInEffects = new EffectSequence();
        // Add before effects
        for (final Runnable rCur : beforeFadeIn) {
            fadeInEffects.addEffect(new FunctionEffect(rCur));
        }
        // Add actual fade effect
        fadeInEffects.addEffect(new FadeEffect(1.0, 0.0, 0));
        fadeInEffects.addEffect(new VisibilityEffect(true));
        fadeInEffects.addEffect(new FadeEffect(0.0, 1.0, 2000));
        // Add after effects
        for (final Runnable rCur : afterFadeIn) {
            fadeInEffects.addEffect(new FunctionEffect(rCur));
        }
        this.addEffect(fadeInEffects);
    }



    public void setBeforeFadeIn(Runnable beforeFadeIn) {
        this.beforeFadeIn.add(beforeFadeIn);
    }

    public void addAfterFadeIn(Runnable afterFadeIn) {
        this.afterFadeIn.add(afterFadeIn);
    }

    public void fadeOut () {
        EffectSequence fadeOutEffects = new EffectSequence();
        // Add before effects
        for (final Runnable rCur : beforeFadeOut) {
            fadeOutEffects.addEffect(new FunctionEffect(rCur));
        }
        // Add actutal fade effects
        fadeOutEffects.addEffect(new FadeEffect(1.0, 0.0, 2000));
        fadeOutEffects.addEffect(new VisibilityEffect(false));

        // Add after effects
        for (final Runnable rCur : afterFadeOut) {
            fadeOutEffects.addEffect(new FunctionEffect(rCur));
        }
        this.addEffect(fadeOutEffects);

    }
}
