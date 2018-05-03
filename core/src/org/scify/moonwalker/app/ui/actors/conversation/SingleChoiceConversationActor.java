package org.scify.moonwalker.app.ui.actors.conversation;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Scaling;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.renderables.SingleChoiceConversationRenderable;
import org.scify.moonwalker.app.helpers.ResourceLocator;
import org.scify.moonwalker.app.ui.actors.TableActor;

/**
 * This class describes the conversation component that is drawn
 * at the bottom of the screen.
 * In order for the Rendering Engine to draw the conversation line component,
 * It needs the {@link Renderable} that is supposed to be "saying" the line,
 * the {@link ConversationLine} instance that gets drawn,
 * and an image path that represents the conversation participant who is saying
 * the line.
 */
public class SingleChoiceConversationActor extends TableActor<SingleChoiceConversationRenderable>  {

    protected Label lineLabel;
    protected ResourceLocator resourceLocator;
    protected Button button;
    protected Image avatarImage;
    protected Image avatarBG;

    public SingleChoiceConversationActor(Skin skin, SingleChoiceConversationRenderable renderable) {
        super(skin, renderable);
        this.renderable = renderable;
        resourceLocator = new ResourceLocator();
        init();
    }

    protected void init() {
        float width = renderable.getWidth();
        float height = renderable.getHeight();
        defaults();
        center();
        setWidth(width);
        setHeight(height);
        addBackground(renderable.getTableBGRenderable(), width, height);

        //avatar
        add().height(height).width(0.01f * width);
        Stack avatarStack = new Stack();
        //BG
        avatarBG = (Image) bookKeeper.getUIRepresentationOfRenderable(renderable.getAvatar_bg());
        avatarBG.setWidth(0.12f * width);
        avatarBG.setScaling(Scaling.fillX);
        avatarStack.add(avatarBG);
        //CHARACTER IMAGE
        avatarImage = (Image) bookKeeper.getUIRepresentationOfRenderable(renderable.getAvatar());
        avatarImage.setWidth(0.12f * width);
        avatarImage.setScaling(Scaling.fillX);
        avatarStack.add(avatarImage);
        add(avatarStack).width(avatarBG.getWidth());

        add().height(height).width(0.01f * width);

        //Text
        lineLabel = (Label) bookKeeper.getUIRepresentationOfRenderable(renderable.getConversationText());
        lineLabel.setWrap(true);
        add(lineLabel).center().width(width * 0.64f);

        add().height(height).width(0.01f * width);

        //closeButton
        button = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getConversationButton());
        add(button).right().width(0.2f * width);
        add().height(height).width(0.01f * width);

        //debugAll();
    }
}
