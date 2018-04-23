package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.EffectTarget;

import java.util.HashSet;
import java.util.Set;

public class TextButtonWithEffect extends TextButton implements EffectTarget {
    public TextButtonWithEffect(String text, Skin skin) {
        super(text, skin);
    }

    public TextButtonWithEffect(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
    }

    public TextButtonWithEffect(String text, TextButtonStyle style) {
        super(text, style);
    }

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
    public void addEffect(Effect effectOfInterest) {
        effects.add(effectOfInterest);
    }

    @Override
    public void removeEffect(Effect eToRemove) {
        effects.remove(eToRemove);
    }
}
