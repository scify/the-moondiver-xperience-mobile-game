package org.scify.engine.renderables.effects;

import java.util.*;

public class BaseEffect implements Effect {
    protected Map<String,String> params;
    protected Map<String,Object> objectParams;

    public static final String PARAM_DURATION = "PARAM_DURATION";
    public static final String INFO_START_TIME = "INFO_START_TIME";

    protected boolean bStopped = false;

    public BaseEffect() {
        initMaps();
        // Default duration is zero
        // params.put(PARAM_DURATION, "0.0");
    }

    protected void initMaps() {
        params = new TreeMap<>();
        objectParams = new TreeMap<>();
    }

    public BaseEffect(double dDurationMSec) {
        initMaps();
        setNumericParameter(PARAM_DURATION, dDurationMSec);
    }

    /**
     * Creates a new effect, simply copying the parameters from a source effect.
     * @param eSource The source effect which provides the parameters.
     */
    public BaseEffect(Effect eSource) {
        initMaps();

        // For each string/numeric parameter
        for (String sStringParam : eSource.getParameters()) {
            // Copy the parameter to the new instance
            setParameter(sStringParam, eSource.getParameter(sStringParam));
        }

        // For each object parameter
        for (String sObjParamName : eSource.getObjectParameters()) {
            // Copy the parameter to the new instance
            setObjectParameter(sObjParamName, eSource.getObjectParameter(sObjParamName));
        }
    }

    /**
     * This method should be overriden by the actual effects in the subclasses.
     * Currently, it only updates the effect info based on the internal parameters object.
     * <b>Make sure you always call this from subclasses - using <i>super.applyTo</i> - to update the start time correctly.</b>
     * @param target The target of the effect.
     * @return The affected EffectTarget.
     */
    @Override
    public synchronized EffectTarget applyTo(EffectTarget target) {
        // Make sure we are not complete already
        if (complete())
            return target;

        // Init start time
        if (!params.containsKey(INFO_START_TIME) || (getNumericParameter(INFO_START_TIME) == Double.MAX_VALUE))
            setNumericParameter(INFO_START_TIME, Double.valueOf(new Date().getTime()));

        // Update target with effect info
        target.setEffectInfo(this, params);
        return target;
    }

    /**
     * This method should be overriden by the subclasses, to indicate the real set of parameters of the effect.
     * @return The set of parameter names for the effect.
     */
    @Override
    public Set<String> getParameters() {
        return params.keySet();
    }

    @Override
    public String getParameter(String sParamName) {
        return params.get(sParamName);
    }

    @Override
    public String setParameter(String sParamName, String sParamValue) {
        String sPrv = getParameter(sParamName);
        params.put(sParamName, sParamValue);

        return sPrv;
    }

    @Override
    public Double getNumericParameter(String sParamName) {
        String sRes = params.get(sParamName);
        if (sRes != null)
            return Double.valueOf(sRes);

        return null;
    }

    @Override
    public Double setNumericParameter(String sParamName, Double dNewVal) {
        String sRes = setParameter(sParamName, String.valueOf(dNewVal));
        if (sRes != null)
            return Double.valueOf(sRes);
        return null;
    }

    @Override
    public synchronized boolean complete() {
        // If stopped
        if (bStopped)
            // then also complete
            return true;

        // Calculate remaining time
        double dStart = Double.MAX_VALUE;
        if (params.containsKey(INFO_START_TIME))
                dStart = Double.valueOf(params.get(INFO_START_TIME));

        double dRemaining = getDuration() - (new Date().getTime() - dStart);

        return dRemaining < 10e-5;
    }

    @Override
    public Set<String> getObjectParameters() {
        return new HashSet<>(objectParams.keySet());
    }

    @Override
    public Object setObjectParameter(String sParamName, Object oNewValue) {
        Object oPrv = getObjectParameter(sParamName);
        objectParams.put(sParamName, oNewValue);

        return oPrv;
    }

    @Override
    public Object getObjectParameter(String sParamName) {
        if (objectParams.containsKey(sParamName))
            return objectParams.get(sParamName);

        return null;
    }

    @Override
    public synchronized double getDuration() {
        return getNumericParameter(PARAM_DURATION);

    }

    @Override
    public void stop() {
        bStopped = true;
    }
}
