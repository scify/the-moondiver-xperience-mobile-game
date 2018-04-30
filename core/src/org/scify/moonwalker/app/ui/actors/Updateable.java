package org.scify.moonwalker.app.ui.actors;


import org.scify.engine.renderables.Renderable;

public interface Updateable<T extends Renderable> {
    void update(T renderable);
}
