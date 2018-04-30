package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.scify.moonwalker.app.ui.renderables.ForestRenderable;

public class ForestActor extends FadingTableActor<ForestRenderable> {

    public ForestActor(Skin skin, ForestRenderable renderable) {
        super(skin, renderable);
        timestamp = this.renderable.getRenderableLastUpdated();
        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        addBackground(renderable.getTableBGRenderable());
    }
}