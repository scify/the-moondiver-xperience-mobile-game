package org.scify.moonwalker.app.ui.actors.conversation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
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
public class NextConversationActor extends TableActor<NextConversationRenderable> implements Updateable {

    protected Label lineLabel;
    protected ResourceLocator resourceLocator;
    protected AppInfo appInfo;
    protected Button button;
    protected NextConversationRenderable renderable;
    protected Image avatarImage;
    protected Image avatarBG;

    public NextConversationActor(Skin skin, NextConversationRenderable renderable) {
        super(skin, renderable);
        this.renderable = renderable;
        appInfo = AppInfo.getInstance();
        resourceLocator = new ResourceLocator();
    }

    public void init(boolean nextButtonVisibility) {
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

        //Text
        lineLabel = new Label(renderable.getConversationLine().getText(), getSkin());
        lineLabel.setWrap(true);
        add(lineLabel).center().width(width * 0.64f);

        add().height(height).width(0.01f * width);
        //button
        add(button).right().width(0.2f * width);
        if (nextButtonVisibility)
            enableButton();
        else
            disableButton();

        add().height(height).width(0.01f * width);

        //debugAll();
    }

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

    protected void enableButton() {
        button.setVisible(true);
    }

    protected void disableButton() {
        button.setVisible(false);
    }

    public void setButton(Button button) {
        this.button = button;
    }
}
