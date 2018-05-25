package org.scify.engine.renderables.effects;

import java.util.Date;

public class BounceEffect extends BaseEffect {
    // Parameters
    public static final String PARAM_BOUNCE_WIDTH = "PARAM_BOUNCE_WIDTH";
    public static final String PARAM_BOUNCE_HEIGHT = "PARAM_BOUNCE_HEIGHT";
    public static final String INFO_TARGET_X_OFFSET = "INFO_TARGET_X_OFFSET";
    public static final String INFO_TARGET_Y_OFFSET = "INFO_TARGET_Y_OFFSET";

//    protected double dMaxXOffset;
//    protected double dMaxYOffset;
//    protected double dCurTime;
//    protected double dStartTime;
//    protected double dDuration;
//    protected double dTimeRemaining;
//    protected double dTargetXOffset;
//    protected double dTargetYOffset;


    public BounceEffect(Effect eSource) {
        super(eSource);
    }

    /**
     * Creates a bounce effect changing the position of the target between two extremes,
     * within a specific span of pixels (for width and height), progressively.
     * @param dBounceWidth The horizontal maximum offset.
     * @param dBounceHeight The vertical maximum offset.
     * @param dDurationMSec The time taken for the full transition.
     */
    public BounceEffect(double dBounceWidth, double dBounceHeight, double dDurationMSec) {
        super(dDurationMSec, false, true);

        setNumericParameter(PARAM_BOUNCE_WIDTH, dBounceWidth);
        setNumericParameter(PARAM_BOUNCE_HEIGHT, dBounceHeight);
    }

    @Override
    public EffectTarget applyTo(EffectTarget target) {
        super.applyTo(target);

        calculateOffsets();

        return target;
    }

    protected void calculateOffsets() {
        double dMaxXOffset = getNumericParameter(PARAM_BOUNCE_WIDTH);
        double dMaxYOffset = getNumericParameter(PARAM_BOUNCE_HEIGHT);

        double dCurTime = new Date().getTime();
        double dStartTime = getNumericParameter(INFO_START_TIME);
        double dDuration = getNumericParameter(PARAM_DURATION);

        double dTimeRemaining = dCurTime - dStartTime;
        double dPercentage = dTimeRemaining / dDuration;
        if (dPercentage > 1.0) {
            dPercentage = 1.0;
        }

        // Apply a function (sinusoid in this case) to determine current X and Y offsets.
        double dTargetXOffset = projectionFunction(dMaxXOffset, dPercentage);
        double dTargetYOffset = projectionFunction(dMaxYOffset, dPercentage);


        // Update target values
        setNumericParameter(INFO_TARGET_X_OFFSET, dTargetXOffset);
        setNumericParameter(INFO_TARGET_Y_OFFSET, dTargetYOffset);

        // Update executed final step
        if (dPercentage == 1.0)
            setBooleanParameter(INFO_EXECUTED_FINAL_STEP, true);

        // DEBUG LINES
//        System.err.println("Offset: " + pRes.toString());
        //////////////
    }

    /**
     * This function returns the target offset (for any dimension), given the maximum offset
     * and the percentage of the change completion. It actually applies a sin function to the
     * offset, completing the circle when completion reaches 100%.
     * @param dMaxOffset The max offset.
     * @param dPercentageOfChange The percentage of the bounce period at the current timepoint.
     * @return The target offset.
     */
    protected double projectionFunction(double dMaxOffset, double dPercentageOfChange) {
        return dMaxOffset * Math.sin(2 * Math.PI * dPercentageOfChange);
    }

}
