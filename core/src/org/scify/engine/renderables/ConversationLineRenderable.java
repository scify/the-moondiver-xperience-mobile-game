package org.scify.engine.renderables;

import org.scify.engine.conversation.ConversationLine;
import org.scify.moonwalker.app.ui.renderables.FadingTableRenderable;

import java.util.List;
import java.util.Set;

public abstract class ConversationLineRenderable extends FadingTableRenderable {
    protected Set<Renderable> allRenderables;
    public static final String BG_IMG_PATH = "img/conversations/bg.png";

    public ConversationLineRenderable(String rendereableCode, String id) {
        super(0,0,0,0,rendereableCode, id, BG_IMG_PATH);
    }

    public abstract void setConversationLines(List<ConversationLine> conversationLines);

    public Set<Renderable> getAllRenderables() {
        return allRenderables;
    }
}
