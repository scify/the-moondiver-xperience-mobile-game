package org.scify.engine.renderables.effects.libgdx;

import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.FunctionEffect;

public class FunctionLGDXEffect extends FunctionEffect implements LGDXEffect {
    public FunctionLGDXEffect(Runnable rToRun) {
        super(rToRun);
    }

    public FunctionLGDXEffect(Effect eSource) {
        super(eSource);
    }
}
