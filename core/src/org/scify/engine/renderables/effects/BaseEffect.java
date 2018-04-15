package org.scify.engine.renderables.effects;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class BaseEffect implements Effect {
    protected Map<String,String> params;

    public static final String PARAM_DURATION = "duration";
    public static final String INFO_START_TIME = "startTime";

    public BaseEffect() {
        params = new TreeMap<>();
        // Default duration is zero
        // params.put(PARAM_DURATION, "0.0");
    }

    public BaseEffect(double dDurationMSec) {
        params = new TreeMap<>();
        params.put(PARAM_DURATION, String.valueOf(dDurationMSec));
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
        if (!params.containsKey(INFO_START_TIME) || (Double.valueOf(params.get(INFO_START_TIME)) == Double.MAX_VALUE))
            params.put(INFO_START_TIME, String.valueOf(new Date().getTime()));
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
    public synchronized boolean complete() {
        double dStart = Double.MAX_VALUE;
        if (params.containsKey(INFO_START_TIME))
                dStart = Double.valueOf(params.get(INFO_START_TIME));

        double dRemaining = getDuration() - (new Date().getTime() - dStart);

        return dRemaining < 10e-5;
    }

    @Override
    public synchronized double getDuration() {
        return Double.valueOf(params.get(PARAM_DURATION));

    }
}
