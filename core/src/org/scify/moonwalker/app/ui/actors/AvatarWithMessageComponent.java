package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import org.scify.engine.Renderable;
import org.scify.engine.conversation.ConversationLine;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;
import org.scify.moonwalker.app.ui.input.UserInputHandlerImpl;

/**
 * This class describes the conversation component that is drawn
 * at the bottom of the screen.
 * In order for the Rendering Engine to draw the conversation line component,
 * It needs the {@link Renderable} that is supposed to be "saying" the line,
 * the {@link ConversationLine} instance that gets drawn,
 * and an image path that represents the {@link Renderable} who is saying
 * the line.
 */
public class AvatarWithMessageComponent extends Group {

    protected String message;
    protected Renderable renderable;
    protected String relativeAvatarPath;
    protected Label lineLabel;
    protected Image avatarImg;
    protected Sprite avatarSprite;
    protected ResourceLocator resourceLocator;
    protected AppInfo appInfo;
    protected boolean hasNextButton;
    protected TextButton nextButton;
    protected Table table;
    protected Image background;

    public AvatarWithMessageComponent(String message, Renderable renderable, String relativeAvatarPath, boolean hasNextButton) {
        appInfo = AppInfo.getInstance();
        this.message = message;
        this.renderable = renderable;
        this.relativeAvatarPath = relativeAvatarPath;
        resourceLocator = new ResourceLocator();
        this.hasNextButton = hasNextButton;
        table = new Table();
    }

    public void initActor(Skin skin, UserInputHandlerImpl userInputHandler) {
        table.setSkin(skin);
        table.setFillParent(true);
        setWidth(appInfo.getScreenWidth());
        setHeight(200);
        table.bottom().left();
        lineLabel = new Label(message, skin);
        lineLabel.setWrap(true);
        lineLabel.setWidth(appInfo.getScreenWidth() * 0.5f);
        avatarSprite = new Sprite(new Texture(resourceLocator.getFilePath(relativeAvatarPath)));
        avatarSprite.setSize((float) (appInfo.getScreenWidth() * 0.15), (float) (appInfo.getScreenWidth() * 0.1));
        avatarImg = new Image(new SpriteDrawable(avatarSprite));
        background = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(resourceLocator.getFilePath("img/component_background.png")))));
        background.setSize(appInfo.getScreenWidth(), appInfo.getScreenHeight() / 4f);
        table.add(avatarImg).width(appInfo.getScreenWidth() * 0.2f).padLeft(5).padBottom(10);
        table.add(lineLabel).width(lineLabel.getWidth()).padLeft(10).align(Align.right);
        if(hasNextButton) {
            nextButton = new TextButton("Επόμενο", skin);
            nextButton.pad(5);
            table.add(nextButton).width(appInfo.getScreenWidth() * 0.2f).padLeft(10).align(Align.left);
            nextButton.addListener(userInputHandler);
        }
        //debug();
        addActor(background);
        addActor(table);
    }
}
