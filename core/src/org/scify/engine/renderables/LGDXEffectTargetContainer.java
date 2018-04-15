package org.scify.engine.renderables;

import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.List;

/**
 * Describes the class of containers that are used to apply effects to the contained renderables.
 */
public interface LGDXEffectTargetContainer {
    /**
     * Returns the actual actors (usually one) in the container.
     *
     * @return A list of renderable items to which the effect is applied, through the container.
     */
    public List<Actor> getContainedActors();

    /**
     * Returns the first actual actor in the container.
     *
     * @return A renderable item to which the effect is applied, through the container.
     */
    public Actor getFirstContainedActor();
}
