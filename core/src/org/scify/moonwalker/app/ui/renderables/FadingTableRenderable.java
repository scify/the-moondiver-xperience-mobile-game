package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.TableRenderable;
import org.scify.engine.renderables.effects.EffectSequence;
import org.scify.engine.renderables.effects.FadeEffect;
import org.scify.engine.renderables.effects.FunctionEffect;
import org.scify.engine.renderables.effects.VisibilityEffect;

public class FadingTableRenderable extends TableRenderable {
    protected ImageRenderable tableBGRenderable;

    public FadingTableRenderable(String type, String id) {
        super(type, id);
    }

    public FadingTableRenderable(float xPos, float yPos, float width, float height, String type, String id, String bgImagePath) {
        super(xPos, yPos, width, height, type, id);

        tableBGRenderable = new ImageRenderable("bg", bgImagePath);
    }

    public void fadeIn () {
        EffectSequence fadeInEffects = new EffectSequence();
        fadeInEffects.addEffect(new FadeEffect(1.0, 0.0, 0));
        fadeInEffects.addEffect(new VisibilityEffect(true));
        fadeInEffects.addEffect(new FadeEffect(0.0, 1.0, 2000));
        fadeInEffects.addEffect(new FunctionEffect(new Runnable() {
            @Override
            public void run() {
                performAfterFadeIn();
            }
        }));
        this.addEffect(fadeInEffects);
    }

    protected void performAfterFadeIn () {
        // Do nothing
    }

    public void fadeOut () {
        EffectSequence fadeOutEffects = new EffectSequence();
        fadeOutEffects.addEffect(new FadeEffect(1.0, 0.0, 2000));
        fadeOutEffects.addEffect(new VisibilityEffect(false));
        fadeOutEffects.addEffect(new FunctionEffect(new Runnable() {
            @Override
            public void run() {
                performAfterFadeOut();
            }
        }));
        this.addEffect(fadeOutEffects);

    }

    protected void performAfterFadeOut () {
        // Do nothing
    }

    public void init() {
        // Do nothing
    }
}
