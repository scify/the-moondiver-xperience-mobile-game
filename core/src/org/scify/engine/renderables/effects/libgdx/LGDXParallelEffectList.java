package org.scify.engine.renderables.effects.libgdx;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.ParallelEffectList;

public class LGDXParallelEffectList extends ParallelEffectList implements LGDXEffect {
    @Override
    public void applyToActor(Actor aTarget, Renderable rRenderable) {
        super.applyTo(rRenderable);

        // Call actual effect
        if (eCurrent != null)
            if (eCurrent instanceof LGDXEffect)
                ((LGDXEffect)eCurrent).applyToActor(aTarget, rRenderable);
    }

    @Override
    public void applyToSprite(Sprite sTarget, Renderable rRenderable) {
        super.applyTo(rRenderable);

        if (eCurrent != null)
            if (eCurrent instanceof LGDXEffect)
                ((LGDXEffect)eCurrent).applyToSprite(sTarget, rRenderable);
    }
}
