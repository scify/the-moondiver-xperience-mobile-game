package org.scify.engine.renderables.effects;

import org.scify.engine.Positionable;
import org.scify.engine.renderables.Renderable;

import java.awt.geom.Point2D;
import java.util.Date;

public class MoveEffect extends BaseEffect {
    public static String PARAM_START_X = "startX";
    public static String PARAM_START_Y = "startY";
    public static String PARAM_TARGET_X = "targetX";
    public static String PARAM_TARGET_Y = "targetY";

    protected double dCurrentX;
    protected double dCurrentY;
    protected double dStartX = Double.NaN;
    protected double dStartY = Double.NaN;
    protected double dCurTime;
    protected double dStartTime;
    protected double dDuration;
    protected double dTimeRemaining;
    protected double dTargetX;
    protected double dTargetY;

    /**
     * Creates a fade-in effect, from alpha 0.0 to 1.0, taking place within 1 second progressively.
     */
    public MoveEffect() {
        super(1000);

        params.put(PARAM_TARGET_X, "0.0");
        params.put(PARAM_TARGET_Y, "0.0");

        dTargetX = 0.0;
        dTargetY = 0.0;
    }

    public MoveEffect(double dToX, double dToY, double dDuration) {
        super(dDuration);

        params.put(PARAM_TARGET_X, String.valueOf(dToX));
        params.put(PARAM_TARGET_Y, String.valueOf(dToY));

        dTargetX = 0.0;
        dTargetY = 0.0;
    }

    public MoveEffect(double dFromX, double dFromY, double dToX, double dToY, double dDuration) {
        super(dDuration);

        params.put(PARAM_TARGET_X, String.valueOf(dToX));
        params.put(PARAM_TARGET_Y, String.valueOf(dToY));

        // Update start position
        this.dStartX = dFromX;
        this.dStartY = dFromY;
        // ... and end position
        dTargetX = dToX;
        dTargetY = dToY;
    }


    @Override
    public EffectTarget applyTo(EffectTarget target) {
        super.applyTo(target);

        if (target instanceof Positionable) {
            Positionable pTarget = (Positionable)target;

            // If we have not yet initialized start position
            if (Double.isNaN(dStartX)) {
                // initialize it
                dStartX = pTarget.getxPos();
                dStartY = pTarget.getyPos();
            }

            calculateTargetPosition();

            // Update target effect info
            target.setEffectInfo(this, params);
        }

        return target;
    }

    protected Point2D.Double calculateTargetPosition() {
        dCurTime = new Date().getTime();
        dStartTime = Long.valueOf(params.get(INFO_START_TIME));
        dDuration = Double.valueOf(params.get(PARAM_DURATION));

        dTimeRemaining = dCurTime - dStartTime;
        double dPercentage = dTimeRemaining / dDuration;

        // Apply a function (sinusoid in this case) to determine current X and Y offsets.
        dCurrentX = projectionFunction(dStartX, dTargetX, dPercentage);
        dCurrentY = projectionFunction(dStartY, dTargetY, dPercentage);


        Point2D.Double pRes = new Point2D.Double();
        pRes.setLocation(dCurrentX, dCurrentY);

        // DEBUG LINES
//        System.err.println("Offset: " + pRes.toString());
        //////////////
        return pRes;
    }

    protected double projectionFunction(double dStart, double dEnd, double dPercentageOfChange) {
        return dStart + dPercentageOfChange * (dEnd - dStart);
    }

}
