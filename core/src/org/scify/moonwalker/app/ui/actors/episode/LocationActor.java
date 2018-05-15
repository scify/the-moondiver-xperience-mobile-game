package org.scify.moonwalker.app.ui.actors.episode;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.scify.moonwalker.app.ui.actors.FadingTableActor;
import org.scify.moonwalker.app.ui.renderables.LocationRenderable;

public class LocationActor extends FadingTableActor<LocationRenderable> {

    public LocationActor (Skin skin, LocationRenderable renderable) {
        super(skin, renderable);
        timestamp = this.renderable.getRenderableLastUpdated();
        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        addBackground(renderable.getTableBGRenderable());
    }
}
