package org.scify.engine.renderables;

import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.engine.conversation.ConversationLine;
import org.scify.moonwalker.app.ui.renderables.FadingTableRenderable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TwoChoiceConversationRenderable extends FadingTableRenderable {

    public final static String BG_IMG_PATH = "img/conversations/bg.png";
    public final static String AVATAR_BG_IMG_PATH = "img/avatars/bg.png";
    protected ImageRenderable avatar_bg;
    protected ImageRenderable avatar;
    protected List<ConversationLine> conversationLines;

    protected ActionButtonRenderable  conversationButtonTop;
    protected ActionButtonRenderable  conversationButtonBottom;

    protected Set<Renderable> allRenderables;

    public Set<Renderable> getAllRenderables() {
        return allRenderables;
    }

    public TwoChoiceConversationRenderable(String id) {
        super(0,0,0,0,CONVERSATION_TWO_CHOICE, CONVERSATION_TWO_CHOICE + id, BG_IMG_PATH);
        float screenWidth = appInfo.getScreenWidth();
        float screenHeight = appInfo.getScreenHeight();
        this.xPos = 0.02f * screenWidth;
        this.yPos = 0.03f *  screenHeight;
        width = 0.96f * screenWidth;
        height = 0.3f * screenHeight;
        tableBGRenderable = new ImageRenderable("chat_bg", BG_IMG_PATH);
        conversationButtonTop = new ActionButtonRenderable(Renderable.ACTOR_TEXT_BUTTON,"button_next_top");
        conversationButtonTop.setPositionDrawable(false);
        conversationButtonTop.setZIndex(102);
        conversationButtonBottom = new ActionButtonRenderable(Renderable.ACTOR_TEXT_BUTTON, "button_next_bottom");
        conversationButtonBottom.setPositionDrawable(false);
        conversationButtonBottom.setZIndex(102);
        avatar_bg = new ImageRenderable("avata_bg", AVATAR_BG_IMG_PATH);
        avatar_bg.setPositionDrawable(false);
        avatar_bg.setZIndex(2);
        initSubRenderables();
    }

    private void initSubRenderables() {
        allRenderables = new HashSet<>();
        allRenderables.add(conversationButtonTop);
        allRenderables.add(conversationButtonBottom);
        allRenderables.add(avatar_bg);
    }

    public ImageRenderable getTableBGRenderable() {
        return tableBGRenderable;
    }

    public void setConversationLines(List<ConversationLine> conversationLines) {
        this.conversationLines = conversationLines;
        ConversationLine conversationLine = conversationLines.get(0);
        conversationButtonTop.setUserAction(new UserAction(UserActionCode.MULTIPLE_SELECTION_ANSWER, conversationLine.getId()));
        conversationButtonTop.setTitle(conversationLine.getText());
        conversationLine = conversationLines.get(1);
        conversationButtonBottom.setUserAction(new UserAction(UserActionCode.MULTIPLE_SELECTION_ANSWER, conversationLine.getId()));
        conversationButtonBottom.setTitle(conversationLine.getText());
    }

    public void setAvatarImg (String imgPath) {
        avatar = new ImageRenderable("avatar_img", imgPath);
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
