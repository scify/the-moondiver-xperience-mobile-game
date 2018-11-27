package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.EffectTarget;
import org.scify.moonwalker.app.ui.ThemeController;

import java.util.HashSet;
import java.util.Set;

public class ImageButtonWithEffect extends ImageButton implements EffectTarget, Updateable {
    protected String lastSkinType = null;

    public ImageButtonWithEffect(Skin skin) {
        super(skin);
    }

    public ImageButtonWithEffect(Skin skin, String styleName) {
        super(skin, styleName);
    }

    public ImageButtonWithEffect(ImageButtonStyle style) {
        super(style);
    }

    public ImageButtonWithEffect(Drawable imageUp) {
        super(imageUp);
    }

    public ImageButtonWithEffect(Drawable imageUp, Drawable imageDown) {
        super(imageUp, imageDown);
    }

    public ImageButtonWithEffect(Drawable imageUp, Drawable imageDown, Drawable imageChecked) {
        super(imageUp, imageDown, imageChecked);
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
        // Update skin, if needed
        if (renderable instanceof ActionButtonRenderable) {
            String sNewSkinType = ((ActionButtonRenderable) renderable).getButtonSkin();
            if (!sNewSkinType.equals(lastSkinType)) {
                setSkin(ThemeController.getInstance().getSkinByType(sNewSkinType));
                if (getSkin().has("default", ImageButton.ImageButtonStyle.class)) {
                    ImageButton.ImageButtonStyle newStyle = getSkin().get(ImageButton.ImageButtonStyle.class);
                    setStyle(newStyle);
                }
                lastSkinType = sNewSkinType;
            }
        }


        renderable.wasUpdated();
    }
}
