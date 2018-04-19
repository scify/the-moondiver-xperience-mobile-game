package org.scify.moonwalker.app.ui.actors.conversation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.renderables.NextConversationRenderable;
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
public class TwoChoiceConversationActor extends TableActor<TwoChoiceConversationRenderable> {

    protected ResourceLocator resourceLocator;
    protected AppInfo appInfo;
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
        float width = renderable.getWidth();
        float height = renderable.getHeight();
        defaults();
        center();
        setWidth(width);
        setHeight(height);
        addBackground(renderable.getTableBGRenderable(), width, height);

        add().height(height).width(0.01f * width);

        //avatar
        Stack avatarStack = new Stack();
        Texture avatarBGTexture = new Texture(resourceLocator.getFilePath("img/avatars/bg.png"));
        avatarBG = new Image(new TextureRegionDrawable(new TextureRegion(avatarBGTexture)));
        avatarBG.setWidth(0.12f * width);
        avatarBG.setScaling(Scaling.fillX);
        avatarStack.add(avatarBG);
        Texture avatar = imgUrlToTexture(renderable.getRelativeAvatarPath());
        avatarImage = new Image(new TextureRegionDrawable(new TextureRegion(avatar)));
        avatarImage.setWidth(0.12f * width);
        avatarImage.setScaling(Scaling.fillX);
        avatarStack.add(avatarImage);
        add(avatarStack).width(avatarBG.getWidth());

        add().height(height).width(0.01f * width);

        //button
        Table buttonsTable = new Table();
        buttonsTable.defaults();
        buttonsTable.center();
        float buttonWidth = 0.85f * width;
        float buttonHeight = 0.3f * height;
        buttonsTable.add(topAnswerButton).width(buttonWidth).height(buttonHeight);
        buttonsTable.row();
        buttonsTable.add().height(buttonHeight/2);
        buttonsTable.row();
        buttonsTable.add(botAnswerButton).width(buttonWidth).height(buttonHeight);
        add(buttonsTable).width(buttonWidth);

        add().height(height).width(0.01f * width);
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
