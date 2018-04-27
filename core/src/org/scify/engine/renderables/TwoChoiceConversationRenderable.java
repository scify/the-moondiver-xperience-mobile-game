package org.scify.engine.renderables;

import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.engine.conversation.ConversationLine;
import org.scify.moonwalker.app.ui.renderables.TableRenderable;

import java.util.ArrayList;
import java.util.List;

public class TwoChoiceConversationRenderable extends TableRenderable {

    public final static String BG_IMG_PATH = "img/conversations/bg.png";
    protected String relativeAvatarImgPath;
    protected List<ConversationLine> conversationLines;
    protected List<ActionButtonWithEffect> buttons;

    public TwoChoiceConversationRenderable(String id) {
        super(0,0,0,0,"two_choice_conversation", id, BG_IMG_PATH);
        float screenWidth = appInfo.getScreenWidth();
        float screenHeight = appInfo.getScreenHeight();
        this.xPos = 0.02f * screenWidth;
        this.yPos = 0.03f *  screenHeight;
        width = 0.96f * screenWidth;
        height = 0.3f * screenHeight;
        buttons = new ArrayList<>();
        tableBGRenderable = new ImageRenderable("chat_bg", BG_IMG_PATH);
    }

    public ImageRenderable getTableBGRenderable() {
        return tableBGRenderable;
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
        ActionButtonWithEffect button = new ActionButtonWithEffect("text_button", "two_selection_answer");
        button.setTitle(line.getText());
        button.setUserAction(new UserAction(UserActionCode.MULTIPLE_SELECTION_ANSWER, line.getId()));
        buttons.add(button);
    }

    public List<ActionButtonWithEffect> getButtons() {
        return buttons;
    }
}
