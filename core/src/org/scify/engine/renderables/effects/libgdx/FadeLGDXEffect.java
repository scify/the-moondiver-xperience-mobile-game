package org.scify.engine.renderables.effects.libgdx;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.FadeEffect;

public class FadeLGDXEffect extends FadeEffect implements LGDXEffect {

    public FadeLGDXEffect(double dFromAlpha, double dToAlpha, double dDurationMSec) {
        super(dFromAlpha, dToAlpha, dDurationMSec);
    }

    public FadeLGDXEffect() {
        super();
    }

    @Override
    public synchronized void applyToActor(Actor aTarget, Renderable rRenderable) {
        super.applyTo(rRenderable);

        // DEBUG LINES
//        System.err.println("Target alpha: " + String.valueOf(dTargetAlpha));
        //////////////

        aTarget.setColor(aTarget.getColor().r, aTarget.getColor().g, aTarget.getColor().b,
                (float) dTargetAlpha);

    }

    @Override
    public synchronized void applyToSprite(Sprite sTarget, Renderable rRenderable) {
        super.applyTo(rRenderable);

        // Update target alpha
        sTarget.setColor(sTarget.getColor().r, sTarget.getColor().g, sTarget.getColor().b,
                (float) dTargetAlpha);
    }
}
