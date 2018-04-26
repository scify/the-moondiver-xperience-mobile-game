package org.scify.engine.renderables.effects.libgdx;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.FunctionEffect;

public class FunctionLGDXEffect extends FunctionEffect implements LGDXEffect {
    public FunctionLGDXEffect(Runnable rToRun) {
        super(rToRun);
    }

    public FunctionLGDXEffect(Effect eSource) {
        super(eSource);
    }

    /**
     * Applies the effect to the selected target, updating any effect info.
     *
     * @param aTarget     The target actor to update.
     * @param rRenderable The renderable containing the effect state info.
     */
    @Override
    public void applyToActor(Actor aTarget, Renderable rRenderable) {
        applyTo(rRenderable);
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
        applyTo(rRenderable);
        // Do nothing
    }
}
