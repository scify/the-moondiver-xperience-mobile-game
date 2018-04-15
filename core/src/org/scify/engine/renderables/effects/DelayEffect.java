package org.scify.engine.renderables.effects;

public class DelayEffect extends BaseEffect {
    public DelayEffect(double dDuration) {
        super();

        this.params.put(this.PARAM_DURATION, String.valueOf(dDuration)
        );
    }

}
