package org.scify.engine.renderables;

import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.EffectSequence;
import org.scify.engine.renderables.effects.FadeEffect;
import org.scify.engine.renderables.effects.VisibilityEffect;

import java.util.*;

public class SingleChoiceConversationRenderable extends ConversationRenderable {

    public static final String AVATAR_BG_IMG_PATH = "img/avatars/bg.png";
    public static final String SINGLE_CHOICE_BUTTON_ID = "single_choice_button";
    public static final String AVATAR_BG_ID = "avatar_bg";
    public static final String AVATAR_IMAGE_ID = "avatar_image";
    public static final String CONVERSATION_TEXT_ID = "conversation_text_label";

    protected ConversationLine conversationLine;
    protected TextLabelRenderable conversationText;
    protected ImageRenderable avatar_bg;
    protected ImageRenderable avatar;
    protected ActionButtonRenderable conversationButton;
    protected boolean buttonActive;
    protected int conversationId;

    protected Set<Renderable> allRenderables;

    public Set<Renderable> getAllRenderables() {
        return allRenderables;
    }

    public SingleChoiceConversationRenderable(int id, String bgImgPath, boolean keepFirstDuringParsing, Map<String, String> replaceLexicon) {
        super(CONVERSATION_SINGLE_CHOICE, CONVERSATION_SINGLE_CHOICE + id, bgImgPath, keepFirstDuringParsing, replaceLexicon);
        conversationId = id;
        float screenWidth = appInfo.getScreenWidth();
        float screenHeight = appInfo.getScreenHeight();
        this.xPos = 0.02f * screenWidth;
        this.yPos = 0.03f * screenHeight;
        width = 0.96f * screenWidth;
        height = 0.3f * screenHeight;
        buttonActive = true;
        avatar_bg = createImageRenderable(AVATAR_BG_ID, AVATAR_BG_IMG_PATH, false, true, 102);
        initSubRenderables();
    }

    private void initSubRenderables() {
        allRenderables = new HashSet<>();
        allRenderables.add(avatar_bg);
    }

    public void setAvatarImg(String imgPath) {
        avatar = createImageRenderable(AVATAR_IMAGE_ID + imgPath, imgPath,false, true, 103);
        markAsNeedsUpdate();
        allRenderables.add(avatar);
    }

    @Override
    public void setConversationLines(List<ConversationLine> conversationLines) {
        this.conversationLines = conversationLines;
        conversationLine = conversationLines.get(0);

        String buttonText = conversationLine.getButtonText();
        if (buttonText != null) {
            conversationButton = createTextButton(SINGLE_CHOICE_BUTTON_ID + id, buttonText,
                    new UserAction(UserActionCode.SINGLE_CHOICE_CONVERSATION_LINE, new Map.Entry<Renderable, ConversationLine>() {

                        @Override
                        public Renderable getKey() {
                            return conversationButton;
                        }

                        @Override
                        public ConversationLine getValue() {
                            return conversationLine;
                        }

                        @Override
                        public ConversationLine setValue(ConversationLine value) {
                            return null;
                        }
                    }), false, false, 102);
        }else {
            conversationButton = createTextButton(SINGLE_CHOICE_BUTTON_ID + id, "Επόμενο",
                    new UserAction(UserActionCode.SINGLE_CHOICE_CONVERSATION_LINE, new Map.Entry<Renderable, ConversationLine>() {

                        @Override
                        public Renderable getKey() {
                            return conversationButton;
                        }

                        @Override
                        public ConversationLine getValue() {
                            return conversationLine;
                        }

                        @Override
                        public ConversationLine setValue(ConversationLine value) {
                            return null;
                        }
                    }), false, false, 102);
        }
        conversationText = createTextLabelRenderable(CONVERSATION_TEXT_ID + conversationId, parseText(conversationLine.getText()),false, true, 102);
        allRenderables.add(conversationText);
        conversationButton.setOneClickAllowed(true);
        allRenderables.add(conversationButton);
    }

    public void showButton() {
        EffectSequence es = new EffectSequence();
        es.addEffect(new FadeEffect(1,0,0));
        es.addEffect(new VisibilityEffect(true));
        es.addEffect(new FadeEffect(0,1,300));
        conversationButton.addEffect(es);
    }

    public void setConversationLine(ConversationLine conversationLine) {
        List<ConversationLine> listOfConversationLines = new ArrayList<>();
        listOfConversationLines.add(conversationLine);
        setConversationLines(listOfConversationLines);
    }

    public ImageRenderable getTableBGRenderable() {
        return tableBGRenderable;
    }

    public void enableButton() {
        if (buttonActive == false) {
            buttonActive = true;
            conversationButton.setVisible(true);
        }
    }

    public void disableButton() {
        if (buttonActive) {
            buttonActive = false;
            conversationButton.setVisible(false);
        }
    }

    public boolean getConversationButtonStatus() {
        return buttonActive;
    }

    public ConversationLine getConversationLine() {
        return conversationLine;
    }

    public ActionButtonRenderable getConversationButton() {
        return conversationButton;
    }

    public ImageRenderable getAvatar_bg() {
        return avatar_bg;
    }

    public ImageRenderable getAvatar() {
        return avatar;
    }

    public TextLabelRenderable getConversationText() {
        return conversationText;
    }
}
