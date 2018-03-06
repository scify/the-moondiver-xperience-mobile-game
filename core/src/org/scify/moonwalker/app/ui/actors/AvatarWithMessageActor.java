package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import org.scify.engine.Renderable;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.conversation.SingleConversationLine;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;

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
        initTable();
    }

    protected void initTable() {
        setFillParent(true);
        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        bottom().left();
        background = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(resourceLocator.getFilePath("img/component_background.png")))));
        background.setSize(appInfo.getScreenWidth(), appInfo.getScreenHeight() / 4f);
        addActor(background);
        lineLabel = new Label(renderable.getConversationLine().getText(), getSkin());
        lineLabel.setWrap(true);
        lineLabel.setWidth(appInfo.getScreenWidth() * 0.5f);
        avatarSprite = new Sprite(new Texture(resourceLocator.getFilePath("img/avatars/" + renderable.getRelativeAvatarPath())));
        avatarSprite.setSize((float) (appInfo.getScreenWidth() * 0.15), (float) (appInfo.getScreenWidth() * 0.1));
        avatarImg = new Image(new SpriteDrawable(avatarSprite));
        add(avatarImg).width(appInfo.getScreenWidth() * 0.2f).padLeft(5).padBottom(10);
        add(lineLabel).width(lineLabel.getWidth()).padLeft(10).align(Align.right);
        debug();
    }

    public void setButton(Button button) {
        this.button = button;
        add(this.button).width(appInfo.getScreenWidth() * 0.2f).padLeft(10).align(Align.left);
    }
}
