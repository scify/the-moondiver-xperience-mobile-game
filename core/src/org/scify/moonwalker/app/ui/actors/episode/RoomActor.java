package org.scify.moonwalker.app.ui.actors.episode;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import org.scify.moonwalker.app.ui.actors.FadingTableActor;
import org.scify.moonwalker.app.ui.actors.ImageWithEffect;
import org.scify.moonwalker.app.ui.renderables.RoomRenderable;

public class RoomActor extends FadingTableActor<RoomRenderable> {

    protected ImageWithEffect phoneOffImage;
    protected ImageWithEffect phoneOnImage;
    protected Button skipDialogButton;

    public RoomActor(Skin skin, RoomRenderable renderable) {
        super(skin, renderable);
        this.renderable = renderable;
        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        addBackground(renderable.getTableBGRenderable());
        init();
    }

    protected void init() {
        phoneOffImage = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getPhoneOffRenderable());
        phoneOnImage = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getPhoneOnRenderable());
        Stack phonesStack = new Stack();
        phonesStack.add(phoneOffImage);
        phonesStack.add(phoneOnImage);
        add(phonesStack).width(convertWidth(phoneOffImage.getWidth())).height(convertHeight(phoneOffImage.getHeight()));
        skipDialogButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getSkipDialogButtonRenderable());
        Table buttonTable = new Table();
        float ratio = skipDialogButton.getWidth() /skipDialogButton.getHeight();
        float height = getHeight() * 0.15f;
        buttonTable.add(skipDialogButton).height(height).width(ratio * height);
        buttonTable.padTop(0.03f * getHeight());
        add(buttonTable).expand().top();
        add().width(0.2f *getWidth()).height(getHeight());
    }

}
