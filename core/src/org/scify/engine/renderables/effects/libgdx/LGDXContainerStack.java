package org.scify.engine.renderables.effects.libgdx;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import org.scify.engine.renderables.LGDXEffectTargetContainer;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.EffectTarget;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LGDXContainerStack extends Stack implements LGDXEffectTargetContainer {
    private Set<Effect> effects = new HashSet<>();


    @Override
    public List<Actor> getContainedActors() {
        return Arrays.asList(getChildren().items);
    }

    public Actor getFirstContainedActor() {
        return getChildren().get(0);
    }

    @Override
    public EffectTarget apply(Effect toApply) {
        return toApply.applyTo(this);
    }

    @Override
    public Set<Effect> getEffects() {
        return effects;
    }

    @Override
    public EffectTarget addEffect(Effect effectOfInterest) {
        effects.add(effectOfInterest);
        return this;

    }

    @Override
    public EffectTarget removeEffect(Effect eToRemove) {
        effects.remove(eToRemove);
        return this;
    }
}
