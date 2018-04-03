package org.scify.engine.renderables;

import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.engine.conversation.ConversationLine;
import org.scify.moonwalker.app.ui.actors.ActionButton;

import java.util.ArrayList;
import java.util.List;

public class TwoChoiceConversationRenderable extends Renderable{

    protected String relativeAvatarImgPath;
    protected List<ConversationLine> conversationLines;
    protected List<ActionButton> buttons;


    public TwoChoiceConversationRenderable(String id) {
        super("two_choice_conversation", id);
        xPos = 0;
        yPos = 0;
        width = appInfo.getScreenWidth();
        height = 300;
        buttons = new ArrayList<>();
    }

    public void setConversationLines(List<ConversationLine> conversationLines) {
        this.conversationLines = conversationLines;
        for(ConversationLine line : conversationLines)
            addPossibleAnswer(line);
    }

    public String getRelativeAvatarPath() {
        return relativeAvatarImgPath;
    }

    public void setRelativeAvatarImgPath(String relativeAvatarImgPath) {
        this.relativeAvatarImgPath = relativeAvatarImgPath;
    }

    public List<ConversationLine> getConversationLines() {
        return conversationLines;
    }

    protected void addPossibleAnswer(ConversationLine line) {
        ActionButton button = new ActionButton("text_button", "two_selection_answer");
        button.setTitle(line.getText());
        button.setUserAction(new UserAction(UserActionCode.MULTIPLE_SELECTION_ANSWER, line.getId()));
        buttons.add(button);
    }

    public List<ActionButton> getButtons() {
        return buttons;
    }
}
