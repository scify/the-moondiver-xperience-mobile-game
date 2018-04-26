package org.scify.engine.renderables.effects.libgdx;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.ParallelEffectList;

public class LGDXParallelEffectList extends ParallelEffectList implements LGDXEffect {
    @Override
    public void applyToActor(Actor aTarget, Renderable rRenderable) {
        super.applyTo(rRenderable);
    }

    @Override
    public void applyToSprite(Sprite sTarget, Renderable rRenderable) {
        super.applyTo(rRenderable);
    }

    /**
     * Initializes an LGDX parallel effect list, based on a generic parallel effect list. If a generic effect does not have its equivalent
     * in LGDX, it is ignored (and an exception is output to System.err).
     * @param elSource The generic effect list, used as a guide.
     * @param factory An (optional) factory for LGDX effects in the list. If null, the default LGDXEffectFactory
     *                is used.
     */
    public LGDXParallelEffectList(ParallelEffectList elSource, LGDXEffectFactory<LGDXEffect> factory) {
        // If no factory is provided
        if (factory == null)
            // use default one
            factory = LGDXEffectFactory.getFactorySingleton();

        // For each effect
        for (Effect eCur: elSource.getSeriesOfEffects()) {
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

}
