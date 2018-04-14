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
     * Returns the current state info of an effect
     * @param effectOfInterest The effect of interest.
     * @return A map of settings (string key-value pairs) indicating the current state of the selected effect.
     */
    public Map<String,String> getEffectInfo(Effect effectOfInterest);

    /**
     * Updates the current state info of an effect
     * @param effectOfInterest The effect the information of which we will update.
     * @param updatedInfo The updated info of the effect.
     */
    public void setEffectInfo(Effect effectOfInterest, Map<String, String> updatedInfo);

    /**
     * Removes a selected event from the active effects on the target.
     * Usually applied to complete effect that no longer need to be applied.
     * @param eToRemove The effect to remove.
     */
    public void removeEffect(Effect eToRemove);
}
