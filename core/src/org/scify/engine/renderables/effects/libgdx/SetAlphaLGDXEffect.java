package org.scify.engine.renderables.effects.libgdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.SetAlphaEffect;

public class SetAlphaLGDXEffect extends SetAlphaEffect implements LGDXEffect {

    public SetAlphaLGDXEffect(double dAlpha) {
        super(dAlpha);
    }

    public SetAlphaLGDXEffect(Effect eSource) {
        super(eSource);
    }

    @Override
    public void applyToActor(Actor aTarget, Renderable rRenderable) {
        Color cTarget = aTarget.getColor();
        cTarget.a = (float)(getNumericParameter(PARAM_ALPHA).doubleValue());
        aTarget.setColor( cTarget );
        stop();

        super.applyTo(rRenderable);
    }

    @Override
    public void applyToSprite(Sprite sTarget, Renderable rRenderable) {
        Color cTarget = sTarget.getColor();
        cTarget.a = (float)(getNumericParameter(PARAM_ALPHA).doubleValue());
        sTarget.setColor( cTarget );
        stop();

        super.applyTo(rRenderable);

    }
}
