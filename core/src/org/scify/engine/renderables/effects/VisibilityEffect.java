package org.scify.engine.renderables.effects;

import com.badlogic.gdx.utils.compression.lzma.Base;

public class VisibilityEffect extends BaseEffect {

    public static final String PARAM_VISIBILITY = "PARAM_VISIBILITY";

    public VisibilityEffect(final boolean bSetVisibilityTo) {
        super(0.0, true, false);

        setNumericParameter(PARAM_VISIBILITY, bSetVisibilityTo == true ? 1.0 : 0.0);
    }

    public VisibilityEffect(Effect eSource) {
        super(eSource);
    }

    @Override
    public synchronized EffectTarget applyTo(EffectTarget target) {
        setBooleanParameter(INFO_EXECUTED_ONCE, true);
        return super.applyTo(target);
    }
}
