package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import org.scify.engine.renderables.Renderable;

import java.util.Map;

public interface IContainerActor<T extends Renderable> {
    public T getRenderable();
    public Map<Actor,Renderable> getChildrenActorsAndRenderables();
}
