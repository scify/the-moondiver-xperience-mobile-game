package org.scify.engine.renderables.effects;

import java.util.Date;

public class RotateEffect extends BaseEffect {
    public static final String PARAM_FROM_ANGLE = "PARAM_FROM_ANGLE";
    public static final String PARAM_TO_ANGLE = "PARAM_TO_ANGLE";
    public static final String INFO_CURRENT_ANGLE = "INFO_CURRENT_ANGLE";
    public final static String PARAM_ORIGIN_X = "PARAM_ORIGIN_X";
    public final static String PARAM_ORIGIN_Y = "PARAM_ORIGIN_Y";


    /**
     * Creates a fade-in effect, from 0.0 to 360.0 degrees, taking place within 1 second progressively.
     */
    public RotateEffect() {
        this(0.0, 360.0, 1000);
    }

    public RotateEffect(Effect eSource) {
        super(eSource);
    }

    /**
     * Creates a fade effect changing the transparency (alpha) of the target from a given start value
     * to a given end value, within a specific span of time, progressively.
     * @param dFromAngle The starting angle (in degrees).
     * @param dToAngle The target, end angle (in degrees).
     * @param dDurationMSec The time taken for the full transition.
     */
    public RotateEffect(double dFromAngle, double dToAngle, double dDurationMSec) {
        super(dDurationMSec, false, true);

        setNumericParameter(PARAM_FROM_ANGLE, dFromAngle);
        setNumericParameter(PARAM_TO_ANGLE, dToAngle);
    }

    @Override
    public EffectTarget applyTo(EffectTarget target) {
        super.applyTo(target);

        calculateAngle();

        return target;
    }

    protected double calculateAngle() {
        double dStartAngle = getNumericParameter(PARAM_FROM_ANGLE);
        double dEndAngle = getNumericParameter(PARAM_TO_ANGLE);

        double dCurTime = new Date().getTime();
        double dTimeRemaining = dCurTime - getNumericParameter(INFO_START_TIME);
        double dPercentage = dTimeRemaining / getDuration();
        if (dPercentage >= 1.0) {
            setBooleanParameter(INFO_EXECUTED_FINAL_STEP, true);
            dPercentage = 1.0;
        }

        // Pose limits to alpha value
        double dTargetAngle = projectionFunction(dStartAngle, dEndAngle, dPercentage);
        // Update info
        setNumericParameter(INFO_CURRENT_ANGLE, dTargetAngle);


        return dTargetAngle;
    }

    protected double projectionFunction(double dStart, double dEnd, double dPercentage) {
        return dStart + (dEnd - dStart) * dPercentage;
    }

    public void setOriginPoint(double dX, double dY) {
        setNumericParameter(PARAM_ORIGIN_X, dX);
        setNumericParameter(PARAM_ORIGIN_Y, dY);
    }
}
