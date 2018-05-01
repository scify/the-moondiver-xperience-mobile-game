package org.scify.engine.renderables.effects.libgdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.FadeEffect;

public class FadeLGDXEffect extends FadeEffect implements LGDXEffect {

    public FadeLGDXEffect(double dFromAlpha, double dToAlpha, double dDurationMSec) {
        super(dFromAlpha, dToAlpha, dDurationMSec);
    }

    public FadeLGDXEffect(Effect eSource) {
        super(eSource);
    }

    @Override
    public synchronized void applyToActor(Actor aTarget, Renderable rRenderable) {
        super.applyTo(rRenderable);

        // DEBUG LINES
//        System.err.println("Target alpha: " + String.valueOf(dTargetAlpha));
        //////////////
        Color cCol = aTarget.getColor();
        cCol.a = (float) getCurrentAlpha();
        // Update target alpha
        aTarget.setColor(cCol);

    }

    @Override
    public synchronized void applyToSprite(Sprite sTarget, Renderable rRenderable) {
        super.applyTo(rRenderable);

        Color cCol = sTarget.getColor();
        cCol.a = (float) getCurrentAlpha();
        // Update target alpha
        sTarget.setColor(cCol);
    }

    protected double getCurrentAlpha() {
        double dCurrentAlpha = getNumericParameter(INFO_CURRENT_ALPHA);
        return dCurrentAlpha;

    }
}
