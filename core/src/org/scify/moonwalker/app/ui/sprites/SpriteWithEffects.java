package org.scify.moonwalker.app.ui.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.EffectTarget;

import java.util.HashSet;
import java.util.Set;

public class SpriteWithEffects extends Sprite implements EffectTarget {
    protected Set<Effect> effects;

    public SpriteWithEffects() {
        super();
        effects = new HashSet<>();
    }

    public SpriteWithEffects(Texture texture) {
        super(texture);
        effects = new HashSet<>();
    }

    public SpriteWithEffects(Texture texture, int srcWidth, int srcHeight) {
        super(texture, srcWidth, srcHeight);
        effects = new HashSet<>();
    }

    public SpriteWithEffects(Texture texture, int srcX, int srcY, int srcWidth, int srcHeight) {
        super(texture, srcX, srcY, srcWidth, srcHeight);
        effects = new HashSet<>();
    }

    public SpriteWithEffects(TextureRegion region) {
        super(region);
        effects = new HashSet<>();
    }

    public SpriteWithEffects(TextureRegion region, int srcX, int srcY, int srcWidth, int srcHeight) {
        super(region, srcX, srcY, srcWidth, srcHeight);
        effects = new HashSet<>();
    }

    public SpriteWithEffects(Sprite sprite) {
        super(sprite);
        effects = new HashSet<>();
    }

    @Override
    public EffectTarget apply(Effect toApply) {
        return toApply.applyTo(this);
    }

    @Override
    public Set<Effect> getEffects() {
        return new HashSet<>(effects);
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
