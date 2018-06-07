package org.scify.moonwalker.app.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.UserInputHandler;
import org.scify.moonwalker.app.helpers.ResourceLocator;

public abstract class ComponentFactory<T> {

    protected Skin skin;
    protected LGDXRenderableBookKeeper bookKeeper;
    protected ResourceLocator resourceLocator;
    protected UserInputHandler userInputHandler;
    public ComponentFactory(Skin skin, LGDXRenderableBookKeeper bookKeeper) {
        this.skin = skin;
        this.bookKeeper = bookKeeper;
        this.resourceLocator = ResourceLocator.getInstance();
    }

    public abstract T createResourceForType(Renderable toDraw);

    public void setUserInputHandler(UserInputHandler userInputHandler) {
        this.userInputHandler = userInputHandler;
    }
}
