package org.scify.engine.renderables.effects;

import com.badlogic.gdx.utils.compression.lzma.Base;

public class VisibilityEffect extends BaseEffect {

    public static final String PARAM_VISIBILITY = "PARAM_VISIBILITY";

    public VisibilityEffect(final boolean bSetVisibilityTo) {
        super(Double.MAX_VALUE);

        setNumericParameter(PARAM_VISIBILITY, bSetVisibilityTo == true ? 1.0 : 0.0);
    }

    public VisibilityEffect(Effect eSource) {
        super(eSource);
    }

}
