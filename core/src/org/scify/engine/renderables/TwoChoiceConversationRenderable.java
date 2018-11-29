package org.scify.engine.renderables;

import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.renderables.effects.EffectSequence;
import org.scify.engine.renderables.effects.FadeEffect;
import org.scify.engine.renderables.effects.FunctionEffect;
import org.scify.engine.renderables.effects.VisibilityEffect;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

    public TwoChoiceConversationRenderable(int id, String bgImgPath, boolean keepFirstDuringParsing, Map<String, String> replaceLexicon) {
        super(CONVERSATION_TWO_CHOICE, CONVERSATION_TWO_CHOICE + id, bgImgPath, keepFirstDuringParsing, replaceLexicon);
        conversationId = id;
        float screenWidth = appInfo.getScreenWidth();
        float screenHeight = appInfo.getScreenHeight();
        this.xPos = 0.02f * screenWidth;
        this.yPos = 0.03f *  screenHeight;
        width = 0.96f * screenWidth;
        height = 0.3f * screenHeight;

        avatar_bg = createImageRenderable(AVATAR_BG_ID, AVATAR_BG_IMG_PATH, false, true, 102);
        initSubRenderables();
    }

    private synchronized void initSubRenderables() {
        allRenderables = new HashSet<>();
        allRenderables.add(avatar_bg);
    }

    public ImageRenderable getTableBGRenderable() {
        return tableBGRenderable;
    }

    @Override
    public synchronized void setConversationLines(List<ConversationLine> conversationLines) {
        this.conversationLines = conversationLines;
        final ConversationLine conversationLine1 = conversationLines.get(0);
        conversationButtonTop = createTextButton(TOP_BUTTON_ID+ conversationLine1.getId(),parseText(conversationLine1.getText()),
                new UserAction(UserActionCode.MULTIPLE_SELECTION_ANSWER, new Map.Entry<Renderable, ConversationLine>() {

                    @Override
                    public Renderable getKey() {
                        return conversationButtonTop;
                    }

                    @Override
                    public ConversationLine getValue() {
                        return conversationLine1;
                    }

                    @Override
                    public ConversationLine setValue(ConversationLine value) {
                        return null;
                    }
                }),false, false,102);
        conversationButtonTop.setOneClickAllowed(true);
        allRenderables.add(conversationButtonTop);
        final ConversationLine conversationLine2 = conversationLines.get(1);
        conversationButtonBottom = createTextButton(BOTTOM_BUTTON_ID+ conversationLine2.getId(),parseText(conversationLine2.getText()),
                new UserAction(UserActionCode.MULTIPLE_SELECTION_ANSWER, new Map.Entry<Renderable, ConversationLine>() {

                    @Override
                    public Renderable getKey() {
                        return conversationButtonBottom;
                    }

                    @Override
                    public ConversationLine getValue() {
                        return conversationLine2;
                    }

                    @Override
                    public ConversationLine setValue(ConversationLine value) {
                        return null;
                    }
                }),false, false,102);
        conversationButtonBottom.setOneClickAllowed(true);
        allRenderables.add(conversationButtonBottom);
    }

    public synchronized void showButtons() {
        EffectSequence es = new EffectSequence();
        // Make sure they cannot be clicked before appearing
        conversationButtonTop.setClickedOnce(true);
        conversationButtonBottom.setClickedOnce(true);

                es.addEffect(new FadeEffect(1,0,0));
        es.addEffect(new VisibilityEffect(true));
        es.addEffect(new FadeEffect(0,1,300));
        es.addEffect(new FunctionEffect(new Runnable() {
            @Override
            public void run() {
                // Allow clicking
                conversationButtonTop.setClickedOnce(false);
                conversationButtonBottom.setClickedOnce(false);

            }
        }));
        conversationButtonTop.addEffect(es);
        conversationButtonBottom.addEffect(es);
    }

    public synchronized void setAvatarImg (String imgPath) {
        avatar = createImageRenderable(AVATAR_IMAGE_ID + imgPath, imgPath,false, true, 103);
        allRenderables.add(avatar);
    }

    public synchronized ImageRenderable getAvatar_bg() {
        return avatar_bg;
    }

    public synchronized ImageRenderable getAvatar() {
        return avatar;
    }

    public synchronized ActionButtonRenderable getConversationButtonTop() {
        return conversationButtonTop;
    }

    public synchronized ActionButtonRenderable getConversationButtonBottom() {
        return conversationButtonBottom;
    }

}
