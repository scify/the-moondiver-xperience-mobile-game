package org.scify.engine.renderables.effects;

import java.awt.geom.Point2D;
import java.util.Date;

public class BounceEffect extends BaseEffect {
    public static String PARAM_BOUNCE_WIDTH = "bounceWidth";
    public static String PARAM_BOUNCE_HEIGHT = "bounceHeight";
    public static String PARAM_DURATION = "duration";
    public static String INFO_DURATION_REMAINING_MSEC = "durationRemaining";

    protected double dMaxXOffset;
    protected double dMaxYOffset;
    protected double dCurTime;
    protected double dStartTime;
    protected double dDuration;
    protected double dTimeRemaining;
    protected double dTargetXOffset;
    protected double dTargetYOffset;

    /**
     * Creates a fade-in effect, from alpha 0.0 to 1.0, taking place within 1 second progressively.
     */
    public BounceEffect() {
        super();

        params.put(PARAM_BOUNCE_WIDTH, "100.0");
        params.put(PARAM_BOUNCE_HEIGHT, "100.0");
        params.put(PARAM_DURATION, "1000.0");
    }

    /**
     * Creates a bounce effect changing the position of the target between two extremes,
     * within a specific span of pixels (for width and height), progressively.
     * @param dBounceWidth The horizontal maximum offset.
     * @param dBounceHeight The vertical maximum offset.
     * @param dDurationMSec The time taken for the full transition.
     */
    public BounceEffect(double dBounceWidth, double dBounceHeight, double dDurationMSec) {
        super();

        params.put(PARAM_BOUNCE_WIDTH, String.valueOf(dBounceWidth));
        params.put(PARAM_BOUNCE_HEIGHT, String.valueOf(dBounceHeight));
        params.put(PARAM_DURATION, String.valueOf(dDurationMSec));
        params.put(INFO_DURATION_REMAINING_MSEC, String.valueOf(dDurationMSec));
    }

    @Override
    public EffectTarget applyTo(EffectTarget target) {
        super.applyTo(target);

        calculateOffsets();

        if (!params.containsKey(INFO_START_TIME))
            // Set initial effect time
            params.put(INFO_START_TIME, String.valueOf(new Date().getTime()));
        // Update target effect info
        target.setEffectInfo(this, params);

        return target;
    }

    protected Point2D.Double calculateOffsets() {
        dMaxXOffset = Double.valueOf(params.get(PARAM_BOUNCE_WIDTH));
        dMaxYOffset = Double.valueOf(params.get(PARAM_BOUNCE_HEIGHT));

        dCurTime = new Date().getTime();
        dStartTime = Long.valueOf(params.get(INFO_START_TIME));
        dDuration = Double.valueOf(params.get(PARAM_DURATION));

        dTimeRemaining = dCurTime - dStartTime;
        double dPercentage = dTimeRemaining / dDuration;

        // Apply a function (sinusoid in this case) to determine current X and Y offsets.
        dTargetXOffset = projectionFunction(dMaxXOffset, dPercentage);
        dTargetYOffset = projectionFunction(dMaxYOffset, dPercentage);


        Point2D.Double pRes = new Point2D.Double();
        pRes.setLocation(dTargetXOffset, dTargetYOffset);

        // DEBUG LINES
//        System.err.println("Offset: " + pRes.toString());
        //////////////
        return pRes;
    }

    protected double projectionFunction(double dMaxOffset, double dPercentageOfChange) {
        return dMaxOffset * Math.sin(2 * Math.PI * dPercentageOfChange);
    }

}
