package org.scify.moonwalker.app.ui.actors;

import org.scify.engine.renderables.Renderable;
import org.scify.moonwalker.app.ui.renderables.ContactScreenRenderable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ContactScreenActor extends TableActor implements Updateable {

    protected ContactScreenRenderable renderable;

    public ContactScreenActor (Skin skin, ContactScreenRenderable renderable) {
        super(skin);
        this.renderable = renderable;
        timestamp = this.renderable.getRenderableLastUpdated();
        addBackground(renderable.getImgPath());
    }

    public void init() {
        float screenHeight = getHeight();
        float screenWidth = getWidth();
    }

    @Override
    public void update(Renderable renderable) {
        if (this.renderable.getRenderableLastUpdated() > timestamp) {
            this.renderable = (ContactScreenRenderable) renderable;
            this.timestamp = this.renderable.getRenderableLastUpdated();
        }
    }

}
