package org.scify.engine.conversation;

import org.scify.engine.conversation.ConversationLine;
import java.util.List;

public class MultipleConversationLines {

    String avatarImgPath;
    String title;
    List<ConversationLine> conversationLines;

    public MultipleConversationLines(String title, List<ConversationLine> conversationLines) {
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
