package org.scify.engine.renderables.effects.libgdx;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.EffectSequence;
import org.scify.engine.renderables.effects.EffectTarget;

public class LGDXEffectSequence extends EffectSequence implements LGDXEffect {
    public LGDXEffectSequence(EffectSequence eSource, LGDXEffectFactory factory) {
        // If no factory is provided
        if (factory == null)
            // use default one
            factory = LGDXEffectFactory.getFactorySingleton();

        // For each effect
        for (Effect eCur: eSource.getSeriesOfEffects()) {
            // Get its LGDX equivalent and add it
            try {
                this.addEffect(factory.getEffectFor(eCur));
            } catch (EffectNotRegisteredException e) {
                // Ignore if something is missing
                e.printStackTrace(System.err);
                System.err.println("Effect " + e.getClass().getCanonicalName() + " has no LGDX equivalent in given " +
                        "factory");
            }
        }

    }
    @Override
    public void applyToActor(Actor aTarget, Renderable rRenderable) {
        applyTo(rRenderable);
    }

    @Override
    public void applyToSprite(Sprite sTarget, Renderable rRenderable) {
        applyTo(rRenderable);
    }
}
