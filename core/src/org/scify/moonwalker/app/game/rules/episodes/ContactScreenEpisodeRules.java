package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.conversation.ConversationLine;
import org.scify.moonwalker.app.game.rules.ConversationRules;
import org.scify.moonwalker.app.ui.renderables.ContactScreenRenderable;

import java.util.ArrayList;
import java.util.Set;

public class ContactScreenEpisodeRules extends FadingEpisodeRules<ContactScreenRenderable> {

    protected static final String CONTACT_ID = "contact";
    protected boolean exitButtonEnabled;

    public ContactScreenEpisodeRules() {
        super();
        renderable = null;
        exitButtonEnabled = false;
    }

    @Override
    public GameState getNextState(final GameState gsCurrent, UserAction userAction) {
        if (conversationRules != null && conversationRules.isFinished()) {
            exitButtonEnabled = true;
        } else if (renderable != null && renderable.isChatEnabled()) {
            // Initialize conversation
            if (gameInfo.getCurrentDay() == 1)
                createConversation(gsCurrent, "conversations/episode_contact_screen1.json");
        }
        return super.getNextState(gsCurrent, userAction);
    }

    @Override
    public void episodeStartedEvents(final GameState gameState) {
        if (!isEpisodeStarted(gameState)) {
            renderable = new ContactScreenRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), CONTACT_ID);
            renderable.addAfterFadeIn(new Runnable() {
                @Override
                public void run() {
                    renderable.enableChat();
                }
            });
            //TODO remmove this, only for fast debugging
            gameInfo.setSelectedPlayer("boy");
            gameState.addRenderables(new ArrayList<>(renderable.getAllRenderables()));
            gameState.addRenderable(renderable);
            super.episodeStartedEvents(gameState);
        }
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        String code = EpisodeEndStateCode.EPISODE_FINISHED_SUCCESS;
        conversationRules.cleanUpState(gsCurrent);
        return new EpisodeEndState(code, cleanUpGameState(gsCurrent));
    }

    @Override
    protected void onEnterConversationOrder(GameState gsCurrent, ConversationLine lineEntered) {
        Set<String> eventTrigger;
        if (gsCurrent.eventsQueueContainsEvent(ConversationRules.ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT)) {
            eventTrigger = (Set<String>) gsCurrent.getGameEventWithType(ConversationRules.ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT).parameters;
            if (eventTrigger.contains("ring_phone")) {
                gsCurrent.addGameEvent(new GameEvent(AUDIO_START_UI, renderable.MOBILE_AUDIO_PATH));
            }
        }

        super.onEnterConversationOrder(gsCurrent, lineEntered);
    }

    @Override
    protected void handleUserAction(GameState gameState, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case EPISODE_BACK:
                if (exitButtonEnabled) {
                    endEpisodeAndAddEventWithType(gameState, "");
                }else {
                    gameState.addGameEvent(new GameEvent(AUDIO_START_UI, renderable.WRONG_BUTTON_AUDIO_PATH));
                }
                break;
        }
        super.handleUserAction(gameState, userAction);
    }
}
