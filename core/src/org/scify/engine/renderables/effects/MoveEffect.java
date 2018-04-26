package org.scify.engine.renderables.effects;

import org.scify.engine.Positionable;

import java.awt.geom.Point2D;
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
    public MoveEffect() {
        super(1000, true, true);

        setNumericParameter(PARAM_START_X, null);
        setNumericParameter(PARAM_START_Y, null);
        setNumericParameter(PARAM_TARGET_X, 0.0);
        setNumericParameter(PARAM_TARGET_Y, 0.0);
    }

    public MoveEffect(double dToX, double dToY, double dDuration) {
        super(dDuration, true, true);

        setNumericParameter(PARAM_TARGET_X, dToX);
        setNumericParameter(PARAM_TARGET_Y, dToY);
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
            if (getParameter(PARAM_START_X) == null) {
                // initialize it
                setNumericParameter(PARAM_START_X, (double) pTarget.getxPos());
                setNumericParameter(PARAM_START_Y, (double) pTarget.getyPos());
                setBooleanParameter(INFO_EXECUTED_ONCE, true);
            }

            calculateTargetPosition();

        }

        return target;
    }

    protected Point2D.Double calculateTargetPosition() {
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

        // Apply new location info
        Point2D.Double pRes = new Point2D.Double();
        pRes.setLocation(dCurrentX, dCurrentY);

        // Update whether we have reached the end
        if (dPercentage == 1.0)
            setBooleanParameter(INFO_EXECUTED_FINAL_STEP, true);

        // DEBUG LINES
//        System.err.println("Offset: " + pRes.toString());
        //////////////
        return pRes;
    }

    protected double projectionFunction(double dStart, double dEnd, double dPercentageOfChange) {
        return dStart + dPercentageOfChange * (dEnd - dStart);
    }

}
