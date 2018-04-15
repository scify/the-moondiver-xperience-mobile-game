package org.scify.engine.renderables.effects;

import java.util.Date;

public class FadeEffect extends BaseEffect {
    public static String PARAM_FROM_ALPHA = "fromAlpha";
    public static String PARAM_TO_ALPHA = "toAlpha";
//    public static String INFO_CURRENT_ALPHA = "currentAlpha";

    protected double dStartAlpha;
    protected double dEndAlpha;
    protected double dCurtime;
    protected double dStartTime;
    protected double dDuration;
    protected double dTimeRemaining;
    protected double dTargetAlpha;

    /**
     * Creates a fade-in effect, from alpha 0.0 to 1.0, taking place within 1 second progressively.
     */
    public FadeEffect() {
        super(1000);

        params.put(PARAM_FROM_ALPHA, "0.0");
        params.put(PARAM_TO_ALPHA, "1.0");
    }

    /**
     * Creates a fade effect changing the transparency (alpha) of the target from a given start value
     * to a given end value, within a specific span of time, progressively.
     * @param dFromAlpha The staring alpha (from 0.0 to 1.0).
     * @param dToAlpha The target, end alpha (from 0.0 to 1.0).
     * @param dDurationMSec The time taken for the full transition.
     */
    public FadeEffect(double dFromAlpha, double dToAlpha, double dDurationMSec) {
        super(dDurationMSec);

        params.put(PARAM_FROM_ALPHA, String.valueOf(dFromAlpha));
        params.put(PARAM_TO_ALPHA, String.valueOf(dToAlpha));
    }

    @Override
    public EffectTarget applyTo(EffectTarget target) {
        super.applyTo(target);

        calculateAlpha();

        return target;

    }

    protected double calculateAlpha() {
        dStartAlpha = Double.valueOf(params.get(PARAM_FROM_ALPHA));
        dEndAlpha = Double.valueOf(params.get(PARAM_TO_ALPHA));

        dCurtime = new Date().getTime();
        dStartTime = Long.valueOf(params.get(INFO_START_TIME));
        dDuration = Double.valueOf(params.get(PARAM_DURATION));

        dTimeRemaining = dCurtime - dStartTime;

        double dPercentage = dTimeRemaining / dDuration;

        // Pose limits to alpha values
        if (dEndAlpha > dStartAlpha)
            dTargetAlpha = Math.min(dStartAlpha + dPercentage * (dEndAlpha - dStartAlpha),
                    dEndAlpha);
        else
            dTargetAlpha = Math.max(dStartAlpha + dPercentage * (dEndAlpha - dStartAlpha),
                    dEndAlpha);

        return dTargetAlpha;
    }


}
