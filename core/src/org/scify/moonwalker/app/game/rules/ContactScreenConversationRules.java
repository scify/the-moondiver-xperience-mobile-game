package org.scify.moonwalker.app.game.rules;

import org.scify.engine.GameState;
import org.scify.engine.conversation.ConversationLine;

import java.util.Iterator;
import java.util.Set;

public class ContactScreenConversationRules extends ConversationRules {

    public static final String AT_LEAST_ONE_DAY_REMAINING_FOR_THE_JOURNEY = "at_lest_one_day_remaining";
    public static final String AT_LEAST_ONE_DAY_NOTIFICATION_NOT_SHOWN = "notification_not_shown";
    public static final String AT_LEAST_ONE_DAY_NOTIFICATION_SHOWN = "notification_shown";
    public static final String NO_DAYS_REMAINING_FOR_THE_JOURNEY = "no_days_remaining";
    public static final String SET_NOTIFICATION_SHOWN_EVENT = "set_notification_shown";

    public ContactScreenConversationRules(String conversationJSONFilePath, String bgImgPath) {
        super(conversationJSONFilePath, bgImgPath);
    }

    @Override
    protected boolean satisfiesPrerequisites(ConversationLine next, GameState currentGameState) {
        boolean bSuperOK = super.satisfiesPrerequisites(next, currentGameState);

        if (!bSuperOK)
            return false;
        Set<String> prerequisites = next.getPrerequisites();

        for (String prerequisite : prerequisites) {
            if(!satisfiesPrerequisite(prerequisite, currentGameState))
                return false;
        }
        return true;
    }

    protected boolean satisfiesPrerequisite(String prerequisite, GameState currentGameState) {
        boolean satisfiesPrerequisite = true;
        if (prerequisite.equals(AT_LEAST_ONE_DAY_REMAINING_FOR_THE_JOURNEY)) {
            satisfiesPrerequisite = gameInfo.getDaysLeftForDestination() >= 1;
        }

        if (prerequisite.equals(NO_DAYS_REMAINING_FOR_THE_JOURNEY)) {
            satisfiesPrerequisite = gameInfo.getDaysLeftForDestination() < 1;
        }

        if (prerequisite.equals(AT_LEAST_ONE_DAY_NOTIFICATION_NOT_SHOWN)) {
            satisfiesPrerequisite = !gameInfo.isNoDaysLeftNotificationShown();
        }

        if (prerequisite.equals(AT_LEAST_ONE_DAY_NOTIFICATION_SHOWN)) {
            satisfiesPrerequisite = gameInfo.isNoDaysLeftNotificationShown();
        }
        return satisfiesPrerequisite;
    }

    @Override
    protected void onExitConversationOrder(GameState gsCurrent, ConversationLine lineExited) {
        Set<String> sAllEvents = lineExited.getOnExitCurrentOrderTrigger();

        if (sAllEvents.contains(SET_NOTIFICATION_SHOWN_EVENT)) {
            gameInfo.setNoDaysLeftNotificationShown(true);
        }

        super.onExitConversationOrder(gsCurrent, lineExited);
    }
}
