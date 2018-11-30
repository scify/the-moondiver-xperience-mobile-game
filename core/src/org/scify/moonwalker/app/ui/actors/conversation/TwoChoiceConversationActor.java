package org.scify.moonwalker.app.ui.actors.conversation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.SnapshotArray;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.SingleChoiceConversationRenderable;
import org.scify.engine.renderables.TwoChoiceConversationRenderable;
import org.scify.moonwalker.app.ui.actors.TableActor;
import org.scify.moonwalker.app.ui.actors.Updateable;

import java.util.concurrent.Semaphore;

/**
 * This class describes the conversation component that is drawn
 * at the bottom of the screen.
 * In order for the Rendering Engine to draw the conversation line component,
 * It needs the {@link Renderable} that is supposed to be "saying" the line,
 * the {@link ConversationLine} instance that gets drawn,
 * and an image path that represents the conversation participant who is saying
 * the line.
 */
public class TwoChoiceConversationActor extends TableActor<TwoChoiceConversationRenderable> implements Updateable<TwoChoiceConversationRenderable> {

    protected Button buttonTop;
    protected Button buttonBottom;
    protected Image avatarImage;
    protected Image avatarBG;
    protected Stack avatarStack;
    protected Semaphore sInitOngoing;


    public TwoChoiceConversationActor(Skin skin, TwoChoiceConversationRenderable renderable) {
        super(skin, renderable);
        this.renderable = renderable;
        sInitOngoing = new Semaphore(1);
        init();
    }

    protected void init() {
        try {
            sInitOngoing.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot initialize conversation.");
        }

        float width = renderable.getWidth();
        float height = renderable.getHeight();
        defaults();
        center();
        setWidth(width);
        setHeight(height);
        addBackground(renderable.getTableBGRenderable(), width, height);

        add().height(height).width(0.01f * width);

        //avatar
        avatarStack = new Stack();
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

        //Buttons
        Table buttonsTable = new Table();
        buttonsTable.defaults();
        buttonsTable.center();
        float buttonWidth = 0.85f * width;
        float buttonHeight = 0.3f * height;
        buttonTop = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getConversationButtonTop());
        buttonTop.setStyle(getSkin().get("default", TextButton.TextButtonStyle.class));
        buttonsTable.add(buttonTop).width(buttonWidth).height(buttonHeight);
        buttonsTable.row();
        buttonsTable.add().height(buttonHeight/2);
        buttonsTable.row();
        buttonBottom = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getConversationButtonBottom());
        buttonBottom.setStyle(getSkin().get("default", TextButton.TextButtonStyle.class));
        buttonsTable.add(buttonBottom).width(buttonWidth).height(buttonHeight);
        add(buttonsTable).width(buttonWidth);

        add().height(height).width(0.01f * width);

        sInitOngoing.release();
    }

    @Override
    public void update(TwoChoiceConversationRenderable renderable) {
        // If init ongoing
        if (sInitOngoing.availablePermits() < 1) {
            return; // do nothing
        }

        float width = renderable.getWidth();
        float height = renderable.getHeight();

        //CHARACTER IMAGE
        Image newAvatarImage = (Image) bookKeeper.getUIRepresentationOfRenderable(renderable.getAvatar());
        SnapshotArray<Actor> stackChildren = avatarStack.getChildren();
        stackChildren.begin();
        if ((newAvatarImage != avatarImage) // If image changed
                || ((stackChildren.size > 0) && (stackChildren.items[stackChildren.size - 1] != newAvatarImage))) { // or not on top of non-empty stack
            avatarStack.removeActor(avatarImage); // Remove previous, if different
            // Create and add new
            avatarImage = newAvatarImage;

            avatarImage.setWidth(0.12f * width);
            avatarImage.setScaling(Scaling.fillX);
            avatarStack.add(avatarImage);

            // DEBUG LINES
//            Gdx.app.log("DEBUG_UI", renderable.toString() + " updating...");
            //////////////
        }
        stackChildren.end();

        renderable.wasUpdated();    }
}
