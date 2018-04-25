package org.scify.engine.renderables.effects;

import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;

import java.util.List;

/**
 * Applies an effect on a series of {@link ImageRenderable} objects, making each object fade-in after the next one,
 * until the whole series is visible. Useful for "Indiana-Jones-map-route-points" cases.
 */
public class PointRouteFadeEffectLGDX extends BaseEffect implements LGDXIgnorableEffect {
    protected static final String POINT_RENDERABLES = "POINT_RENDERABLES";
    public static final String INFO_FADE_APPLIED = "INFO_FADE_APPLIED";
    public static final String INFO_FADE_EFFECTS = "INFO_FADE_EFFECTS";

    public PointRouteFadeEffectLGDX(double dDurationMSec, List<Renderable> lirPointRenderables) {
        super(dDurationMSec);

        // Keep image renderables
        setObjectParameter(POINT_RENDERABLES, lirPointRenderables);
        setNumericParameter(INFO_FADE_APPLIED, 0.0);
    }

    public PointRouteFadeEffectLGDX(Effect eSource) {
        super(eSource);
    }


    /**
     * Applies (once) the point route sub-effects to the set of stored {@link org.scify.engine.renderables.Renderable}
     * objects. WARNING: Ignores the target parameter.
     * @param target Ignored.
     * @return The target, unchanged.
     */
    @Override
    public synchronized EffectTarget applyTo(EffectTarget target) {
        // The first time
        if (getNumericParameter(INFO_FADE_APPLIED) == 0) {
            // Share that we have applied the fading
            setNumericParameter(INFO_FADE_APPLIED, 1.0);

            List<ImageRenderable> lirPoints = (List<ImageRenderable>)getObjectParameter(POINT_RENDERABLES);

            // Determine single point step duration
            double dStepDuration = getNumericParameter(PARAM_DURATION) /  lirPoints.size();
            int iCnt = 0;
            // For every image renderable
            for (ImageRenderable ir : lirPoints) {

                // Add appropriate delay per point
                EffectSequence es = new EffectSequence();
                // Init by making invisible
                es.addEffect(new SetAlphaEffect(0.0));

                if (iCnt > 0) {
                    es.addEffect(new DelayEffect(dStepDuration * iCnt));
                }
                iCnt++;

                // Add fade-in
                es.addEffect(new FadeEffect(0.0, 1.0, dStepDuration - 100));

                // Apply effect list to point
                ir.apply(es);
            }
        }
        return super.applyTo(target);
    }
}
