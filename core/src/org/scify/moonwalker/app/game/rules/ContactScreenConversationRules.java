package org.scify.moonwalker.app.game.rules;

import org.scify.engine.GameState;
import org.scify.engine.conversation.ConversationLine;

public class ContactScreenConversationRules extends ConversationRules {

    public static final String AT_LEAST_ONE_DAY_REMAINING_FOR_THE_JOURNEY = "at_lest_one_day_remaining";
    public static final String NO_DAYS_REMAINING_FOR_THE_JOURNEY = "no_days_remaining";

    public ContactScreenConversationRules(String conversationJSONFilePath, String bgImgPath) {
        super(conversationJSONFilePath, bgImgPath);
    }

    @Override
    protected boolean satisfiesPrerequisites(ConversationLine next, GameState currentGameState) {
        boolean bSuperOK = super.satisfiesPrerequisites(next, currentGameState);

        if (!bSuperOK)
            return false;

        if (next.getPrerequisites().contains(AT_LEAST_ONE_DAY_REMAINING_FOR_THE_JOURNEY)) {
            return gameInfo.getDaysLeftForDestination() >= 1;
        }

        if (next.getPrerequisites().contains(NO_DAYS_REMAINING_FOR_THE_JOURNEY)) {
            return gameInfo.getDaysLeftForDestination() < 1;
        }

        return true;
    }
}
