package org.scify.engine.renderables;

import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.engine.conversation.ConversationLine;
import org.scify.moonwalker.app.ui.renderables.FadingTableRenderable;
import java.util.HashSet;
import java.util.Set;

public class SingleChoiceConversationRenderable extends TableRenderable {

    public static final String BG_IMG_PATH = "img/conversations/bg.png";
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

    public SingleChoiceConversationRenderable(ConversationLine line) {
        super(0, 0, 0, 0, CONVERSATION_SINGLE_CHOICE, CONVERSATION_SINGLE_CHOICE + line.getId(), BG_IMG_PATH);
        conversationId = line.getId();
        float screenWidth = appInfo.getScreenWidth();
        float screenHeight = appInfo.getScreenHeight();
        this.xPos = 0.02f * screenWidth;
        this.yPos = 0.03f * screenHeight;
        width = 0.96f * screenWidth;
        height = 0.3f * screenHeight;
        conversationButton = createTextButton(SINGLE_CHOICE_BUTTON_ID + line.getId(), "Επόμενο", new UserAction(UserActionCode.SINGLE_CHOICE_CONVERSATION_LINE, line), false, true, 102);
        buttonActive = true;
        avatar_bg = createImageRenderable(AVATAR_BG_ID + line.getId(), AVATAR_BG_IMG_PATH, false, true, 102);
        initSubRenderables();
    }

    private void initSubRenderables() {
        allRenderables = new HashSet<>();
        allRenderables.add(conversationButton);
        allRenderables.add(avatar_bg);
    }

    public void setAvatarImg(String imgPath) {
        avatar = createImageRenderable(AVATAR_IMAGE_ID + conversationId, imgPath,false, true, 103);
        allRenderables.add(avatar);
    }

    public void setConversationLine(ConversationLine conversationLine) {

        this.conversationLine = conversationLine;
        String buttonText = conversationLine.getButtonText();
        if (buttonText != null) {
            conversationButton.setTitle(buttonText);
        }
        conversationText = createTextLabelRenderable(CONVERSATION_TEXT_ID + conversationId, conversationLine.getText(),false, true, 102);
        allRenderables.add(conversationText);
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
