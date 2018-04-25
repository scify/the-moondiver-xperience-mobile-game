package org.scify.engine.renderables.effects;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class EffectSequence extends BaseEffect {
    public  static final String PARAM_SERIES_OF_EFFECTS = "PARAM_SERIES_OF_EFFECTS";

    public  static final String INFO_CURRENT_EFFECT = "INFO_CURRENT_EFFECT";
    public static final String INFO_CURRENT_EFFECT_ITERATOR = "INFO_CURRENT_EFFECT_ITERATOR";

    @Override
    public synchronized void stop() {
        if (getObjectParameter(INFO_CURRENT_EFFECT) != null) {
            ((Effect)getObjectParameter(INFO_CURRENT_EFFECT)).stop();
        }

        super.stop();
    }

    public EffectSequence() {
        super(Double.MAX_VALUE);

        // Initialize list
        setObjectParameter(PARAM_SERIES_OF_EFFECTS, new LinkedList<>());
        resetCurrent();
        updateDuration();
    }

    public EffectSequence(List<Effect> oneAfterTheOther) {
        super(Double.MAX_VALUE);

        // Initialize list
        setObjectParameter(PARAM_SERIES_OF_EFFECTS, new LinkedList<>(oneAfterTheOther));

        resetCurrent();
        updateDuration();
    }

    @Override
    public synchronized double getDuration() {
        return updateDuration();
    }

    protected double updateDuration() {
        double dDuration = 0.0;

        for (Effect e : ((List<Effect>)getObjectParameter(PARAM_SERIES_OF_EFFECTS))) {
            dDuration += e.getDuration();
        }

        // Update params
        setNumericParameter(PARAM_DURATION, dDuration);

        return dDuration;
    }

    @Override
    public EffectTarget applyTo(EffectTarget target) {
        super.applyTo(target);
        Effect eCurrent = getCurrentEffect();

        // If no current effect set
        if (eCurrent != null) {

            // If the current effect is complete
            if (eCurrent.complete()) {
                // DEBUG LINES
//                System.err.println("Completed " + eCurrent.toString());
                //////////////

                // If series has not been completed
                if (hasNextEffect()) {
                    eCurrent = getNextEffect();
                    // Move to the next effect
                }
                else {
                    eCurrent = null; // Else render current to null
                }
                // Update info
                setCurrentEffect(eCurrent);
            }

            // If we have a current effect
            if (getCurrentEffect() != null)
                // Apply it
                eCurrent.applyTo(target);
        }

        return target;
    }

    protected  Effect getCurrentEffect() {
        return (Effect)(getObjectParameter(INFO_CURRENT_EFFECT));
    }

    protected void resetCurrent() {
        // Reset current
        moveToFirstEffect();
        if (hasNextEffect())
            setCurrentEffect(getNextEffect());
        else
            setCurrentEffect(null);
    }

    protected boolean hasNextEffect() {
        return ((ListIterator<Effect>)getObjectParameter(INFO_CURRENT_EFFECT_ITERATOR)).hasNext();
    }

    protected Effect getNextEffect() {
        return ((ListIterator<Effect>)getObjectParameter(INFO_CURRENT_EFFECT_ITERATOR)).next();
    }

    protected void moveToFirstEffect() {
        ListIterator<Effect> effectListIterator = getSeriesOfEffects().listIterator();
        setObjectParameter(INFO_CURRENT_EFFECT_ITERATOR, effectListIterator);
    }

    public void setCurrentEffect(Effect eCurrent) {
        setObjectParameter(INFO_CURRENT_EFFECT, eCurrent);
    }

    public void addEffect(Effect eToAdd) {
        getSeriesOfEffects().add(eToAdd);
        resetCurrent();
        updateDuration();
    }

    public void removeEffect(Effect eToAdd) {
        getSeriesOfEffects().remove(eToAdd);
        resetCurrent();
        updateDuration();
    }

    public void clear() {
        getSeriesOfEffects().clear();
        resetCurrent();
        updateDuration();
    }

    public List<Effect> getSeriesOfEffects() {
        return ((List)getObjectParameter(PARAM_SERIES_OF_EFFECTS));
    }
}
