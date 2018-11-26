package org.scify.engine.renderables.effects;

import java.util.Set;

public interface Effect extends Comparable<Effect> {
    /**
     * Applies the effect results to the input target.
     * @param rTarget An EffectTarget, where the effect is applied.
     * @return The affected EffectTarget.
     */
    public EffectTarget applyTo(EffectTarget rTarget);

    /**
     * Returns the set of parameters for the effect. Does <b>not</b> include the object parameters, even though
     * it <b>does include</b> the string representation of the numeric ones.
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
     * Returns the value of the given parameter, as a double number
     * @param sParamName The name of the parameter to look up.
     * @return The value of the parameter as a double number.
     */
    public Double getNumericParameter(String sParamName);

    /**
     * Sets the value of the given parameter, as a double number
     * @param sParamName The name of the parameter to look up.
     * @param dNewVal The new value of the parameter.
     * @return The previous value of the parameter, before the change.
     */
    public Double setNumericParameter(String sParamName, Double dNewVal);


    /**
     * Returns whether the effect is now complete and can be ignored or removed.
     * @return True if the effect is complete, otherwise false.
     */
    public boolean complete();

    /**
     * Returns the set of the names of the object parameters for the effect.
     * @return The set of parameter names.
     */
    public Set<String> getObjectParameters();

    /**
     * Updates a selected parameter value
     * @param sParamName The name of the parameter to change.
     * @param oNewValue The new value of the parameter.
     * @return The previous value of the parameter, before the change.
     */
    public Object setObjectParameter(String sParamName, Object oNewValue);

    /**
     * Returns the value of the given parameter, as an object.
     * @param sParamName The name of the parameter to look up.
     * @return The value of the parameter as a double number.
     */
    public Object getObjectParameter(String sParamName);

    /**
     * Updates a selected parameter value
     * @param sParamName The name of the parameter to change.
     * @param bNewValue The new boolean value of the parameter.
     * @return The previous value of the parameter, before the change.
     */
    public boolean setBooleanParameter(String sParamName, boolean bNewValue);

    /**
     * Returns the value of the given parameter, as a boolean value.
     * @param sParamName The name of the parameter to look up.
     * @return The value of the parameter as a boolean value.
     */
    public boolean  getBooleanParameter(String sParamName);


    /**
     * Returns the (expected) duration of the effect.
     * @return The duration of the effect, up to completion, in milliseconds.
     */
    public double getDuration();


    /**
     * Stops the effect (usually by rendering it complete).
     */
    public void stop();

    /**
     * Adds the effect to the target, for later rendering. ALWAYS use this when you expect a rendering engine
     * to render the effects.
     * @param target The effect target.
     * @return The effect target updated (allows chaining).
     */
    public EffectTarget addEffectTo(EffectTarget target);
}
