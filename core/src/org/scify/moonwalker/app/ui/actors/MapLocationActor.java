package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import org.scify.moonwalker.app.ui.renderables.MapEpisodeRenderable;

public class MapLocationActor extends Actor {

    protected MapEpisodeRenderable renderable;

    public MapLocationActor(Skin skin, MapEpisodeRenderable renderable) {
        super();
        this.renderable = renderable;
    }


}
