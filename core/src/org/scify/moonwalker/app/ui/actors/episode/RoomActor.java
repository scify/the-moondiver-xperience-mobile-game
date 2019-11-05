package org.scify.moonwalker.app.ui.actors.episode;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.moonwalker.app.ui.actors.FadingTableActor;
import org.scify.moonwalker.app.ui.actors.ImageWithEffect;
import org.scify.moonwalker.app.ui.actors.StackWithEffect;
import org.scify.moonwalker.app.ui.renderables.RoomRenderable;

public class RoomActor extends FadingTableActor<RoomRenderable> {

    protected ImageWithEffect phoneOffImage;
    protected ImageWithEffect phoneOnImage;
    protected Button skipDialogButton;

    public RoomActor(Skin skin, RoomRenderable renderable) {
        super(skin, renderable);
        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        addBackground(renderable.getTableBGRenderable());
        init();
    }

    protected synchronized void init() {
        // Table elements
        phoneOffImage = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getPhoneOffRenderable());
        phoneOnImage = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getPhoneOnRenderable());
        StackWithEffect phonesStack = new StackWithEffect<>();
        phonesStack.add(phoneOffImage);
        phonesStack.add(phoneOnImage);
        add(phonesStack).width(convertWidth(phoneOffImage.getWidth())).height(convertHeight(phoneOffImage.getHeight()));

        // Non-table, positioned elements
        skipDialogButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getSkipDialogButtonRenderable());
        ActionButtonRenderable rSkipBtn = renderable.getSkipDialogButtonRenderable();
        float ratio = 1.0f;
        float height = getHeight() * 0.15f;
        float width = ratio * height;
        rSkipBtn.setHeight(height);
        rSkipBtn.setWidth(width);
        rSkipBtn.setyPos(appInfo.convertY(0.80f * 1080));
        rSkipBtn.setxPos(appInfo.convertX( (0.92f - (width / 1920)) * 1920));
        rSkipBtn.markAsNeedsUpdate();
    }

}
