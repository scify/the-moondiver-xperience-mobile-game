package org.scify.engine.renderables.effects.libgdx;

import org.scify.engine.renderables.effects.Effect;

public class EffectNotRegisteredException extends Exception {
    protected Effect sourceEffect;

    public EffectNotRegisteredException(Effect eSourceEffect, String message) {
        super("Effect " + eSourceEffect.toString() + " has no equivalent registerd  in the current " +
                "context.\n" + message);

        sourceEffect = eSourceEffect;
    }

}
