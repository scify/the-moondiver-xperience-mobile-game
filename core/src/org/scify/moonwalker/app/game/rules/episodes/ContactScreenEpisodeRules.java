package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.conversation.ConversationLine;
import org.scify.moonwalker.app.game.rules.ConversationRules;
import org.scify.moonwalker.app.ui.renderables.ContactScreenRenderable;

import java.util.ArrayList;
import java.util.Set;

public class ContactScreenEpisodeRules extends FadingEpisodeRules<ContactScreenRenderable> {

    protected static final String RENDERABLE_ID = "contact";
    protected boolean outroInitiated;

    public ContactScreenEpisodeRules() {
        super();
        renderable = null;
        outroInitiated = false;
    }

    @Override
    public GameState getNextState(final GameState gameState, UserAction userAction) {
        if (conversationRules != null && conversationRules.isFinished() && !outroInitiated) {
            outroInitiated = true;
            if (gameInfo.getCurrentDay() == 1) {
                gameInfo.setMapRequestFlag(true);
            }
            gameInfo.setContactRequestFlag(false);
            endEpisodeAndAddEventWithType(gameState, "");
        } else if (renderable != null && renderable.isChatEnabled()) {
            // Initialize conversation
            // we need to check wich conversation to show
            // if we are on the first day of the journey, show the initial conversation
            // otherwise, check to see if we need to show a conversation for a successfully/ not successfully completed location episode
            if (gameInfo.getCurrentDay() == 1)
                createConversation(gameState, "conversations/episode_contact_screen1.json", renderable.CONVERSATION_BG_IMG_PATH);
            else {
                createConversation(gameState, gameInfo.getConversationFileForContactScreen(), renderable.CONVERSATION_BG_IMG_PATH);
            }
        }
        return super.getNextState(gameState, userAction);
    }

    @Override
    public void episodeStartedEvents(final GameState gameState) {
        if (!isEpisodeStarted(gameState)) {
            renderable = new ContactScreenRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), RENDERABLE_ID);
            renderable.addAfterFadeIn(new Runnable() {
                @Override
                public void run() {
                    renderable.enableChat();
                }
            });
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
                gsCurrent.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.MOBILE_AUDIO_PATH));
            }
        }

        super.onEnterConversationOrder(gsCurrent, lineEntered);
    }
}
