package org.scify.moonwalker.app.ui.actors;


import com.badlogic.gdx.scenes.scene2d.ZIndexedStack;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.EffectTarget;

import java.util.HashSet;
import java.util.Set;

public class StackWithEffect<T extends Updateable> extends ZIndexedStack implements EffectTarget, Updateable {
    protected Set<Effect> effects = new HashSet<>();

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

    public T getBasicComponent() {
        return (T)getChildren().get(0);
    }

    @Override
    public void update(Renderable renderable) {
        setZIndex(renderable.getZIndex());
        getBasicComponent().update(renderable);
    }
}
