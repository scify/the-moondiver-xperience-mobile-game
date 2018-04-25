package org.scify.engine.renderables.effects.libgdx;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.VisibilityEffect;

public class VisibilityLGDXEffect extends VisibilityEffect implements LGDXEffect {
    public VisibilityLGDXEffect(Effect eSource) {
        super(eSource);
    }

    @Override
    public void applyToActor(Actor aTarget, Renderable rRenderable) {
        boolean bNewVisibility = getNumericParameter(PARAM_VISIBILITY) == 1.0;
//        System.err.println("Setting visibility to " + String.valueOf(bNewVisibility));
        rRenderable.setVisible(bNewVisibility);
        stop();

        super.applyTo(rRenderable);
    }

    @Override
    public void applyToSprite(Sprite sTarget, Renderable rRenderable) {
        super.applyTo(rRenderable);

        boolean bNewVisibility = getNumericParameter(PARAM_VISIBILITY) == 1.0;
        if (bNewVisibility)
            sTarget.setAlpha(1.0f); // WARNING! Changes alpha!
        else
            sTarget.setAlpha(0.0f); // WARNING! Changes alpha!
        stop();

        super.applyTo(rRenderable);
    }
}
