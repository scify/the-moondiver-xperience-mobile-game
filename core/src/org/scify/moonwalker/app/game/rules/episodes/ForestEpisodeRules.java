package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.renderables.effects.EffectSequence;
import org.scify.engine.renderables.effects.FadeEffect;
import org.scify.engine.renderables.effects.FunctionEffect;
import org.scify.moonwalker.app.game.rules.ConversationRules;
import org.scify.moonwalker.app.ui.renderables.ForestRenderable;

import java.util.Set;

public class ForestEpisodeRules extends BaseEpisodeRules {
    public static final String CONVERSATION_FAIL = "fail";
    public static final String CONVERSATION_FINISHED = "CONVERSATION_FINISHED";
    protected ForestRenderable renderable;
    protected boolean outroInitiated;

    public ForestEpisodeRules() {
        super();
        renderable = null;
        outroInitiated = false;
    }


    @Override
    public GameState getNextState(final GameState gsCurrent, UserAction userAction) {
        if (conversationRules != null && conversationRules.isFinished() && !outroInitiated) {
            outroInitiated = true;
            renderable.setAfterFadeOut(new Runnable() {
                @Override
                public void run() {
                    endGameAndAddEventWithType(gsCurrent, EPISODE_FINISHED);
                }
            });
            renderable.fadeOut();
        } else if (renderable != null && renderable.isChatEnabled()) {
            // Initialize conversation
            createConversation(gsCurrent, "conversations/episode_forest.json");
        }
        return super.getNextState(gsCurrent, userAction);
    }

    @Override
    public void episodeStartedEvents(final GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            renderable = new ForestRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), "forest");
            renderable.fadeIn();
            currentState.addRenderable(renderable);
            currentState.addGameEvent(new GameEvent("AUDIO_START_LOOP_UI", renderable.FOREST_AUDIO_PATH));

            super.episodeStartedEvents(currentState);

        }
    }

    @Override
    protected void onEnterConversationOrder(GameState gsCurrent, ConversationLine lineEntered) {
        Set<String> eventTrigger;
        if(gsCurrent.eventsQueueContainsEvent(ConversationRules.ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT)) {
            eventTrigger = (Set<String>) gsCurrent.getGameEventsWithType(ConversationRules.ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT).parameters;
            if (eventTrigger.contains("ring_phone")) {
                gsCurrent.addGameEvent(new GameEvent("AUDIO_START_UI", renderable.MOBILE_AUDIO_PATH));
            }
        }

        super.onEnterConversationOrder(gsCurrent, lineEntered);
    }

    @Override
    protected void onExitConversationOrder(GameState gsCurrent, ConversationLine lineExited) {
        Set<String> eventTrigger;

        // If we received a conversation "fail" event
        if(gsCurrent.eventsQueueContainsEvent(ConversationRules.ON_EXIT_CONVERSATION_ORDER_TRIGGER_EVENT)) {
            eventTrigger = (Set<String>) gsCurrent.getGameEventsWithType(ConversationRules.ON_EXIT_CONVERSATION_ORDER_TRIGGER_EVENT).parameters;
            if (eventTrigger.contains(CONVERSATION_FAIL)) {
                gsCurrent.addGameEvent(new GameEvent(CONVERSATION_FAIL));
            }
        }

        super.onExitConversationOrder(gsCurrent, lineExited);
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        String code = "";
        // Handle failed conversation
        if (gsCurrent.eventsQueueContainsEvent(CONVERSATION_FAIL)) {
            code = EpisodeEndStateCode.EPISODE_FINISHED_FAILURE;
            gsCurrent.removeGameEventsWithType(CONVERSATION_FAIL);
        }
        else
        if (gsCurrent.eventsQueueContainsEvent(CONVERSATION_FINISHED)) {
            code = EpisodeEndStateCode.EPISODE_FINISHED_SUCCESS;
        }
        conversationRules.cleanUpState(gsCurrent);

        return new EpisodeEndState(code, cleanUpState(gsCurrent));
    }

    protected GameState cleanUpState(GameState currentState) {
        currentState.removeAllGameEventsOwnedBy(this);
        currentState.addGameEvent(new GameEvent("AUDIO_STOP_UI", renderable.FOREST_AUDIO_PATH));
        currentState.addGameEvent(new GameEvent("AUDIO_DISPOSE_UI", renderable.FOREST_AUDIO_PATH));
        currentState.removeGameEventsWithType("CONVERSATION_READY_TO_FINISH");
        currentState.removeGameEventsWithType(CONVERSATION_FINISHED);
        currentState.removeGameEventsWithType("CONVERSATION_STARTED");
        return currentState;
    }
}
