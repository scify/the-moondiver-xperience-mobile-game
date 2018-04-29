package org.scify.engine.renderables.effects;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class PointRouteSinglePointTypeEffect extends EffectSequence {

    public static final String POINT_LIST = "POINT_LIST";
    public static final String PARAM_FADE_IN_STEP_PERCENTAGE = "PARAM_FADE_IN_STEP_PERCENTAGE";
    public static final String PARAM_FADE_OUT_STEP_PERCENTAGE = "PARAM_FADE_OUT_STEP_PERCENTAGE";

    /**
     * Creates an effect which causes the {@link org.scify.engine.renderables.Renderable} to fade in and then fade out
     * in a sequence of given positions. The weights of fading in/out indicate the relative time dedicated to each
     * of the two events. A weight of 1.0 for fade in, 1.0 for fade out indicates that both effects will take equal time.
     * A weight of 2.0 for fade in and 1.0 for fade out indicates that fade in will take 2 times longer than fade out.
     * @param lvPointList The positions where the point will appear
     * @param dTotalDuration The duration of the whole effect
     * @param dFadeInStepDurationWeight The weight of the time will be used for fading in.
     * @param dFadeOutStepDurationWeight The weight of the time will be used for fading out.
     */
    public PointRouteSinglePointTypeEffect(List<Vector2> lvPointList, double dFadeInStepDurationWeight, double dFadeOutStepDurationWeight, double dTotalDuration) {
        super();

        setObjectParameter(POINT_LIST, lvPointList);
        setNumericParameter(PARAM_FADE_IN_STEP_PERCENTAGE, dFadeInStepDurationWeight);
        setNumericParameter(PARAM_FADE_OUT_STEP_PERCENTAGE, dFadeOutStepDurationWeight);

        double dFadeInStepPercentage = (dFadeInStepDurationWeight / (dFadeInStepDurationWeight + dFadeOutStepDurationWeight));
        double dFadeOutStepPercentage = (dFadeOutStepDurationWeight / (dFadeInStepDurationWeight + dFadeOutStepDurationWeight));

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
            // Fade in (in 2/3s of the step time)
            addEffect(new FadeEffect(0.0, 1.0, dFadeInStepPercentage * dStepDuration));
            // Fade out (in 1/3s of the step time)
            addEffect(new FadeEffect(1.0, 0.0, dFadeOutStepPercentage * dStepDuration));
        }


    }
}
