package org.scify.engine.renderables;

import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.moonwalker.app.ui.renderables.TableRenderable;

import java.util.ArrayList;
import java.util.List;

public class MultipleChoiceConversationRenderable extends TableRenderable{

    protected String relativeAvatarImgPath;
    protected String title;
    protected List<ConversationLine> conversationLines;
    protected List<ActionButtonWithEffect> buttons;


    public MultipleChoiceConversationRenderable(String id) {
        super(0,0,0,0, "multiple_choice_conversation", id, "");
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
        ActionButtonWithEffect button = new ActionButtonWithEffect("text_button", "multiple_selection_answer");
        button.setTitle(line.getText());
        button.setUserAction(new UserAction(UserActionCode.MULTIPLE_SELECTION_ANSWER, line.getId()));
        buttons.add(button);
    }

    public List<ActionButtonWithEffect> getButtons() {
        return buttons;
    }
}
