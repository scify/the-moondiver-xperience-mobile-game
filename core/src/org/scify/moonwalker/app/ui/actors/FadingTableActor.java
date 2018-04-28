package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.scify.moonwalker.app.ui.renderables.FadingTableRenderable;

public class FadingTableActor<T extends FadingTableRenderable> extends TableActor<T> {


    public FadingTableActor(Skin skin, T rRenderable) {
        super(skin, rRenderable);
    }

    protected void init() {
        renderable.fadeIn();
    }
}
