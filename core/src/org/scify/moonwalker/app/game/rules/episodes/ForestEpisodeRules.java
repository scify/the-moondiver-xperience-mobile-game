package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.EffectSequence;
import org.scify.engine.renderables.effects.FadeEffect;
import org.scify.engine.renderables.effects.FunctionEffect;
import org.scify.moonwalker.app.game.SelectedPlayer;
import org.scify.moonwalker.app.game.rules.ConversationRules;
import org.scify.moonwalker.app.ui.renderables.ForestRenderable;
import org.scify.moonwalker.app.ui.renderables.RoomRenderable;

import java.util.Set;

public class ForestEpisodeRules extends BaseEpisodeRules{
    protected ForestRenderable renderable;
    protected boolean readyToEndEpisode;
    protected boolean outroInitiated;


    public ForestEpisodeRules() {
        super();
        renderable = null;
        readyToEndEpisode = false;
        outroInitiated = false;
    }


    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        GameEvent room_outro_event = gsCurrent.getGameEventsWithType("FOREST_OUTRO");
        if (room_outro_event != null) {
            gsCurrent.removeGameEventsWithType("FOREST_OUTRO");
            outroInitiated = true;
            EffectSequence fadeOutEffects = new EffectSequence();
            fadeOutEffects.addEffect(new FadeEffect(1.0, 0.0, 2000));
            fadeOutEffects.addEffect(new FunctionEffect(new Runnable() {
                @Override
                public void run() {
                    readyToEndEpisode = true;
                }
            }));
            renderable.addEffect(fadeOutEffects);
        } else if (renderable != null && renderable.isChatEnabled() && outroInitiated == false) {
            gsCurrent = handleConversationRules(gsCurrent, userAction);
        }
        return super.getNextState(gsCurrent, userAction);
    }

    @Override
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            renderable = new ForestRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), "forest");
            renderable.setVisible(false);
            currentState.addRenderable(renderable);
            super.episodeStartedEvents(currentState);
            currentState.addGameEvent(new GameEvent("AUDIO_START_LOOP_UI", renderable.FOREST_AUDIO_PATH));
        }
    }

    protected GameState handleConversationRules(GameState gsCurrent, UserAction userAction) {
        if (conversationHasNotStartedAndNotFinished(gsCurrent)) {
            // call base class create method, passing the resource file for this specific conversation
            createConversation(gsCurrent, "conversations/episode_forest.json");
        }
        if (isConversationOngoing(gsCurrent)) {
            // ask the conversation rules to alter the current game state accordingly
            gsCurrent = conversationRules.getNextState(gsCurrent, userAction);
        }
        if (isConversationFinished(gsCurrent)) {
            gsCurrent.addGameEvent(new GameEvent("FOREST_OUTRO"));
        }
        handleTriggerEventForCurrentConversationLine(gsCurrent);
        return gsCurrent;
    }

    protected void handleTriggerEventForCurrentConversationLine(GameState gameState) {
        Set<String> eventTrigger;
        if(gameState.eventsQueueContainsEvent(ConversationRules.ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT)) {
            eventTrigger = (Set<String>) gameState.getGameEventsWithType(ConversationRules.ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT).parameters;
            if (eventTrigger.contains("ring_phone")) {
                gameState.addGameEvent(new GameEvent("AUDIO_START_UI", renderable.MOBILE_AUDIO_PATH));
            }
            gameState.removeGameEventsWithType(ConversationRules.ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT);
        }
    }

    @Override
    public boolean isEpisodeFinished(GameState gsCurrent) {
        return readyToEndEpisode;
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        EpisodeEndStateCode code = EpisodeEndStateCode.EPISODE_FINISHED_FAILURE;
        if (gsCurrent.eventsQueueContainsEvent("CONVERSATION_FINISHED")) {
            code = EpisodeEndStateCode.EPISODE_FINISHED_SUCCESS;
            conversationRules.cleanUpState(gsCurrent);
        }

        return new EpisodeEndState(code, cleanUpState(gsCurrent));
    }

    protected GameState cleanUpState(GameState currentState) {
        currentState.removeAllGameEventsOwnedBy(this);
        currentState.addGameEvent(new GameEvent("AUDIO_STOP_UI", renderable.FOREST_AUDIO_PATH));
        currentState.addGameEvent(new GameEvent("AUDIO_DISPOSE_UI", renderable.FOREST_AUDIO_PATH));
        currentState.removeGameEventsWithType("CONVERSATION_READY_TO_FINISH");
        currentState.removeGameEventsWithType("CONVERSATION_FINISHED");
        currentState.removeGameEventsWithType("CONVERSATION_STARTED");
        return currentState;
    }
}
