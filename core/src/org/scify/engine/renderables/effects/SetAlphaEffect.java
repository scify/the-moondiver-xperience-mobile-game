package org.scify.engine.renderables.effects;

public class SetAlphaEffect extends BaseEffect {
    public static final String PARAM_ALPHA = "PARAM_ALPHA";

    public SetAlphaEffect(double dAlpha) {
        super(0.0, true, true);

        setNumericParameter(PARAM_ALPHA, dAlpha);
    }

    public SetAlphaEffect(Effect eSource) {
        super(eSource);
    }

    @Override
    public synchronized EffectTarget applyTo(EffectTarget target) {
        setBooleanParameter(INFO_EXECUTED_ONCE, true);
        setBooleanParameter(INFO_EXECUTED_FINAL_STEP, true);
        return super.applyTo(target);
    }
}
