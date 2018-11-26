package org.scify.engine.renderables.effects;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BaseEffect implements Effect {
    /**
     * Supplies auto-inc to support ordering of effects based on their creation time
      */

    protected static AtomicLong idGenerator = new AtomicLong();

    protected long effectOrder;
    protected Map<String,String> params;
    protected Map<String,Object> objectParams;

    public static final String PARAM_DURATION = "PARAM_DURATION";
    public static final String INFO_START_TIME = "INFO_START_TIME";
    public static final String PARAM_EXECUTE_AT_LEAST_ONCE = "PARAM_EXECUTE_AT_LEAST_ONCE";
    public static final String PARAM_EXECUTE_FINAL_STEP = "PARAM_EXECUTE_FINAL_STEP";
    public static final String INFO_EXECUTED_ONCE = "INFO_EXECUTED_ONCE";
    public static final String INFO_EXECUTED_FINAL_STEP = "INFO_EXECUTED_FINAL_STEP";


    /**
     * Indicates whether the effect needs to be interrupted.
     */
    protected boolean bStopped = false;
    /**
     * Signals whether the effect was executed, at least once.
     */

    protected void initMaps() {
        params = new TreeMap<>();
        objectParams = new TreeMap<>();
    }

    public BaseEffect(double dDurationMSec, boolean bExecuteOnce, boolean bExecuteFinalStep) {
        initMaps();
        setNumericParameter(PARAM_DURATION, dDurationMSec);
        setBooleanParameter(PARAM_EXECUTE_AT_LEAST_ONCE, bExecuteOnce);
        setBooleanParameter(PARAM_EXECUTE_FINAL_STEP, bExecuteFinalStep);
        effectOrder = idGenerator.incrementAndGet();
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
        // TODO: Should we also copy the source ID?
        effectOrder = idGenerator.incrementAndGet();
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



        boolean bCompleted = true;

        // Either no one asked for the execution of the final step, or we have already executed it, so we are complete on this aspect.
        bCompleted = bCompleted &&  (!getBooleanParameter(PARAM_EXECUTE_FINAL_STEP) || getBooleanParameter(INFO_EXECUTED_FINAL_STEP));
        // Either no one asked for at least one execution, or we have already done it, so we are complete on this aspect.
        bCompleted = bCompleted &&  (!getBooleanParameter(PARAM_EXECUTE_AT_LEAST_ONCE) || getBooleanParameter(INFO_EXECUTED_ONCE));

        // Calculate remaining time
        double dStart = Double.MAX_VALUE;
        if (params.containsKey(INFO_START_TIME))
            dStart = Double.valueOf(params.get(INFO_START_TIME));
        double dRemaining = getDuration() - (new Date().getTime() - dStart);
        // If we have passed the required time, we also are complete in this aspect
        bCompleted = bCompleted && (dRemaining < 10e-5);

        // This returns "completed" only if we are complete regarding all the aspects.
        return bCompleted;
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
    public boolean setBooleanParameter(String sParamName, boolean bNewValue) {
        boolean bPrv = getBooleanParameter(sParamName);

        params.put(sParamName, String.valueOf(bNewValue));
        return bPrv;
    }

    @Override
    public boolean getBooleanParameter(String sParamName) {
        return Boolean.valueOf(getParameter(sParamName));
    }

    @Override
    public synchronized double getDuration() {
        return getNumericParameter(PARAM_DURATION);

    }

    @Override
    public synchronized void stop() {
        bStopped = true;
    }

    @Override
    public EffectTarget addEffectTo(EffectTarget target) {
        target.addEffect(this);

        return target;
    }

    @Override
    public int compareTo(Effect o) {
        if (this.equals(o))
            return 0;
        long myOrder = effectOrder;
        long otherOrder;

        if (o instanceof BaseEffect) {
             otherOrder = ((BaseEffect)o).effectOrder;
        } else {
            throw new IncompatibleClassChangeError("Cannot compare a generic effect to a BaseEffect subclass.");
        }

        return (int)(myOrder - otherOrder);
    }
}
