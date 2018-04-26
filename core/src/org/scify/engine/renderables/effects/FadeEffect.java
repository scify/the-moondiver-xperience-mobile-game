package org.scify.engine.renderables.effects;

import java.util.Date;

public class FadeEffect extends BaseEffect {
    public static String PARAM_FROM_ALPHA = "PARAM_FROM_ALPHA";
    public static String PARAM_TO_ALPHA = "PARAM_TO_ALPHA";
    public static String INFO_CURRENT_ALPHA = "PARAM_TARGET_ALPHA";

//    public static String INFO_CURRENT_ALPHA = "currentAlpha";
//    protected double dStartAlpha;
//    protected double dEndAlpha;
//    protected double dCurtime;
//    protected double dStartTime;
//    protected double dDuration;
//    protected double dTimeRemaining;
//    protected double dTargetAlpha;

    /**
     * Creates a fade-in effect, from alpha 0.0 to 1.0, taking place within 1 second progressively.
     */
    public FadeEffect() {
        super(1000, false, true);

        setNumericParameter(PARAM_FROM_ALPHA, 0.0);
        setNumericParameter(PARAM_TO_ALPHA, 1.0);
    }

    public FadeEffect(Effect eSource) {
        super(eSource);
    }

    /**
     * Creates a fade effect changing the transparency (alpha) of the target from a given start value
     * to a given end value, within a specific span of time, progressively.
     * @param dFromAlpha The staring alpha (from 0.0 to 1.0).
     * @param dToAlpha The target, end alpha (from 0.0 to 1.0).
     * @param dDurationMSec The time taken for the full transition.
     */
    public FadeEffect(double dFromAlpha, double dToAlpha, double dDurationMSec) {
        super(dDurationMSec, false, true);

        setNumericParameter(PARAM_FROM_ALPHA, dFromAlpha);
        setNumericParameter(PARAM_TO_ALPHA, dToAlpha);
    }

    @Override
    public EffectTarget applyTo(EffectTarget target) {
        super.applyTo(target);

        calculateAlpha();

        return target;

    }

    protected double calculateAlpha() {
        double dStartAlpha = getNumericParameter(PARAM_FROM_ALPHA);
        double dEndAlpha = getNumericParameter(PARAM_TO_ALPHA);

        double dCurtime = new Date().getTime();
        double dStartTime = getNumericParameter(INFO_START_TIME);
        double dDuration = getDuration();

        double dTimeRemaining = dCurtime - dStartTime;

        double dPercentage = dTimeRemaining / dDuration;
        // Check if we have exceeded the end
        if (dPercentage > 1.0)
            dPercentage =  1.0;


        // Pose limits to alpha values
        double dTargetAlpha;
        if (dEndAlpha > dStartAlpha)
            dTargetAlpha = Math.min(dStartAlpha + dPercentage * (dEndAlpha - dStartAlpha),
                    dEndAlpha);
        else
            dTargetAlpha = Math.max(dStartAlpha + dPercentage * (dEndAlpha - dStartAlpha),
                    dEndAlpha);

        setNumericParameter(INFO_CURRENT_ALPHA, dTargetAlpha);

        if (dPercentage == 1.0)
            setBooleanParameter(INFO_EXECUTED_FINAL_STEP, true);

        return dTargetAlpha;
    }


}
