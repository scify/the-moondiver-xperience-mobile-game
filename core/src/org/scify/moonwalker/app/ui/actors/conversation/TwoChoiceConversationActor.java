package org.scify.moonwalker.app.ui.actors.conversation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.TwoChoiceConversationRenderable;
import org.scify.moonwalker.app.helpers.AppInfo;
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
public class TwoChoiceConversationActor extends TableActor {

    protected ResourceLocator resourceLocator;
    protected AppInfo appInfo;
    protected Image background;
    protected Button topAnswerButton;
    protected Button botAnswerButton;
    protected TwoChoiceConversationRenderable renderable;
    protected Image avatarImage;
    protected Image avatarBG;

    public TwoChoiceConversationActor(Skin skin, TwoChoiceConversationRenderable renderable) {
        super(skin, renderable);
        this.renderable = renderable;
        appInfo = AppInfo.getInstance();
        resourceLocator = new ResourceLocator();
    }

    public void init(Button topAnswerButton, Button botAnswerButton) {
        float screenWidth = appInfo.getScreenWidth();
        float screenHeight = appInfo.getScreenHeight();
        Stack stack = new Stack();
        Texture chatBox = new Texture(resourceLocator.getFilePath("img/conversations/bg.png"));
        float width = convertWidth(chatBox.getWidth());
        float height = convertHeight(chatBox.getHeight());

        setWidth(screenWidth);
        setHeight(height * 1.3f);
        background = new Image(new TextureRegionDrawable(new TextureRegion(chatBox)));
        stack.add(background);

        Table table = new Table();
        table.defaults();
        table.center();

        //avatar
        Stack avatarStack = new Stack();

        Texture avatarBGTexture = new Texture(resourceLocator.getFilePath("img/avatars/bg.png"));
        avatarBG = new Image(new TextureRegionDrawable(new TextureRegion(avatarBGTexture)));
        avatarBG.setWidth(convertWidth(200));
        avatarBG.setScaling(Scaling.fillX);
        avatarStack.add(avatarBG);
        Texture avatar = imgUrlToTexture(renderable.getRelativeAvatarPath());
        avatarImage = new Image(new TextureRegionDrawable(new TextureRegion(avatar)));
        avatarImage.setWidth(convertWidth(200));
        avatarImage.setScaling(Scaling.fillX);
        avatarStack.add(avatarImage);
        table.add(avatarStack).left().width(avatarImage.getWidth());
        float widthLeftForButtons = width - avatarImage.getWidth() - (0.05f * width);


        table.add().width(0.02f * width).height(height * 0.9f);

        //button
        Table buttonsTable = new Table();
        buttonsTable.defaults();
        buttonsTable.center();

        buttonsTable.add(topAnswerButton).right().width(widthLeftForButtons);
        buttonsTable.row();
        buttonsTable.add().height(height * 0.2f);
        buttonsTable.row();
        buttonsTable.add(botAnswerButton).right().width(widthLeftForButtons);
        table.add(buttonsTable).width(widthLeftForButtons);
        table.add().width(0.02f * width);

        stack.add(table);
        add(stack).height(height).width(width).center();

    }

    protected float convertHeight(float initialHeight) {
        int initialBackgroundHeight = 1080;
        float ret = appInfo.getScreenHeight() * initialHeight;
        ret = ret / initialBackgroundHeight;
        return ret;
    }

    protected float convertWidth(float initialWidth) {
        int initialBackgroundWidth = 1920;
        float ret = appInfo.getScreenWidth() * initialWidth;
        ret = ret / initialBackgroundWidth;
        return ret;
    }
}
