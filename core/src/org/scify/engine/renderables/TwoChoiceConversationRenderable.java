package org.scify.engine.renderables;

import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.engine.conversation.ConversationLine;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TwoChoiceConversationRenderable extends ConversationRenderable {

    public static final String AVATAR_BG_IMG_PATH = "img/avatars/bg.png";

    public static final String TOP_BUTTON_ID = "button_top";
    public static final String BOTTOM_BUTTON_ID = "button_bottom";
    public static final String AVATAR_BG_ID = "avatar_bg";
    public static final String AVATAR_IMAGE_ID = "avatar_image";

    protected ImageRenderable avatar_bg;
    protected ImageRenderable avatar;
    protected int conversationId;
    protected ActionButtonRenderable  conversationButtonTop;
    protected ActionButtonRenderable  conversationButtonBottom;

    protected Set<Renderable> allRenderables;

    public Set<Renderable> getAllRenderables() {
        return allRenderables;
    }

    public TwoChoiceConversationRenderable(int id, String bgImgPath, boolean keepFirstDuringParsing) {
        super(CONVERSATION_TWO_CHOICE, CONVERSATION_TWO_CHOICE + id, bgImgPath, keepFirstDuringParsing);
        conversationId = id;
        float screenWidth = appInfo.getScreenWidth();
        float screenHeight = appInfo.getScreenHeight();
        this.xPos = 0.02f * screenWidth;
        this.yPos = 0.03f *  screenHeight;
        width = 0.96f * screenWidth;
        height = 0.3f * screenHeight;

        avatar_bg = createImageRenderable(AVATAR_BG_ID + id, AVATAR_BG_IMG_PATH, false, true, 102);
        initSubRenderables();
    }

    private void initSubRenderables() {
        allRenderables = new HashSet<>();
        allRenderables.add(avatar_bg);
    }

    public ImageRenderable getTableBGRenderable() {
        return tableBGRenderable;
    }

    @Override
    public void setConversationLines(List<ConversationLine> conversationLines) {
        this.conversationLines = conversationLines;
        ConversationLine conversationLine = conversationLines.get(0);
        conversationButtonTop = createTextButton(TOP_BUTTON_ID+ conversationLine.getId(),conversationLine.getText(),new UserAction(UserActionCode.MULTIPLE_SELECTION_ANSWER, conversationLine),false, true,102);
        allRenderables.add(conversationButtonTop);
        conversationLine = conversationLines.get(1);
        conversationButtonBottom = createTextButton(BOTTOM_BUTTON_ID+ conversationLine.getId(),conversationLine.getText(),new UserAction(UserActionCode.MULTIPLE_SELECTION_ANSWER, conversationLine),false, true,102);
        allRenderables.add(conversationButtonBottom);
    }

    public void setAvatarImg (String imgPath) {
        avatar = new ImageRenderable(AVATAR_IMAGE_ID, imgPath);
        avatar.setZIndex(103);
        avatar.setPositionDrawable(false);
        allRenderables.add(avatar);
    }

    public ImageRenderable getAvatar_bg() {
        return avatar_bg;
    }

    public ImageRenderable getAvatar() {
        return avatar;
    }

    public ActionButtonRenderable getConversationButtonTop() {
        return conversationButtonTop;
    }

    public ActionButtonRenderable getConversationButtonBottom() {
        return conversationButtonBottom;
    }

}
