package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import org.scify.moonwalker.app.ui.renderables.MapLocationRenderable;

public class MapLocationActor extends FadingTableActor<MapLocationRenderable> {

    protected MapLocationRenderable renderable;

    public MapLocationActor(Skin skin, MapLocationRenderable renderable) {
        super(skin, renderable);
    }


}
