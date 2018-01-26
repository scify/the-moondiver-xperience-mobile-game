package org.scify.moonwalker.app.game.conversation;

import java.util.ArrayList;
import java.util.List;

public class Conversation {
    protected List<ConversationState> conversationStateList;
    protected int currentConversationIndex = -1;
    public Conversation() {
        conversationStateList = new ArrayList<>();
    }

    public void addConversationState(ConversationState state) {
        conversationStateList.add(state);
        if(currentConversationIndex == -1)
            currentConversationIndex = 0;
    }

    public ConversationState getNextState() {
        // TODO check for prerequisites here
        return conversationStateList.get(currentConversationIndex++);
    }
}
