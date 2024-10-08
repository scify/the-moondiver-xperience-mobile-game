package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.renderables.effects.DelayEffect;
import org.scify.engine.renderables.effects.EffectSequence;
import org.scify.engine.renderables.effects.FunctionEffect;
import org.scify.moonwalker.app.game.rules.ConversationRules;
import org.scify.moonwalker.app.game.rules.QuestionConversationRules;
import org.scify.moonwalker.app.ui.renderables.ForestRenderable;

import java.util.Set;

import static org.scify.moonwalker.app.game.rules.ConversationRules.EVENT_RING_PHONE;

public class ForestEpisodeRules extends FadingEpisodeRules<ForestRenderable> {
    public static final String RENDERABLE_ID = "forest";
    protected boolean outroInitiated;

    public ForestEpisodeRules() {
        super();
        renderable = null;
        outroInitiated = false;
    }


    @Override
    public GameState getNextState(final GameState gameState, UserAction userAction) {
        if (conversationRules != null && conversationRules.isFinished() && !outroInitiated) {
            outroInitiated = true;
            if (gameState.getGameEventWithType(conversationRules.CONVERSATION_FAILED)  != null) {
                EffectSequence effects = new EffectSequence();
                effects.addEffect(new DelayEffect(3000));
                effects.addEffect(new FunctionEffect(new Runnable() {
                    @Override
                    public void run() {
                        gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI, renderable.BORING_MUSIC_AUDIO_PATH));
                        endEpisodeAndAddEventWithType(gameState, "");
                    }
                }));
                renderable.addEffect(effects);
            }else {
                gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI, renderable.FOREST_AUDIO_PATH));
                endEpisodeAndAddEventWithType(gameState, "");
            }
        } else if (renderable != null && renderable.isChatEnabled()) {
            // Initialize conversation
            if(conversationRules == null) {
                conversationRules = new QuestionConversationRules("conversations/episode_forest.json", renderable.CONVERSATION_BG_IMG_PATH, "", "", renderable.CORRECT_AUDIO_PATH, renderable.WRONG_AUDIO_PATH, true, 1);
                conversationRules.setStarted(true);
                initializeParseVariables();
            }
        }
        return super.getNextState(gameState, userAction);
    }

    @Override
    public void episodeStartedEvents(final GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            if (gameInfo.isFromLoad())
                currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI));
            renderable = new ForestRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), RENDERABLE_ID);
            renderable.addAfterFadeIn(new Runnable() {
                @Override
                public void run() {
                    renderable.enableChat();
                }
            });
            //currentState.addRenderable(renderable);
            currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_DISPOSE_UI));
            currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_LOOP_UI, renderable.FOREST_AUDIO_PATH));

            gameInfo.setMainEpisodeCounter(3);
            gameInfo.save();

            super.episodeStartedEvents(currentState);
        }
    }

    @Override
    protected void onEnterConversationOrder(GameState gsCurrent, ConversationLine lineEntered) {
        Set<String> eventTrigger;
        if (gsCurrent.eventsQueueContainsEvent(ConversationRules.ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT)) {
            eventTrigger = (Set<String>) gsCurrent.getGameEventWithType(ConversationRules.ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT).parameters;
            if (eventTrigger.contains(EVENT_RING_PHONE)) {
                gsCurrent.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.MOBILE_AUDIO_PATH));
            }
        }

        super.onEnterConversationOrder(gsCurrent, lineEntered);
    }

    @Override
    protected void onExitConversationOrder(GameState gsCurrent, ConversationLine lineExited) {
        Set<String> eventTrigger;

        // If we received a conversation "fail" event
        if (gsCurrent.eventsQueueContainsEvent(ConversationRules.ON_EXIT_CONVERSATION_ORDER_TRIGGER_EVENT)) {
            eventTrigger = (Set<String>) gsCurrent.getGameEventWithType(ConversationRules.ON_EXIT_CONVERSATION_ORDER_TRIGGER_EVENT).parameters;
            if (eventTrigger.contains(conversationRules.TAG_FAIL)) {
                gsCurrent.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI, renderable.FOREST_AUDIO_PATH));
                gsCurrent.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.BORING_MUSIC_AUDIO_PATH));
                gsCurrent.addGameEvent(new GameEvent(conversationRules.CONVERSATION_FAILED));
            }
        }

        super.onExitConversationOrder(gsCurrent, lineExited);
    }

    @Override
    public EpisodeEndState determineEndState(GameState gameState) {
        String code = "";
        // Handle failed conversation
        if (gameState.eventsQueueContainsEvent(conversationRules.CONVERSATION_FAILED))
            code = EpisodeEndStateCode.SCENARIO_NEEDS_RESTART;
        else if (gameState.eventsQueueContainsEvent(conversationRules.CONVERSATION_FINISHED))
            code = EpisodeEndStateCode.EPISODE_FINISHED_SUCCESS;

        conversationRules.cleanUpState(gameState);
        return new EpisodeEndState(code, cleanUpGameState(gameState));
    }
}
