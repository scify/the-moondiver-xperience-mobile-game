package org.scify.moonwalker.app.ui.actors.conversation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.renderables.NextConversationRenderable;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;
import org.scify.moonwalker.app.ui.actors.TableActor;
import org.scify.moonwalker.app.ui.actors.Updateable;

/**
 * This class describes the conversation component that is drawn
 * at the bottom of the screen.
 * In order for the Rendering Engine to draw the conversation line component,
 * It needs the {@link Renderable} that is supposed to be "saying" the line,
 * the {@link ConversationLine} instance that gets drawn,
 * and an image path that represents the conversation participant who is saying
 * the line.
 */
public class NextConversationActor extends TableActor implements Updateable {

    protected Label lineLabel;
    protected ResourceLocator resourceLocator;
    protected AppInfo appInfo;
    protected Image background;
    protected Button button;
    protected NextConversationRenderable renderable;
    protected Image avatarImage;
    protected Image avatarBG;

    @Override
    public void update(Renderable renderable) {
        if (this.renderable.getRenderableLastUpdated() > timestamp) {
            System.out.println("setting renderable: " + renderable.getRenderableLastUpdated() + " over: " + this.renderable.getRenderableLastUpdated());
            this.renderable = (NextConversationRenderable) renderable;
            this.timestamp = this.renderable.getRenderableLastUpdated();
            if (this.renderable.getButtonNextStatus()) {
                enableButton();
            } else {
                disableButton();
            }
        }
    }

    public NextConversationActor(Skin skin, NextConversationRenderable renderable) {
        super(skin, renderable);
        this.renderable = renderable;
        appInfo = AppInfo.getInstance();
        resourceLocator = new ResourceLocator();
    }

    protected void enableButton() {
        button.setVisible(true);
    }

    protected void disableButton() {
        button.setVisible(false);
    }

    public void init(boolean nextButtonVisibility) {
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


        table.add().width(0.02f * width).height(height * 0.9f);

        //Text
        lineLabel = new Label(renderable.getConversationLine().getText(), getSkin());
        lineLabel.setWrap(true);
        lineLabel.setWidth(width * 0.6f);
        table.add(lineLabel).center().width(lineLabel.getWidth());
        table.add().width(0.02f * width);
        //button
        table.add(button).right().width(0.2f * width);
        if (nextButtonVisibility)
            enableButton();
        else
            disableButton();
        table.add().width(0.02f * width);
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
