package org.scify.moonwalker.app.game.conversation;

import org.scify.engine.UserAction;

public interface ConversationRules {
    ConversationState getInitialState();
    ConversationState getNextState(UserAction userAction);
    boolean isConversationFinished();
}
