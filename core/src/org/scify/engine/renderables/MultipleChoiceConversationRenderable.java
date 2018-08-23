package org.scify.engine.renderables;

import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.engine.conversation.ConversationLine;
import org.scify.moonwalker.app.game.quiz.Answer;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MultipleChoiceConversationRenderable extends ConversationRenderable {

    public static final String TOP_LEFT_BUTTON_ID = "button_top_left";
    public static final String BOTTOM_LEFT_BUTTON_ID = "button_bottom_left";
    public static final String TOP_RIGHT_BUTTON_ID = "button_top_right";
    public static final String BOTTOM_RIGHT_BUTTON_ID = "button_bottom_right";
    public static final String CONVERSATION_QUESTION_ID = "conversation_question_label";

    protected TextLabelRenderable question;
    protected List<ConversationLine> conversationLines;
    protected int conversationId;
    protected ActionButtonRenderable conversationButtonTopLeft;
    protected ActionButtonRenderable conversationButtonBottomLeft;
    protected ActionButtonRenderable conversationButtonTopRight;
    protected ActionButtonRenderable conversationButtonBottomRight;

    public MultipleChoiceConversationRenderable(int id, String questionText, String bgImgPath, boolean keepFirstDuringParsing, Map<String, String> replaceLexicon) {
        super(CONVERSATION_MULTIPLE_CHOICE, CONVERSATION_MULTIPLE_CHOICE + id, bgImgPath, keepFirstDuringParsing, replaceLexicon);
        conversationId = id;
        float screenWidth = appInfo.getScreenWidth();
        float screenHeight = appInfo.getScreenHeight();
        this.xPos = 0.02f * screenWidth;
        this.yPos = 0.03f * screenHeight;
        width = 0.96f * screenWidth;
        height = 0.8f * screenHeight;
        setPositionDrawable(true);
        initSubRenderables();
        if (questionText != null) {
            question = createTextLabelRenderable(CONVERSATION_QUESTION_ID + conversationId, questionText, false, true, 102);
            allRenderables.add(question);
        }
    }

    public TextLabelRenderable getQuestion() {
        return question;
    }

    private void initSubRenderables() {
        allRenderables = new HashSet<>();
    }

    public void setConversationLines(List<ConversationLine> conversationLines) {
        this.conversationLines = conversationLines;
        ConversationLine conversationLine = conversationLines.get(0);
        conversationButtonTopLeft = createTextButton(TOP_LEFT_BUTTON_ID + conversationLine.getId(), conversationLine.getText(), new UserAction(UserActionCode.MULTIPLE_SELECTION_ANSWER, conversationLine), false, true, 102, ((Answer) conversationLine.getPayload()).isCorrect());
        allRenderables.add(conversationButtonTopLeft);
        conversationLine = conversationLines.get(1);
        conversationButtonBottomLeft = createTextButton(BOTTOM_LEFT_BUTTON_ID + conversationLine.getId(), conversationLine.getText(), new UserAction(UserActionCode.MULTIPLE_SELECTION_ANSWER, conversationLine), false, true, 102, ((Answer) conversationLine.getPayload()).isCorrect());
        allRenderables.add(conversationButtonBottomLeft);
        conversationLine = conversationLines.get(2);
        conversationButtonTopRight = createTextButton(TOP_RIGHT_BUTTON_ID + conversationLine.getId(), conversationLine.getText(), new UserAction(UserActionCode.MULTIPLE_SELECTION_ANSWER, conversationLine), false, true, 102, ((Answer) conversationLine.getPayload()).isCorrect());
        allRenderables.add(conversationButtonTopRight);
        conversationLine = conversationLines.get(3);
        conversationButtonBottomRight = createTextButton(BOTTOM_RIGHT_BUTTON_ID + conversationLine.getId(), conversationLine.getText(), new UserAction(UserActionCode.MULTIPLE_SELECTION_ANSWER, conversationLine), false, true, 102, ((Answer) conversationLine.getPayload()).isCorrect());
        allRenderables.add(conversationButtonBottomRight);
    }

    protected ActionButtonRenderable createTextButton(String id, String text, UserAction userAction, boolean positionDrawable, boolean visibility, int zIndex, boolean defaultButtonSkin) {
        ActionButtonRenderable ret = new ActionButtonRenderable(Renderable.ACTOR_TEXT_BUTTON, id);
        ret.setTitle(text);
        ret.setUserAction(userAction);
        ret.setDefaultButtonSkin(defaultButtonSkin);
        setRenderableAttributes(ret, positionDrawable, visibility, zIndex);
        return ret;
    }


    public List<ConversationLine> getConversationLines() {
        return conversationLines;
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
