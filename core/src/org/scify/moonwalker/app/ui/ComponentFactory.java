package org.scify.moonwalker.app.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.UserInputHandler;
import org.scify.moonwalker.app.helpers.ResourceLocator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

public abstract class ComponentFactory<T> {

    protected Skin skin;
    protected ResourceLocator resourceLocator;
    protected UserInputHandler userInputHandler;
    protected java.util.List<Texture> textureList;
    public ComponentFactory(Skin skin) {
        this.skin = skin;
        this.resourceLocator = new ResourceLocator();
        textureList = Collections.synchronizedList(new ArrayList<Texture>());
    }

    public abstract T createResourceForType(Renderable toDraw);

    public void setUserInputHandler(UserInputHandler userInputHandler) {
        this.userInputHandler = userInputHandler;
    }

    public void disposeResources() {
        ListIterator<Texture> listIterator = textureList.listIterator();
        while (listIterator.hasNext()) {
            Texture currentTexture = listIterator.next();
            currentTexture.dispose();
            listIterator.remove();
        }
    }
}
