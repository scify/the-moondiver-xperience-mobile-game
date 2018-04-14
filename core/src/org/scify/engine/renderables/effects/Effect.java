package org.scify.engine.renderables.effects;

import java.util.Set;

public interface Effect {
    /**
     * Applies the effect to the input target.
     * @param rTarget An EffectTarget, where the effect is applied.
     * @return The affected EffectTarget.
     */
    public EffectTarget applyTo(EffectTarget rTarget);

    /**
     * Returns the set of parameters for the effect.
     * @return The set of parameter names.
     */
    public Set<String> getParameters();

    /**
     * Returns the value of the selected parameter.
     * @param sParamName The parameter to lookup.
     * @return The parameter value, or null if the parameter was not found.
     */
    public String getParameter(String sParamName);

    /**
     * Updates a selected parameter value
     * @param sParamName The name of the parameter to change.
     * @param sParamValue The new value of the parameter.
     * @return The previous value of the parameter, before the change.
     */
    public String setParameter(String sParamName, String sParamValue);

    /**
     * Returns whether the effect is now complete and can be ignored or removed.
     * @return True if the effect is complete, otherwise false.
     */
    public boolean complete();

    /**
     * Returns the (expected) duration of the effect.
     * @return The duration of the effect, up to completion, in milliseconds.
     */
    public double getDuration();
}
