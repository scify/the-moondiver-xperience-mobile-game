package org.scify.engine.renderables.effects;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class EffectSequence extends BaseEffect {
    public  static final String PARAM_SERIES_OF_EFFECTS = "PARAM_SERIES_OF_EFFECTS";
    public  static final String INFO_CURRENT_FIELD = "INFO_CURRENT_FIELD";

    protected List<Effect> seriesOfEffects;
    protected ListIterator<Effect> ieCurrent;
    protected Effect eCurrent;
    double dDuration;

    public EffectSequence() {
        super(0.0);

        // Initialize list
        seriesOfEffects = new LinkedList<>();

        setObjectParameter(PARAM_SERIES_OF_EFFECTS, seriesOfEffects);
        resetCurrent();
        updateDuration();
    }

    public EffectSequence(List<Effect> oneAfterTheOther) {
        // Initialize list
        seriesOfEffects = new LinkedList<>(oneAfterTheOther);
        resetCurrent();
        updateDuration();
    }

    @Override
    public synchronized double getDuration() {

        return dDuration;
    }

    private double updateDuration() {

        for (Effect e : seriesOfEffects) {
            dDuration += e.getDuration();
        }

        // Update params
        double dRes = setNumericParameter(PARAM_DURATION, dDuration);

        return dDuration;
    }

    @Override
    public EffectTarget applyTo(EffectTarget target) {
        super.applyTo(target);

        // If no current effect set
        if (eCurrent != null) {

            // If the current effect is complete
            if (eCurrent.complete()) {
                // DEBUG LINES
                System.err.println("Completed " + eCurrent.toString());
                //////////////

                // If series has not been completed
                if (ieCurrent.hasNext())
                    // Move to the next effect
                    eCurrent = ieCurrent.next();
                else
                    eCurrent = null; // Else render current to null
            }

            if (eCurrent != null)
                // Apply the current effect
                eCurrent.applyTo(target);
        }

        return target;
    }

    protected void resetCurrent() {
        // Reset current
        ieCurrent = seriesOfEffects.listIterator();
        if (ieCurrent.hasNext())
            eCurrent = ieCurrent.next();
        else
            eCurrent = null;

        setObjectParameter(INFO_CURRENT_FIELD, eCurrent);

    }

    public void addEffect(Effect eToAdd) {
        seriesOfEffects.add(eToAdd);
        resetCurrent();
        updateDuration();
    }

    public void clear() {
        seriesOfEffects.clear();
        resetCurrent();
        updateDuration();
    }

    public List<Effect> getSeriesOfEffects() {
        return new ArrayList<>((List)getObjectParameter(PARAM_SERIES_OF_EFFECTS));
    }
}
