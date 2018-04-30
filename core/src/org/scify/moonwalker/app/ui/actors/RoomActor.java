package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import org.scify.engine.renderables.Renderable;
import org.scify.moonwalker.app.ui.renderables.RoomRenderable;

public class RoomActor extends FadingTableActor<RoomRenderable> implements Updateable {

    protected ImageWithEffect phoneOffImage;
    protected ImageWithEffect phoneOnImage;

    public RoomActor(Skin skin, RoomRenderable renderable) {
        super(skin, renderable);
        this.renderable = renderable;
        timestamp = this.renderable.getRenderableLastUpdated();
        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        addBackground(renderable.getTableBGRenderable());
        init();
    }

    public void init() {
        phoneOffImage = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getPhoneOffRenderable());
        phoneOnImage = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getPhoneOnRenderable());
        Stack phonesStack = new Stack();
        phonesStack.add(phoneOffImage);
        phonesStack.add(phoneOnImage);
        add(phonesStack).width(convertWidth(phoneOffImage.getWidth())).height(convertHeight(phoneOffImage.getHeight()));
        getChildrenActorsAndRenderables().put(phoneOffImage,renderable.getPhoneOffRenderable());
        getChildrenActorsAndRenderables().put(phoneOnImage,renderable.getPhoneOnRenderable());
    }

    @Override
    public void update(Renderable renderable) {
        if (this.renderable.getRenderableLastUpdated() > timestamp) {
            this.renderable = (RoomRenderable) renderable;
            this.timestamp = this.renderable.getRenderableLastUpdated();
        }
    }
}
