package org.scify.engine.renderables.effects.libgdx;

import org.scify.engine.renderables.effects.*;

public class LGDXEffectFactory<T extends LGDXEffect> {

    public T getEffectFor(Effect eSource) throws EffectNotRegisteredException {
        if (eSource instanceof BounceEffect) {
            return (T)bounce(eSource);
        }

        if (eSource instanceof DelayEffect) {
            return (T)delay(eSource);
        }

        if (eSource instanceof FadeEffect) {
            return (T)fade(eSource);
        }

        if (eSource instanceof FunctionEffect) {
            return (T)function(eSource);
        }

        if (eSource instanceof MoveEffect) {
            return (T)move(eSource);
        }


        if (eSource instanceof RotateEffect) {
            return (T)rotate(eSource);
        }

        if (eSource instanceof EffectSequence) {
            return (T)effectSequence(eSource);
        }


        if (eSource instanceof ParallelEffectList) {
            return (T)parallelEffectList(eSource);
        }

        // No equivalent LGDX implementation found
        throw new EffectNotRegisteredException(eSource, "Current factory:" + this.getClass().getCanonicalName());
    }

    public FunctionLGDXEffect function(Effect eSource) {
        return new FunctionLGDXEffect(eSource);
    }

    public static LGDXParallelEffectList parallelEffectList(Effect eSource) {
        return new LGDXParallelEffectList((ParallelEffectList)eSource, getFactorySingleton());
    }


    public static BounceLGDXEffect bounce(Effect eSource) {
        return new BounceLGDXEffect(eSource);
    }


    public static DelayLGDXEffect delay(Effect eSource) {
        return new DelayLGDXEffect(eSource);
    }

    public static FadeLGDXEffect fade(Effect eSource) {
        return new FadeLGDXEffect(eSource);
    }


    public static MoveLGDXEffect move(Effect eSource) {
        return new MoveLGDXEffect(eSource);
    }

    public static RotateLGDXEffect rotate(Effect eSource) {
        return new RotateLGDXEffect(eSource);
    }

    public static SlideLGDXEffect slide(Effect eSource) {
        return new SlideLGDXEffect(eSource);
    }

    public static LGDXEffectSequence effectSequence(Effect eSource) {
        return new LGDXEffectSequence((EffectSequence)eSource, getFactorySingleton());
    }

    /**
     * A placeholder for the singleton of the factory.
     */
    protected static LGDXEffectFactory singleton;
    /**
     * Creates factory singleton returning it.
     * @return The factory singleton.
     */
    public static LGDXEffectFactory getFactorySingleton() {
        if (singleton == null) {
            singleton = new LGDXEffectFactory<>();
        }

        return singleton;
    }

}
