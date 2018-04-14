package org.scify.engine.renderables.effects.libgdx;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.Effect;

public interface LGDXEffect extends Effect {
    /**
     * Applies the effect to the selected target, updating any effect info.
     * @param aTarget The target actor to update.
     * @param rRenderable The renderable containing the effect state info.
     */
    public void applyToActor(Actor aTarget, Renderable rRenderable);

    /**
     * Applies the effect to the selected target, updating any effect info.
     * @param sTarget The target sprite to update.
     * @param rRenderable The renderable containing the effect state info.
     */
    public void applyToSprite(Sprite sTarget, Renderable rRenderable);
}
