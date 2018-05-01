package org.scify.engine.renderables.effects;

import org.scify.engine.Positionable;

import java.util.Date;

public class MoveEffect extends BaseEffect {
    public static final String PARAM_START_X = "PARAM_START_X";
    public static final String PARAM_START_Y = "PARAM_START_Y";
    public static final String PARAM_TARGET_X = "PARAM_TARGET_X";
    public static final String PARAM_TARGET_Y = "PARAM_TARGET_Y";

    public static final String INFO_CURRENT_X = "INFO_CURRENT_X";
    public static final String INFO_CURRENT_Y = "INFO_CURRENT_Y";

    /**
     * Creates a fade-in effect, from alpha 0.0 to 1.0, taking place within 1 second progressively.
     */
    public MoveEffect(double dToX, double dToY, double dDuration) {
        this(Double.MIN_VALUE, Double.MIN_VALUE, dToX, dToY, 1000.0);
    }

    public MoveEffect(double dFromX, double dFromY, double dToX, double dToY, double dDuration) {
        super(dDuration, true, true);

        setNumericParameter(PARAM_START_X, dFromX);
        setNumericParameter(PARAM_START_Y, dFromY);
        setNumericParameter(PARAM_TARGET_X, dToX);
        setNumericParameter(PARAM_TARGET_Y, dToY);

    }

    public MoveEffect(Effect eSource) {
        super(eSource);
    }

    @Override
    public EffectTarget applyTo(EffectTarget target) {
        super.applyTo(target);

        if (target instanceof Positionable) {
            Positionable pTarget = (Positionable)target;

            // If we have not yet initialized start position
            if (getNumericParameter(PARAM_START_X) == Double.MIN_VALUE) {
                // initialize it
                setNumericParameter(PARAM_START_X, (double) pTarget.getxPos());
                setNumericParameter(PARAM_START_Y, (double) pTarget.getyPos());
            }

            calculateTargetPosition();

        }

        return target;
    }

    protected void calculateTargetPosition() {
        double dCurTime = new Date().getTime();
        double dStartTime = getNumericParameter(INFO_START_TIME);

        double dTimeRemaining = dCurTime - dStartTime;
        double dPercentage = dTimeRemaining / getDuration();
        // Check maximum
        if (dPercentage > 1.0)
            dPercentage = 1.0;

        // Apply a function (sinusoid in this case) to determine current X and Y offsets.
        double dCurrentX = projectionFunction(getNumericParameter(PARAM_START_X), getNumericParameter(PARAM_TARGET_X), dPercentage);
        double dCurrentY = projectionFunction(getNumericParameter(PARAM_START_Y), getNumericParameter(PARAM_TARGET_Y), dPercentage);

        // Update parameters
        setNumericParameter(INFO_CURRENT_X, dCurrentX);
        setNumericParameter(INFO_CURRENT_Y, dCurrentY);

        // Update whether we have reached the end
        if (dPercentage == 1.0)
            setBooleanParameter(INFO_EXECUTED_FINAL_STEP, true);

        setBooleanParameter(INFO_EXECUTED_ONCE, true);
    }

    protected double projectionFunction(double dStart, double dEnd, double dPercentageOfChange) {
        return dStart + dPercentageOfChange * (dEnd - dStart);
    }

}
