package org.scify.engine.renderables.effects;

import java.util.Date;

public class RotateEffect extends BaseEffect {
    public static String PARAM_FROM_ANGLE = "fromAngle";
    public static String PARAM_TO_ANGLE = "toAngle";

    protected double dStartAngle;
    protected double dEndAngle;
    protected double dCurTime;
    protected double dStartTime;
    protected double dDuration;
    protected double dTimeRemaining;
    protected double dTargetAngle;

    /**
     * Creates a fade-in effect, from 0.0 to 360.0 degrees, taking place within 1 second progressively.
     */
    public RotateEffect() {
        super(1000);

        params.put(PARAM_FROM_ANGLE, "0.0");
        params.put(PARAM_TO_ANGLE, "360.0");

    }

    /**
     * Creates a fade effect changing the transparency (alpha) of the target from a given start value
     * to a given end value, within a specific span of time, progressively.
     * @param dFromAngle The starting angle (in degrees).
     * @param dToAngle The target, end angle (in degrees).
     * @param dDurationMSec The time taken for the full transition.
     */
    public RotateEffect(double dFromAngle, double dToAngle, double dDurationMSec) {
        super(dDurationMSec);

        params.put(PARAM_FROM_ANGLE, String.valueOf(dFromAngle));
        params.put(PARAM_TO_ANGLE, String.valueOf(dToAngle));
    }

    @Override
    public EffectTarget applyTo(EffectTarget target) {
        super.applyTo(target);

        calculateAngle();

        return target;
    }

    protected double calculateAngle() {
        dStartAngle = Double.valueOf(params.get(PARAM_FROM_ANGLE));
        dEndAngle = Double.valueOf(params.get(PARAM_TO_ANGLE));

        dCurTime = new Date().getTime();
        dStartTime = Long.valueOf(params.get(INFO_START_TIME));
        dDuration = Double.valueOf(params.get(PARAM_DURATION));

        dTimeRemaining = dCurTime - dStartTime;
        double dPercentage = dTimeRemaining / dDuration;

        // Pose limits to alpha values
        if (dEndAngle > dStartAngle)
            dTargetAngle = Math.min(dStartAngle + dPercentage * (dEndAngle - dStartAngle),
                    dEndAngle);
        else
            dTargetAngle = Math.max(dStartAngle + dPercentage * (dEndAngle - dStartAngle),
                    dEndAngle);

        return dTargetAngle;
    }


}
