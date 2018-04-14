package org.scify.engine.renderables.effects;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class EffectList extends BaseEffect {
    protected List<Effect> seriesOfEffects;
    protected ListIterator<Effect> ieCurrent;
    protected Effect eCurrent;

    public EffectList() {
        // Initialize list
        seriesOfEffects = new LinkedList<>();
        resetCurrent();
        getDuration();
    }

    public EffectList(List<Effect> oneAfterTheOther) {
        // Initialize list
        seriesOfEffects = new LinkedList<>(oneAfterTheOther);
        resetCurrent();
        getDuration();
    }

    @Override
    public synchronized double getDuration() {
        double dDuration;

        if (!params.containsKey(PARAM_DURATION)) {
            updateDuration();
        }

        dDuration = Double.valueOf(params.get(PARAM_DURATION));

        // DEBUG LINES
        // System.err.println("Total list duration: " + String.valueOf(dDuration));
        //////////////

        return dDuration;
    }

    private double updateDuration() {
        double dDuration = 0;

        for (Effect e : seriesOfEffects) {
            dDuration += e.getDuration();
        }

        // Update params
        params.put(PARAM_DURATION, String.valueOf(dDuration));

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
}
