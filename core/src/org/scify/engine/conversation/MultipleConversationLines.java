package org.scify.engine.conversation;

import org.scify.engine.Renderable;
import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.moonwalker.app.ui.actors.ActionButton;

import java.util.ArrayList;
import java.util.List;

public class MultipleConversationLines extends Renderable{

    protected String relativeAvatarImgPath;
    protected String title;
    protected List<ConversationLine> conversationLines;
    protected List<ActionButton> buttons;


    public MultipleConversationLines(String id) {
        super("multiple_conversation_lines", id);
        xPos = 0;
        yPos = 0;
        width = appInfo.getScreenWidth();
        height = 400;
        buttons = new ArrayList<>();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setConversationLines(List<ConversationLine> conversationLines) {
        this.conversationLines = conversationLines;
        for(ConversationLine line : conversationLines)
            addPossibleAnswer(line);
    }

    public String getRelativeAvatarImgPath() {
        return relativeAvatarImgPath;
    }

    public void setRelativeAvatarImgPath(String relativeAvatarImgPath) {
        this.relativeAvatarImgPath = relativeAvatarImgPath;
    }

    public List<ConversationLine> getConversationLines() {
        return conversationLines;
    }

    public String getTitle() {
        return title;
    }

    protected void addPossibleAnswer(ConversationLine line) {
        ActionButton button = new ActionButton("text_button", "multiple_selection_answer");
        button.setTitle(line.text);
        button.setUserAction(new UserAction(UserActionCode.MULTIPLE_SELECTION_ANSWER, line.getId()));
        buttons.add(button);
    }

    public List<ActionButton> getButtons() {
        return buttons;
    }
}
