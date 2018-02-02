package org.scify.engine.conversation;

import org.scify.engine.Renderable;

public class SingleConversationLine {

    protected ConversationLine conversationLine;
    protected String relativeAvatarPath;
    protected Renderable renderableReferenced;

    public SingleConversationLine(ConversationLine conversationLine, String relativeAvatarPath, Renderable renderable) {
        this.conversationLine = conversationLine;
        this.relativeAvatarPath = relativeAvatarPath;
        this.renderableReferenced = renderable;
    }

    public ConversationLine getConversationLine() {
        return conversationLine;
    }

    public String getRelativeAvatarPath() {
        return relativeAvatarPath;
    }

    public Renderable getRenderableReferenced() {
        return renderableReferenced;
    }
}
