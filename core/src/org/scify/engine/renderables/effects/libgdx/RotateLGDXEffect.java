package org.scify.engine.renderables.effects.libgdx;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.RotateEffect;

public class RotateLGDXEffect extends RotateEffect implements LGDXEffect {

    public RotateLGDXEffect() {
        super();
    }

    public RotateLGDXEffect(double dFromAngle, double dToAngle, double dDurationMSec) {
        super(dFromAngle, dToAngle, dDurationMSec);
    }

    @Override
    public synchronized void applyToActor(Actor aTarget, Renderable rRenderable) {
        super.applyTo(rRenderable);

        // DEBUG LINES
        System.err.println("Target alpha: " + String.valueOf(dTargetAngle));
        //////////////
        aTarget.setRotation((float)dTargetAngle);
    }

    @Override
    public synchronized void applyToSprite(Sprite sTarget, Renderable rRenderable) {
        super.applyTo(rRenderable);

        // Update target alpha
        sTarget.setRotation((float)dTargetAngle);

    }

}
