package org.scify.engine.conversation;

import org.scify.engine.Renderable;
import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.ui.actors.ActionButton;

public class SingleConversationLine extends Renderable {

    protected ConversationLine conversationLine;
    protected String relativeAvatarPath;
    protected Renderable renderableReferenced;
    protected ActionButton buttonNext;

    public SingleConversationLine(String id) {
        super("single_conversation_line", id);
        AppInfo appInfo = AppInfo.getInstance();
        xPos = 0;
        yPos = 0;
        width = appInfo.getScreenWidth();
        height = 300;
        buttonNext = new ActionButton("text_button", "button_next");
        buttonNext.setTitle("Επόμενο");
        buttonNext.setUserAction(new UserAction(UserActionCode.NEXT_CONVERSATION_LINE));
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

    public Renderable getRenderableReferenced() {
        return renderableReferenced;
    }

    public ActionButton getButtonNext() {
        return buttonNext;
    }
}
