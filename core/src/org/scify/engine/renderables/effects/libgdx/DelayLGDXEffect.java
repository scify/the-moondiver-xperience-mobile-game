package org.scify.engine.renderables.effects.libgdx;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.DelayEffect;
import org.scify.engine.renderables.effects.Effect;

public class DelayLGDXEffect extends DelayEffect implements LGDXEffect {
    public DelayLGDXEffect(double dDuration) {
        super(dDuration);
    }

    public DelayLGDXEffect(Effect eSource) {
        super(eSource);
    }

    @Override
    public void applyToActor(Actor aTarget, Renderable rRenderable) {
        super.applyTo(rRenderable);

        // Do nothing, i.e. delay!
//        System.err.println("Called delay!");
    }

    @Override
    public void applyToSprite(Sprite sTarget, Renderable rRenderable) {
        super.applyTo(rRenderable);

        // Do nothing, i.e. delay!
//        System.err.println("Called delay!");
    }
}
