package org.scify.engine.conversation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import org.scify.engine.Renderable;
import org.scify.moonwalker.app.helpers.GameInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;

/**
 * This class describes the conversation component that is drawn
 * at the bottom of the screen.
 * In order for the Rendering Engine to draw the conversation line component,
 * It needs the {@link Renderable} that is supposed to be "saying" the line,
 * the {@link ConversationLine} instance that gets drawn,
 * and an image path that represents the {@link Renderable} who is saying
 * the line.
 */
public class ConversationLineComponent extends Actor{

    protected ConversationLine conversationLine;
    protected Renderable renderable;
    protected String relativeAvatarPath;
    protected Table table;
    protected Label lineLabel;
    protected Image avatarImg;
    protected Sprite avatarSprite;
    protected ResourceLocator resourceLocator;
    protected GameInfo gameInfo;

    public ConversationLineComponent(ConversationLine conversationLine, Renderable renderable, String relativeAvatarPath) {
        gameInfo = GameInfo.getInstance();
        this.conversationLine = conversationLine;
        this.renderable = renderable;
        this.relativeAvatarPath = relativeAvatarPath;
        resourceLocator = new ResourceLocator();
    }

    public void initActor(Skin skin) {
        table = new Table(skin);
        table.setFillParent(true);
        table.setWidth(gameInfo.getScreenWidth());
        table.setHeight(200);
        table.bottom().left();
        lineLabel = new Label(conversationLine.getText(), skin);
        lineLabel.setWrap(true);
        avatarSprite = new Sprite(new Texture(resourceLocator.getFilePath(relativeAvatarPath)));
        avatarSprite.setSize((float) (gameInfo.getScreenWidth() * 0.15), (float) (gameInfo.getScreenWidth() * 0.1));
        avatarImg = new Image(new SpriteDrawable(avatarSprite));
        table.setFillParent(true);
        table.bottom().left();
        table.add(avatarImg).padLeft(5).padBottom(10);
        table.add(lineLabel).padLeft(10).align(Align.right);
        table.row();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        table.setDebug(true);
        table.draw(batch, parentAlpha);
        super.draw(batch, parentAlpha);
    }
}
