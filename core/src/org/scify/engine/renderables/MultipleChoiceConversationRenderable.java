package org.scify.engine.renderables;

import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.engine.conversation.ConversationLine;
import org.scify.moonwalker.app.ui.ThemeController;

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

    public synchronized TextLabelRenderable getQuestion() {
        return question;
    }

    private synchronized void initSubRenderables() {
        allRenderables = new HashSet<>();
    }

    public synchronized void setConversationLines(List<ConversationLine> conversationLines) {
        this.conversationLines = conversationLines;
        final ConversationLine conversationLine0 = conversationLines.get(0);
        conversationButtonTopLeft = createTextButton(TOP_LEFT_BUTTON_ID + conversationLine0.getId(),
                conversationLine0.getText(), new UserAction(UserActionCode.MULTIPLE_SELECTION_ANSWER,
                        new Map.Entry<Renderable, ConversationLine>() {

                            @Override
                            public Renderable getKey() {
                                return conversationButtonTopLeft;
                            }

                            @Override
                            public ConversationLine getValue() {
                                return conversationLine0;
                            }

                            @Override
                            public ConversationLine setValue(ConversationLine value) {
                                return null;
                            }
                        }),
                false, true, 102, ThemeController.SKIN_DEFAULT);
        conversationButtonTopLeft.setOneClickAllowed(true);
        allRenderables.add(conversationButtonTopLeft);

        final ConversationLine conversationLine1 = conversationLines.get(1);
        conversationButtonBottomLeft = createTextButton(BOTTOM_LEFT_BUTTON_ID + conversationLine1.getId(),
                conversationLine1.getText(), new UserAction(UserActionCode.MULTIPLE_SELECTION_ANSWER,
                        new Map.Entry<Renderable, ConversationLine>() {

                            @Override
                            public Renderable getKey() {
                                return conversationButtonBottomLeft;
                            }

                            @Override
                            public ConversationLine getValue() {
                                return conversationLine1;
                            }

                            @Override
                            public ConversationLine setValue(ConversationLine value) {
                                return null;
                            }
                        }), false, true, 102, ThemeController.SKIN_DEFAULT);
        conversationButtonBottomLeft.setOneClickAllowed(true);
        allRenderables.add(conversationButtonBottomLeft);

        final ConversationLine conversationLine2 = conversationLines.get(2);
        conversationButtonTopRight = createTextButton(TOP_RIGHT_BUTTON_ID + conversationLine2.getId(),
                conversationLine2.getText(), new UserAction(UserActionCode.MULTIPLE_SELECTION_ANSWER, new Map.Entry<Renderable, ConversationLine>() {

                    @Override
                    public Renderable getKey() {
                        return conversationButtonTopRight;
                    }

                    @Override
                    public ConversationLine getValue() {
                        return conversationLine2;
                    }

                    @Override
                    public ConversationLine setValue(ConversationLine value) {
                        return null;
                    }
                }), false, true, 102, ThemeController.SKIN_DEFAULT);
        conversationButtonTopRight.setOneClickAllowed(true);
        allRenderables.add(conversationButtonTopRight);

        final ConversationLine conversationLine3 = conversationLines.get(3);
        conversationButtonBottomRight = createTextButton(BOTTOM_RIGHT_BUTTON_ID + conversationLine3.getId(),
                conversationLine3.getText(), new UserAction(UserActionCode.MULTIPLE_SELECTION_ANSWER,
                        new Map.Entry<Renderable, ConversationLine>() {

                            @Override
                            public Renderable getKey() {
                                return conversationButtonBottomRight;
                            }

                            @Override
                            public ConversationLine getValue() {
                                return conversationLine3;
                            }

                            @Override
                            public ConversationLine setValue(ConversationLine value) {
                                return null;
                            }
                        }), false, true, 102, ThemeController.SKIN_DEFAULT);
        conversationButtonBottomRight.setOneClickAllowed(true);
        allRenderables.add(conversationButtonBottomRight);
    }

    protected ActionButtonRenderable createTextButton(String id, String text, UserAction userAction, boolean positionDrawable, boolean visibility, int zIndex, String buttonSkin) {
        ActionButtonRenderable ret = new ActionButtonRenderable(Renderable.ACTOR_TEXT_BUTTON, id);
        ret.setTitle(text);
        ret.setUserAction(userAction);
        ret.setButtonSkin(buttonSkin);
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
