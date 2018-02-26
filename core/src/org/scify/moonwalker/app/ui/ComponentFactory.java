package org.scify.moonwalker.app.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.scify.engine.Renderable;
import org.scify.engine.UserInputHandler;
import org.scify.moonwalker.app.helpers.ResourceLocator;

public abstract class ComponentFactory<T> {

    protected Skin skin;
    protected ResourceLocator resourceLocator;
    protected UserInputHandler userInputHandler;

    public ComponentFactory(Skin skin) {
        this.skin = skin;
        this.resourceLocator = new ResourceLocator();
    }

    public abstract T createResourceForType(Renderable toDraw);

    public void setUserInputHandler(UserInputHandler userInputHandler) {
        this.userInputHandler = userInputHandler;
    }
}
