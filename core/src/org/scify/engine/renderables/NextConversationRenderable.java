package org.scify.engine.renderables;

import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.moonwalker.app.ui.actors.ActionButton;

public class NextConversationRenderable extends Renderable {

    protected ImageRenderable tableBGRenderable;
    public final String BG_IMG_PATH = "img/conversations/bg.png";

    protected ConversationLine conversationLine;
    protected String relativeAvatarPath;
    protected ActionButton buttonNext;
    protected boolean buttonNextActive;

    public NextConversationRenderable(String id) {
        super("next_conversation", id);
        xPos = 0;
        yPos = 0;
        width = appInfo.getScreenWidth();
        height = 300;
        buttonNext = new ActionButton("text_button", "button_next");
        buttonNext.setTitle("Επόμενο");
        buttonNext.setUserAction(new UserAction(UserActionCode.NEXT_CONVERSATION_LINE));
        buttonNextActive = true;
        tableBGRenderable = new ImageRenderable("chat_bg", BG_IMG_PATH);
    }

    public ImageRenderable getTableBGRenderable() {
        return tableBGRenderable;
    }

    public void setButtonNextActive() {
        if (buttonNextActive == false) {
            buttonNextActive = true;
            renderableWasUpdated();
        }
    }

    public void setButtonNextInActive() {
        if (buttonNextActive) {
            buttonNextActive = false;
            renderableWasUpdated();
        }
    }

    public boolean getButtonNextStatus() {
        return buttonNextActive;
    }

    public void setConversationLine(ConversationLine conversationLine) {
        this.conversationLine = conversationLine;
    }

    public void setRelativeAvatarPath(String relativeAvatarPath) {
        this.relativeAvatarPath = relativeAvatarPath;
    }

    public ConversationLine getConversationLine() {
        return conversationLine;
    }

    public String getRelativeAvatarPath() {
        return relativeAvatarPath;
    }

    public ActionButton getButtonNext() {
        return buttonNext;
    }
}
