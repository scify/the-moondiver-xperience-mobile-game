package org.scify.engine.renderables.effects;

public class SetAlphaEffect extends BaseEffect {
    public static final String PARAM_ALPHA = "PARAM_ALPHA";

    public SetAlphaEffect(double dAlpha) {
        super(Double.MAX_VALUE);

        setNumericParameter(PARAM_ALPHA, dAlpha);
    }

    public SetAlphaEffect(Effect eSource) {
        super(eSource);
    }

    @Override
    public synchronized EffectTarget applyTo(EffectTarget target) {
        stop();
        return super.applyTo(target);
    }
}
