package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.renderables.SingleConversationLine;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;
import org.scify.moonwalker.app.ui.ThemeController;

/**
 * This class describes the conversation component that is drawn
 * at the bottom of the screen.
 * In order for the Rendering Engine to draw the conversation line component,
 * It needs the {@link Renderable} that is supposed to be "saying" the line,
 * the {@link ConversationLine} instance that gets drawn,
 * and an image path that represents the conversation participant who is saying
 * the line.
 */
public class AvatarWithMessageActor extends TableActor {

    protected Label lineLabel;
    protected ResourceLocator resourceLocator;
    protected AppInfo appInfo;
    protected Image background;
    protected Button button;
    protected SingleConversationLine renderable;
    protected Sprite avatarSprite;
    protected Image avatarImg;

    public AvatarWithMessageActor(Skin skin, SingleConversationLine renderable) {
        super(skin);
        this.renderable = renderable;
        appInfo = AppInfo.getInstance();
        resourceLocator = new ResourceLocator();
    }

    public void init() {
        //setFillParent(true);
        float screenHeight = appInfo.getScreenHeight();
        float screenWidth = appInfo.getScreenWidth();
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
        Texture avatar = imgUrlToTexture(renderable.getRelativeAvatarPath());
        Image avatarImage = new Image(new TextureRegionDrawable(new TextureRegion(avatar)));
        table.add(avatarImage).left().width(convertWidth(avatar.getWidth())).height(convertHeight(avatar.getHeight()));

        table.add().width(0.05f *width).height(height * 0.9f);

        //Text
        lineLabel = new Label(renderable.getConversationLine().getText(), getSkin());
        lineLabel.setWrap(true);
        //lineLabel.setWidth(screenWidth * 0.6f);
        table.add(lineLabel).center().width(0.60f * width);
        //button
        table.add(button).right().width(0.2f * width).height(0.4f * height);

        table.add().width(0.02f * width);

        //table.debug();
        stack.add(table);

        add(stack).height(height).width(width).center();
    }

    public void setButton(Button button) {
        this.button = button;
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
