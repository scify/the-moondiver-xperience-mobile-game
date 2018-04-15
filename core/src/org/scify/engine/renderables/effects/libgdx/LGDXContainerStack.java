package org.scify.engine.renderables.effects.libgdx;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import org.scify.engine.renderables.LGDXEffectTargetContainer;

import java.util.Arrays;
import java.util.List;

public class LGDXContainerStack extends Stack implements LGDXEffectTargetContainer {
    @Override
    public List<Actor> getContainedActors() {
        return Arrays.asList(getChildren().items);
    }

    public Actor getFirstContainedActor() {
        return getChildren().get(0);
    }
}
