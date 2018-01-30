package org.scify.engine.conversation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import org.scify.engine.Renderable;
import org.scify.engine.UserInputHandler;
import org.scify.moonwalker.app.helpers.GameInfo;
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
public class ConversationLineComponent extends Table {

    public ConversationLine conversationLine;
    public Renderable renderable;
    public String relativeAvatarPath;
    public Label lineLabel;
    public Image avatarImg;
    public Sprite avatarSprite;
    public ResourceLocator resourceLocator;
    public GameInfo gameInfo;
    public boolean hasNextButton;
    public TextButton nextButton;

    public ConversationLineComponent(ConversationLine conversationLine, Renderable renderable, String relativeAvatarPath, boolean hasNextButton) {
        gameInfo = GameInfo.getInstance();
        this.conversationLine = conversationLine;
        this.renderable = renderable;
        this.relativeAvatarPath = relativeAvatarPath;
        resourceLocator = new ResourceLocator();
        this.hasNextButton = hasNextButton;

    }

    public void initActor(Skin skin, UserInputHandlerImpl userInputHandler) {
        setSkin(skin);

        setFillParent(true);
        setWidth(gameInfo.getScreenWidth());
        setHeight(200);
        bottom().left();
        lineLabel = new Label(conversationLine.getText(), skin);
        lineLabel.setWrap(true);
        lineLabel.setWidth(gameInfo.getScreenWidth() * 0.5f);
        avatarSprite = new Sprite(new Texture(resourceLocator.getFilePath(relativeAvatarPath)));
        avatarSprite.setSize((float) (gameInfo.getScreenWidth() * 0.15), (float) (gameInfo.getScreenWidth() * 0.1));
        avatarImg = new Image(new SpriteDrawable(avatarSprite));

        add(avatarImg).width(gameInfo.getScreenWidth() * 0.2f).padLeft(5).padBottom(10);
        add(lineLabel).width(lineLabel.getWidth()).padLeft(10).align(Align.right);
        if(hasNextButton) {
            nextButton = new TextButton("Next", skin);
            add(nextButton).width(gameInfo.getScreenWidth() * 0.2f).padLeft(10).align(Align.left);
            nextButton.addListener(userInputHandler);
        }
        debug();
    }
}
