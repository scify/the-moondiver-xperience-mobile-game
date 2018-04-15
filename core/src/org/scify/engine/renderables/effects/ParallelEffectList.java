package org.scify.engine.renderables.effects;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class ParallelEffectList extends BaseEffect {
    protected List<Effect> seriesOfEffects;
    protected ListIterator<Effect> ieCurrent;
    protected Effect eCurrent;

    public ParallelEffectList() {
        // Initialize list
        seriesOfEffects = new LinkedList<>();
        updateDuration();
    }

    public ParallelEffectList(List<Effect> oneAfterTheOther) {
        // Initialize list
        seriesOfEffects = new LinkedList<>(oneAfterTheOther);
        updateDuration();
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
            dDuration = Math.max(dDuration, e.getDuration());
        }

        // Update params
        params.put(PARAM_DURATION, String.valueOf(dDuration));

        return dDuration;
    }


    public void addEffect(Effect eToAdd) {
        seriesOfEffects.add(eToAdd);
        updateDuration();
    }

    public void clear() {
        seriesOfEffects.clear();
        updateDuration();
    }


    @Override
    public EffectTarget applyTo(EffectTarget target) {
        super.applyTo(target);

        for (Effect e: this.seriesOfEffects) {
            e.applyTo(target);
        }

        return target;
    }


}
