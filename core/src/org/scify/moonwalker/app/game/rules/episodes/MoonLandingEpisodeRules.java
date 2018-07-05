package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.renderables.effects.DelayEffect;
import org.scify.engine.renderables.effects.EffectSequence;
import org.scify.engine.renderables.effects.FunctionEffect;
import org.scify.moonwalker.app.game.SelectedPlayer;
import org.scify.moonwalker.app.game.rules.ConversationRules;
import org.scify.moonwalker.app.ui.renderables.MoonLandingRenderable;

import java.util.Set;

import static org.scify.engine.EpisodeEndStateCode.LOCATION_EPISODE_STARTED;
import static org.scify.engine.renderables.Renderable.ACTOR_EPISODE_MOON_LANDING;
import static org.scify.moonwalker.app.game.rules.ConversationRules.EVENT_ROOSTER;
import static org.scify.moonwalker.app.game.rules.ConversationRules.EVENT_SHOW_QUIZ_EPISODE;
import static org.scify.moonwalker.app.game.rules.ConversationRules.EVENT_WAIT_UNTIL_NIGHT;
import static org.scify.moonwalker.app.game.scenarios.MoonWalkerScenario.NEXT_LOCATION;

public class MoonLandingEpisodeRules extends FadingEpisodeRules<MoonLandingRenderable> {

    public static final String RENDERABLE_ID = ACTOR_EPISODE_MOON_LANDING;
    protected boolean outroInitiated;

    public MoonLandingEpisodeRules() {
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
                effects.addEffect(new DelayEffect(4000));
                effects.addEffect(new FunctionEffect(new Runnable() {
                    @Override
                    public void run() {
                        endEpisodeAndAddEventWithType(gameState, "");
                    }
                }));
                renderable.addEffect(effects);
            }else {
                gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI, renderable.AUDIO_BG_PATH));
                endEpisodeAndAddEventWithType(gameState, "");
            }
        } else if (renderable != null && renderable.isChatEnabled()) {
            createConversation(gameState, gameInfo.isGameFullySuccessfullyCompleted() ? "conversations/episode_moon_landing_success_full.json" : "conversations/episode_moon_landing_success_simple.json", renderable.CONVERSATION_BG_IMG_PATH);
        }
        return super.getNextState(gameState, userAction);
    }

    @Override
    public void episodeStartedEvents(final GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            if (gameInfo.isFromLoad())
                currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI));
            MoonLandingRenderable.calculateResPaths(gameInfo.getSelectedPlayer(), gameInfo.isGameFullySuccessfullyCompleted());
            renderable = new MoonLandingRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), RENDERABLE_ID, gameInfo.getSelectedPlayer(), gameInfo.isGameFullySuccessfullyCompleted());
            renderable.addAfterFadeIn(new Runnable() {
                @Override
                public void run() {
                    renderable.enableChat();
                }
            });
            currentState.addRenderable(renderable);
            currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_DISPOSE_UI));
            currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_LOOP_UI, renderable.AUDIO_BG_PATH));

            gameInfo.setMainEpisodeCounter(5);
            gameInfo.save();
            super.episodeStartedEvents(currentState);
        }
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

    @Override
    protected void onExitConversationOrder(GameState gameState, ConversationLine lineEntered) {
        Set<String> eventTrigger;
        if (gameState.eventsQueueContainsEvent(ConversationRules.ON_EXIT_CONVERSATION_ORDER_TRIGGER_EVENT)) {
            eventTrigger = (Set<String>) gameState.getGameEventWithType(ConversationRules.ON_EXIT_CONVERSATION_ORDER_TRIGGER_EVENT).parameters;
            if (eventTrigger.contains(EVENT_ROOSTER)) {
                gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI));
                gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.DAYPASSED_AUDIO_PATH));
            }
        }
        super.onEnterConversationOrder(gameState, lineEntered);
    }
}
