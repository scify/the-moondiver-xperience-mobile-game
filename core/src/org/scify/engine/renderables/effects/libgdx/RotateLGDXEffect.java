package org.scify.engine.renderables.effects.libgdx;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.RotateEffect;

public class RotateLGDXEffect extends RotateEffect implements LGDXEffect {
    public RotateLGDXEffect(double dFromAngle, double dToAngle, double dDurationMSec) {
        super(dFromAngle, dToAngle, dDurationMSec);

        // Set origin to default values
        setNumericParameter(PARAM_ORIGIN_X, 0.0);
        setNumericParameter(PARAM_ORIGIN_Y, 0.0);
    }

    public RotateLGDXEffect(Effect eSource) {
        super(eSource);
    }

    @Override
    public synchronized void applyToActor(Actor aTarget, Renderable rRenderable) {
        super.applyTo(rRenderable);

        // Reset origin to sprite center
        // TODO: Check if we should use actor center and when
        if (getNumericParameter(PARAM_ORIGIN_X) == null) {
            setOriginX(aTarget.getWidth() / 2.0);
        }
        else {
            setOriginX(getNumericParameter(PARAM_ORIGIN_X));
        }
        if (getNumericParameter(PARAM_ORIGIN_Y) == null) {
            setOriginY(aTarget.getHeight() / 2.0);
        }
        else {
            setOriginY(getNumericParameter(PARAM_ORIGIN_Y));
        }

        // DEBUG LINES
//        System.err.println("Target alpha: " + String.valueOf(dTargetAngle));
        //////////////
        // Update target rotation, also using origin
        aTarget.setOrigin((float)getOriginX(), (float)getOriginY());
        double dTargetAngle = getNumericParameter(INFO_CURRENT_ANGLE);
        aTarget.setRotation((float)dTargetAngle);
    }

    @Override
    public synchronized void applyToSprite(Sprite sTarget, Renderable rRenderable) {
        super.applyTo(rRenderable);

        // Reset origin to sprite center
        // TODO: Check if we should use sprite center and when
        if (getNumericParameter(PARAM_ORIGIN_X) == null) {
            setOriginX(sTarget.getWidth() / 2.0);
        }
        else {
            setOriginX(getNumericParameter(PARAM_ORIGIN_X));
        }
        if (getNumericParameter(PARAM_ORIGIN_Y) == null) {
            setOriginY(sTarget.getHeight() / 2.0);
        }
        else {
            setOriginY(getNumericParameter(PARAM_ORIGIN_Y));
        }

        // Update target rotation, also using origin
        sTarget.setOrigin((float)getOriginX(), (float)getOriginY());
        double dCurrentRotation = getNumericParameter(INFO_CURRENT_ANGLE);
        sTarget.setRotation((float)dCurrentRotation);

    }

    protected double getOriginX() {
        return getNumericParameter(PARAM_ORIGIN_X);
    }

    protected double getOriginY() {

        return getNumericParameter(PARAM_ORIGIN_Y);
    }

    protected void setOriginX(double dNewX) {
        setNumericParameter(PARAM_ORIGIN_X,dNewX);
    }

    protected void setOriginY(double dNewY) {
        setNumericParameter(PARAM_ORIGIN_Y,dNewY);
    }
}
