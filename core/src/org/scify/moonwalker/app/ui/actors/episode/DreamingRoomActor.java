package org.scify.moonwalker.app.ui.actors.episode;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import org.scify.moonwalker.app.ui.actors.FadingTableActor;
import org.scify.moonwalker.app.ui.actors.ImageWithEffect;
import org.scify.moonwalker.app.ui.renderables.DreamingRoomRenderable;
import org.scify.moonwalker.app.ui.renderables.RoomRenderable;

public class DreamingRoomActor extends FadingTableActor<DreamingRoomRenderable> {

    protected ImageWithEffect mobileImage;
    protected ImageWithEffect eyesImage;

    public DreamingRoomActor(Skin skin, DreamingRoomRenderable renderable) {
        super(skin, renderable);
        this.renderable = renderable;
        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        addBackground(renderable.getTableBGRenderable());
        init();
    }

    protected void init() {
        mobileImage = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getMobileRenderable());
        eyesImage = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getEyesRenderable());
        Stack phonesStack = new Stack();
        phonesStack.add(eyesImage);
        phonesStack.add(mobileImage);
        add(phonesStack).width(convertWidth(mobileImage.getWidth())).height(convertHeight(mobileImage.getHeight()));
    }

}
