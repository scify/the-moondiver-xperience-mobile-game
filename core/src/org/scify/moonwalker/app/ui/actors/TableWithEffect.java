package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.EffectTarget;

import java.util.HashSet;
import java.util.Set;

public class TableWithEffect extends Table implements EffectTarget {
    protected Set<Effect> effects = new HashSet<>();

    public TableWithEffect() {
        super();
    }

    public TableWithEffect(Skin skin) {
        super(skin);
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
