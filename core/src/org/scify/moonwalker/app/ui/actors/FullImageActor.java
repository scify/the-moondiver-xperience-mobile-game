package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.scify.moonwalker.app.ui.renderables.FullImageRenderable;

public class FullImageActor extends FadingTableActor<FullImageRenderable> {

    public FullImageActor(Skin skin, FullImageRenderable renderable) {
        super(skin, renderable);
        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        addBackground(renderable.getTableBGRenderable());
    }
}
