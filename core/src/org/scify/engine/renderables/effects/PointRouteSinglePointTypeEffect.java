package org.scify.engine.renderables.effects;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class PointRouteSinglePointTypeEffect extends EffectSequence {

    public static final String POINT_LIST = "POINT_LIST";
    public static final String PARAM_FADE_IN_STEP_WEIGHT = "PARAM_FADE_IN_STEP_WEIGHT";
    public static final String PARAM_FADE_OUT_STEP_WEIGHT = "PARAM_FADE_OUT_STEP_WEIGHT";
    public static final String PARAM_VISIBLE_STEP_WEIGHT = "PARAM_VISIBLE_STEP_WEIGHT";

    /**
     * Constructor which sets the delay between fade-in and fade-out to zero.
     */
    public PointRouteSinglePointTypeEffect(List<Vector2> lvPointList, double dFadeInStepDurationWeight, double dFadeOutStepDurationWeight,
                                           double dTotalDuration) {
        this(lvPointList, dFadeInStepDurationWeight, dFadeOutStepDurationWeight, 0.0, dTotalDuration);
    }

    /**
     * Creates an effect which causes the {@link org.scify.engine.renderables.Renderable} to fade in and then fade out
     * in a sequence of given positions. The weights of fading in/out indicate the relative time dedicated to each
     * of the two events. A weight of 1.0 for fade in, 1.0 for fade out indicates that both effects will take equal time.
     * A weight of 2.0 for fade in and 1.0 for fade out indicates that fade in will take 2 times longer than fade out.
     * @param lvPointList The positions where the point will appear
     * @param dTotalDuration The duration of the whole effect
     * @param dFadeInStepDurationWeight The weight of the time will be used for fading in.
     * @param dFadeOutStepDurationWeight The weight of the time will be used for fading out.
     * @param dVisibleStateDurationWeight The weight of the time will be used for remaining visible (between fades).
     */
    public PointRouteSinglePointTypeEffect(List<Vector2> lvPointList, double dFadeInStepDurationWeight, double dFadeOutStepDurationWeight,
                                           double dVisibleStateDurationWeight, double dTotalDuration) {
        super();

        setObjectParameter(POINT_LIST, lvPointList);
        setNumericParameter(PARAM_FADE_IN_STEP_WEIGHT, dFadeInStepDurationWeight);
        setNumericParameter(PARAM_FADE_OUT_STEP_WEIGHT, dFadeOutStepDurationWeight);
        setNumericParameter(PARAM_VISIBLE_STEP_WEIGHT, dVisibleStateDurationWeight);

        double dFadeInStepPercentage = (dFadeInStepDurationWeight / (dFadeInStepDurationWeight + dFadeOutStepDurationWeight + dVisibleStateDurationWeight));
        double dFadeOutStepPercentage = (dFadeOutStepDurationWeight / (dFadeInStepDurationWeight + dFadeOutStepDurationWeight + dVisibleStateDurationWeight));
        double dVisibilityStepPercentage = (dVisibleStateDurationWeight / (dFadeInStepDurationWeight + dFadeOutStepDurationWeight + dVisibleStateDurationWeight));

        // Estimate single step duration
        double dStepDuration = dTotalDuration / lvPointList.size();
        boolean bInitialized = false;
        // For each point
        for (Vector2 vCur : lvPointList) {
            // If first point
            if (!bInitialized) {
                // Initialize visibility and alpha
                addEffect(new SetAlphaEffect(0.0));
                addEffect(new VisibilityEffect(true));
                bInitialized = true;
            }

            // Move to new position
            addEffect(new MoveEffect(vCur.x, vCur.y, 0.0));
            // Fade in
            addEffect(new FadeEffect(0.0, 1.0, dFadeInStepPercentage * dStepDuration));
            // Wait (stay visible), if requested
            if (dVisibilityStepPercentage > 0.0) {
                addEffect(new DelayEffect(dVisibilityStepPercentage * dStepDuration));
            }
            // Fade out
            addEffect(new FadeEffect(1.0, 0.0, dFadeOutStepPercentage * dStepDuration));
        }


    }
}
