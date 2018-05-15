package org.scify.engine.renderables;

import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.engine.conversation.ConversationLine;
import org.scify.moonwalker.app.ui.renderables.FadingTableRenderable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultipleChoiceConversationRenderable extends FadingTableRenderable {

    public static final String BG_IMG_PATH = "img/conversations/bg.png";

    public static final String TOP_LEFT_BUTTON_ID = "button_top_left";
    public static final String BOTTOM_LEFT_BUTTON_ID = "button_bottom_left";
    public static final String TOP_RIGHT_BUTTON_ID = "button_top_right";
    public static final String BOTTOM_RIGHT_BUTTON_ID = "button_bottom_right";

    protected String title;
    protected List<ConversationLine> conversationLines;
    protected int conversationId;
    protected ActionButtonRenderable  conversationButtonTopLeft;
    protected ActionButtonRenderable  conversationButtonBottomLeft;
    protected ActionButtonRenderable  conversationButtonTopRight;
    protected ActionButtonRenderable  conversationButtonBottomRight;

    protected Set<Renderable> allRenderables;

    public Set<Renderable> getAllRenderables() {
        return allRenderables;
    }

    public MultipleChoiceConversationRenderable(int id) {
        super(0,0,0,0,CONVERSATION_MULTIPLE_CHOICE, CONVERSATION_MULTIPLE_CHOICE + id, BG_IMG_PATH);
        conversationId = id;
        float screenWidth = appInfo.getScreenWidth();
        float screenHeight = appInfo.getScreenHeight();
        this.xPos = 0.02f * screenWidth;
        this.yPos = 0.03f *  screenHeight;
        width = 0.96f * screenWidth;
        height = 0.3f * screenHeight;

        initSubRenderables();
    }

    private void initSubRenderables() {
        allRenderables = new HashSet<>();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setConversationLines(List<ConversationLine> conversationLines) {
        this.conversationLines = conversationLines;
        ConversationLine conversationLine = conversationLines.get(0);
        conversationButtonTopLeft = createTextButton(TOP_LEFT_BUTTON_ID+ conversationLine.getId(),conversationLine.getText(),new UserAction(UserActionCode.MULTIPLE_SELECTION_ANSWER, conversationLine.getId()),false, true,102);
        allRenderables.add(conversationButtonTopLeft);
        conversationLine = conversationLines.get(1);
        conversationButtonBottomLeft = createTextButton(BOTTOM_LEFT_BUTTON_ID+ conversationLine.getId(),conversationLine.getText(),new UserAction(UserActionCode.MULTIPLE_SELECTION_ANSWER, conversationLine.getId()),false, true,102);
        allRenderables.add(conversationButtonBottomLeft);
        conversationLine = conversationLines.get(2);
        conversationButtonTopRight = createTextButton(TOP_RIGHT_BUTTON_ID+ conversationLine.getId(),conversationLine.getText(),new UserAction(UserActionCode.MULTIPLE_SELECTION_ANSWER, conversationLine.getId()),false, true,102);
        allRenderables.add(conversationButtonTopRight);
        conversationLine = conversationLines.get(3);
        conversationButtonBottomRight = createTextButton(BOTTOM_RIGHT_BUTTON_ID+ conversationLine.getId(),conversationLine.getText(),new UserAction(UserActionCode.MULTIPLE_SELECTION_ANSWER, conversationLine.getId()),false, true,102);
        allRenderables.add(conversationButtonBottomRight);
    }


    public List<ConversationLine> getConversationLines() {
        return conversationLines;
    }

    public String getTitle() {
        return title;
    }

    public ActionButtonRenderable getConversationButtonTopLeft() {
        return conversationButtonTopLeft;
    }

    public ActionButtonRenderable getConversationButtonBottomLeft() {
        return conversationButtonBottomLeft;
    }

    public ActionButtonRenderable getConversationButtonTopRight() {
        return conversationButtonTopRight;
    }

    public ActionButtonRenderable getConversationButtonBottomRight() {
        return conversationButtonBottomRight;
    }
}
