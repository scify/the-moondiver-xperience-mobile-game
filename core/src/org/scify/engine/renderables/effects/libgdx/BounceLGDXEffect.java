package org.scify.engine.renderables.effects.libgdx;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.BounceEffect;
import org.scify.engine.renderables.effects.Effect;

public class BounceLGDXEffect extends BounceEffect implements LGDXEffect {

    /**
     * Creates a bounce effect that <b>does not alter the renderable position</b>, but instead paints the actual
     * actor or sprite in an offset position.
     * @param dBounceWidth The maximum horizontal bounce offset.
     * @param dBounceHeight The maximum vertical bounce offset.
     * @param dDurationMSec The duration of the effect.
     */
    public BounceLGDXEffect(double dBounceWidth, double dBounceHeight, double dDurationMSec) {
        super(dBounceWidth, dBounceHeight, dDurationMSec);
    }

    public BounceLGDXEffect(Effect eSource) {
        super(eSource);
    }

    @Override
    public synchronized void applyToActor(Actor aTarget, Renderable rRenderable) {
        super.applyTo(rRenderable);

        aTarget.addAction(Actions.moveTo((float)(rRenderable.getxPos() + getNumericParameter(INFO_TARGET_X_OFFSET)),
                (float)(rRenderable.getyPos() + getNumericParameter(INFO_TARGET_Y_OFFSET))));
        // DEBUG LINES
//        System.err.println("Target position: " + String.valueOf(aTarget.getX()) + "," +
//                String.valueOf(aTarget.getY()));
        //////////////
    }

    @Override
    public synchronized void applyToSprite(Sprite sTarget, Renderable rRenderable) {
        super.applyTo(rRenderable);

        // Update target alpha
        sTarget.setX((float)(sTarget.getX() + getNumericParameter(INFO_TARGET_X_OFFSET)));
        sTarget.setY((float)(sTarget.getY() + getNumericParameter(INFO_TARGET_Y_OFFSET)));

    }}
