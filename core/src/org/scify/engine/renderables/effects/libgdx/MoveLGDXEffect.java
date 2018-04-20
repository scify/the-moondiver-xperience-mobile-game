package org.scify.engine.renderables.effects.libgdx;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.MoveEffect;

public class MoveLGDXEffect extends MoveEffect implements LGDXEffect {
    public MoveLGDXEffect(double dFromX, double dFromY, double dToX, double dToY, double dDuration) {
        super(dFromX, dFromY, dToX, dToY, dDuration);
    }

    public MoveLGDXEffect(double dToX, double dToY, double dDuration) {
        super(dToX, dToY, dDuration);
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

        rRenderable.setxPos((float)dCurrentX);
        rRenderable.setyPos((float)dCurrentY);
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

        rRenderable.setxPos((float)dCurrentX);
        rRenderable.setyPos((float)dCurrentY);
    }
}
