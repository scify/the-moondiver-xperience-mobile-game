package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.TableRenderable;
import org.scify.engine.renderables.effects.EffectSequence;
import org.scify.engine.renderables.effects.FadeEffect;
import org.scify.engine.renderables.effects.FunctionEffect;
import org.scify.engine.renderables.effects.VisibilityEffect;

public class FadingTableRenderable extends TableRenderable {
    protected Runnable beforeFadeIn;
    protected Runnable afterFadeIn;
    protected Runnable beforeFadeOut;
    protected Runnable afterFadeOut;

    public void setBeforeFadeOut(Runnable beforeFadeOut) {
        this.beforeFadeOut = beforeFadeOut;
    }

    public void setAfterFadeOut(Runnable afterFadeOut) {
        this.afterFadeOut = afterFadeOut;
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
        if (beforeFadeIn != null) {
            beforeFadeIn.run();
        }
        // TODO: Agree on before fade in and after fade in

        EffectSequence fadeInEffects = new EffectSequence();
        fadeInEffects.addEffect(new FunctionEffect(new Runnable() {
            @Override
            public void run() {
                if (beforeFadeIn != null) {
                    beforeFadeIn.run();
                }
            }
        }));
        fadeInEffects.addEffect(new FadeEffect(1.0, 0.0, 0));
        fadeInEffects.addEffect(new VisibilityEffect(true));
        fadeInEffects.addEffect(new FadeEffect(0.0, 1.0, 2000));
        fadeInEffects.addEffect(new FunctionEffect(new Runnable() {
            @Override
            public void run() {
                if (afterFadeIn != null) {
                    afterFadeIn.run();
                }
            }
        }));
        this.addEffect(fadeInEffects);
    }

    public void setBeforeFadeIn(Runnable beforeFadeIn) {
        this.beforeFadeIn = beforeFadeIn;
    }

    public void setAfterFadeIn(Runnable afterFadeIn) {
        this.afterFadeIn = afterFadeIn;
    }

    public void fadeOut () {
        // TODO: Agree on before fade out and after fade out


        EffectSequence fadeOutEffects = new EffectSequence();
        fadeOutEffects.addEffect(new FunctionEffect(new Runnable() {
            @Override
            public void run() {
                if (beforeFadeOut != null) {
                    beforeFadeOut.run();
                }
            }
        }));
        fadeOutEffects.addEffect(new FadeEffect(1.0, 0.0, 2000));
        fadeOutEffects.addEffect(new VisibilityEffect(false));
        fadeOutEffects.addEffect(new FunctionEffect(new Runnable() {
            @Override
            public void run() {
                if (afterFadeOut != null) {
                    afterFadeOut.run();
                }
            }
        }));
        this.addEffect(fadeOutEffects);

    }
}
