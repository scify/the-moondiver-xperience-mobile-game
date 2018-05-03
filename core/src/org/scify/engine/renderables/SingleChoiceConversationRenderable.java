package org.scify.engine.renderables;

import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.engine.conversation.ConversationLine;
import org.scify.moonwalker.app.ui.renderables.FadingTableRenderable;

import java.util.HashSet;
import java.util.Set;

public class SingleChoiceConversationRenderable extends FadingTableRenderable {

    public final static String BG_IMG_PATH = "img/conversations/bg.png";
    public final static String AVATAR_BG_IMG_PATH = "img/avatars/bg.png";

    protected ConversationLine conversationLine;
    protected TextLabelRenderable conversationText;
    protected ImageRenderable avatar_bg;
    protected ImageRenderable avatar;
    protected ActionButtonRenderable conversationButton;
    protected boolean buttonActive;

    protected Set<Renderable> allRenderables;

    public Set<Renderable> getAllRenderables() {
        return allRenderables;
    }

    public SingleChoiceConversationRenderable(String id) {
        super(0,0,0,0,CONVERSATION_SINGLE_CHOICE, CONVERSATION_SINGLE_CHOICE+id, BG_IMG_PATH);
        float screenWidth = appInfo.getScreenWidth();
        float screenHeight = appInfo.getScreenHeight();
        this.xPos = 0.02f * screenWidth;
        this.yPos = 0.03f *  screenHeight;
        width = 0.96f * screenWidth;
        height = 0.3f * screenHeight;
        conversationButton = new ActionButtonRenderable(Renderable.ACTOR_TEXT_BUTTON, "button_next");
        conversationButton.setTitle("Επόμενο");
        conversationButton.setPositionDrawable(false);
        conversationButton.setZIndex(2);
        buttonActive = true;
        tableBGRenderable = new ImageRenderable("chat_bg", BG_IMG_PATH);
        avatar_bg = new ImageRenderable("avata_bg", AVATAR_BG_IMG_PATH);
        avatar_bg.setPositionDrawable(false);
        avatar_bg.setZIndex(2);
        initSubRenderables();
    }

    private void initSubRenderables() {
        allRenderables = new HashSet<>();
        allRenderables.add(conversationButton);
        allRenderables.add(avatar_bg);
    }

    public void setAvatarImg (String imgPath) {
        avatar = new ImageRenderable("avatar_img", imgPath);
        avatar.setZIndex(3);
        avatar.setPositionDrawable(false);
        allRenderables.add(avatar);
    }

    public void setConversationLine(ConversationLine conversationLine) {

        this.conversationLine = conversationLine;
        String buttonText = conversationLine.getButtonText();
        if (buttonText != null) {
            conversationButton.setTitle(buttonText);
        }
        conversationButton.setUserAction(new UserAction(UserActionCode.SINGLE_CHOICE_CONVERSATION_LINE, conversationLine.getId()));
        conversationText = new TextLabelRenderable(Renderable.ACTOR_LABEL, "conversation_text_label");
        conversationText.setZIndex(2);
        conversationText.setPositionDrawable(false);
        conversationText.setLabel(conversationLine.getText());
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

    public boolean getButtonNextStatus() {
        return buttonActive;
    }

    /*public ConversationLine getConversationLine() {
        return conversationLine;
    }*/

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
