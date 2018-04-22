package org.scify.engine.renderables.effects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.libgdx.LGDXEffect;

public class FunctionEffect extends BaseEffect implements LGDXEffect  {
    public final String PARAM_OBJ_RUNNABLE = "PARAM_OBJ_RUNNABLE";

    public FunctionEffect(Runnable rToRun) {
        super(1000);

        setObjectParameter(PARAM_OBJ_RUNNABLE, rToRun);
    }

    public FunctionEffect(Effect eSource) {
        super(eSource);
    }

    /**
     * This method should be overriden by the actual effects in the subclasses.
     * Currently, it only updates the effect info based on the internal parameters object.
     * <b>Make sure you always call this from subclasses - using <i>super.applyTo</i> - to update the start time correctly.</b>
     *
     * @param target The target of the effect.
     * @return The affected EffectTarget.
     */
    @Override
    public synchronized EffectTarget applyTo(EffectTarget target) {
        super.applyTo(target);

        Object oToRun = getObjectParameter(PARAM_OBJ_RUNNABLE);
        if (oToRun != null) {
            ((Runnable)oToRun).run();
            stop();
        }

        return target;
    }

    /**
     * Applies the effect to the selected target, updating any effect info.
     *
     * @param aTarget     The target actor to update.
     * @param rRenderable The renderable containing the effect state info.
     */
    @Override
    public void applyToActor(Actor aTarget, Renderable rRenderable) {
        super.applyTo(rRenderable);
        // Do nothing
    }

    /**
     * Applies the effect to the selected target, updating any effect info.
     *
     * @param sTarget     The target sprite to update.
     * @param rRenderable The renderable containing the effect state info.
     */
    @Override
    public void applyToSprite(Sprite sTarget, Renderable rRenderable) {
        super.applyTo(rRenderable);
        // Do nothing
    }
}
