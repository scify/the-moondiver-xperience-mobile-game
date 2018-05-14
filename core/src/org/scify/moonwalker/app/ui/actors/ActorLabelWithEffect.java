package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.TextLabelRenderable;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.EffectTarget;

import java.util.HashSet;
import java.util.Set;

public class ActorLabelWithEffect extends Label implements EffectTarget, Updateable {
    public ActorLabelWithEffect(CharSequence text, Skin skin) {
        super(text, skin);
    }

    public ActorLabelWithEffect(CharSequence text, Skin skin, String styleName) {
        super(text, skin, styleName);
    }

    public ActorLabelWithEffect(CharSequence text, Skin skin, String fontName, Color color) {
        super(text, skin, fontName, color);
    }

    public ActorLabelWithEffect(CharSequence text, Skin skin, String fontName, String colorName) {
        super(text, skin, fontName, colorName);
    }

    public ActorLabelWithEffect(CharSequence text, LabelStyle style) {
        super(text, style);
    }

    @Override
    public EffectTarget apply(Effect toApply) {
        return toApply.applyTo(this);
    }

    protected Set<Effect> effects = new HashSet<>();

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

    @Override
    public void update(Renderable renderable) {
        setText(((TextLabelRenderable) renderable).getLabel());
        setZIndex(renderable.getZIndex());
        renderable.wasUpdated();
    }
}
