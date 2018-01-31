package org.scify.moonwalker.app.ui.components;

import org.scify.engine.conversation.ConversationLine;
import java.util.List;

public class MultipleConversationLinesComponent {

    String avatarImgPath;
    String title;
    List<ConversationLine> conversationLines;

    public MultipleConversationLinesComponent(String title, List<ConversationLine> conversationLines) {
        this.conversationLines = conversationLines;
        this.title = title;
    }

    public String getAvatarImgPath() {
        return avatarImgPath;
    }

    public void setAvatarImgPath(String avatarImgPath) {
        this.avatarImgPath = avatarImgPath;
    }

    public List<ConversationLine> getConversationLines() {
        return conversationLines;
    }

    public String getTitle() {
        return title;
    }
}
