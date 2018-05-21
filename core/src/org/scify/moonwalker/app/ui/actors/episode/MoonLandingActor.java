package org.scify.moonwalker.app.ui.actors.episode;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.scify.moonwalker.app.ui.actors.FadingTableActor;
import org.scify.moonwalker.app.ui.renderables.MoonLandingRenderable;

public class MoonLandingActor extends FadingTableActor<MoonLandingRenderable> {

    public MoonLandingActor(Skin skin, MoonLandingRenderable renderable) {
        super(skin, renderable);
        timestamp = this.renderable.getRenderableLastUpdated();
        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        addBackground(renderable.getTableBGRenderable());
    }
}