package org.scify.engine.renderables.effects.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.SlideEffect;
import org.scify.moonwalker.app.helpers.AppInfo;

public class SlideLGDXEffect extends SlideEffect implements LGDXEffect {
    public SlideLGDXEffect(double dToX, double dToY, double dEntryAngle, double dDuration) {
        super(dToX, dToY, dEntryAngle, dDuration);
    }

    public SlideLGDXEffect(Effect eSource) {
        super(eSource);
    }

    @Override
    public void applyToActor(Actor aTarget, Renderable rRenderable) {
        double dRightmost = AppInfo.getInstance().getScreenWidth();
        double dTopmost = AppInfo.getInstance().getScreenWidth();

        // TODO: Complete

        super.applyTo(rRenderable);
    }

    @Override
    public void applyToSprite(Sprite sTarget, Renderable rRenderable) {
        super.applyTo(rRenderable);
    }
}
