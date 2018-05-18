package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.EffectTarget;
import org.scify.moonwalker.app.helpers.ResourceLocator;

import java.util.HashSet;
import java.util.Set;

public class ImageWithEffect extends Image implements EffectTarget, Updateable {
    protected ResourceLocator resourceLocator;

    public ImageWithEffect() {
        super();
    }

    public ImageWithEffect(NinePatch patch) {
        super(patch);
    }

    public ImageWithEffect(TextureRegion region) {
        super(region);
    }

    public ImageWithEffect(Texture texture) {
        super(texture);
    }

    public ImageWithEffect(Skin skin, String drawableName) {
        super(skin, drawableName);
    }

    public ImageWithEffect(Drawable drawable, ResourceLocator resourceLocator) {
        super(drawable);
        this.resourceLocator = resourceLocator;
    }

    public ImageWithEffect(Drawable drawable, Scaling scaling) {
        super(drawable, scaling);
    }

    public ImageWithEffect(Drawable drawable, Scaling scaling, int align) {
        super(drawable, scaling, align);
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
        setZIndex(renderable.getZIndex());
        ImageRenderable imageRenderable = (ImageRenderable) renderable;
        if (imageRenderable.getImgPathWasUpdated()) {
            this.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(resourceLocator.getFilePath(imageRenderable.getImgPath())))));
            imageRenderable.setImgPathWasUpdated(false);
        }
        renderable.wasUpdated();
    }
}
