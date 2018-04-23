package org.scify.engine.renderables.effects;

import java.util.Map;
import java.util.Set;

public interface EffectTarget {
    /**
     * Applies an effect to the target.
     * @param toApply The effect to apply.
     * @return The affected target. Allows chaining "apply" calls to cause multiple effects.
     */
    public EffectTarget apply(Effect toApply);

    /**
     * Returns the list of (active) effects on the target.
     * @return A list of active effects.
     */
    public Set<Effect> getEffects();


    /**
     * Adds an effect to the {@link EffectTarget}
     * @param effectOfInterest The effect we need to add.
     */
    public void addEffect(Effect effectOfInterest);

    /**
     * Removes a selected event from the active effects on the target.
     * Usually applied to complete effect that no longer need to be applied.
     * @param eToRemove The effect to remove.
     */
    public void removeEffect(Effect eToRemove);
}
