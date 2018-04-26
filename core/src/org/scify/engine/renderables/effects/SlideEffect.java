package org.scify.engine.renderables.effects;

import org.scify.engine.Positionable;

public class SlideEffect extends MoveEffect {
    public static final String PARAM_ENTER_ANGLE="PARAM_ENTER_ANGLE";

    public SlideEffect(Effect eSource) {
        super(eSource);
    }

    public SlideEffect(double dToX, double dToY, double dEntryAngle, double dDuration) {
        super(Double.NaN, Double.NaN, dToX, dToY, dDuration);

        setNumericParameter(PARAM_ENTER_ANGLE, dEntryAngle);
    }

    @Override
    public EffectTarget applyTo(EffectTarget target) {
        Positionable pTarget;

        if (!(target instanceof Positionable))
            return target;

        pTarget = (Positionable)target;

        // If not initialized
        if (Double.isNaN(getNumericParameter(PARAM_START_X))) {
            // initialize based on positionable width
            double dStartX = pTarget.getxPos() + pTarget.getWidth() * Math.cos(2.0 * Math.PI * getNumericParameter(PARAM_ENTER_ANGLE) / 360.0);
            setNumericParameter(PARAM_START_X, dStartX);
        }
        // If not initialized
        if (Double.isNaN(getNumericParameter(PARAM_START_Y))) {
            double dStartY = pTarget.getyPos() + pTarget.getHeight() * Math.sin(2.0 * Math.PI * getNumericParameter(PARAM_ENTER_ANGLE) / 360.0);
            // initialize based on positionable height
            setNumericParameter(PARAM_START_Y, dStartY);
        }

        return super.applyTo(target);
    }
}
