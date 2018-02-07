package org.scify.moonwalker.app.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.scify.engine.Renderable;
import org.scify.moonwalker.app.helpers.ResourceLocator;

public abstract class ComponentFactory<T> {

    protected Skin skin;
    protected ResourceLocator resourceLocator;

    public ComponentFactory(Skin skin) {
        this.skin = skin;
        this.resourceLocator = new ResourceLocator();
    }

    public abstract T createResourceForType(Renderable toDraw);
}
