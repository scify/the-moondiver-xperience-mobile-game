package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.renderables.effects.DelayEffect;
import org.scify.engine.renderables.effects.EffectSequence;
import org.scify.engine.renderables.effects.FunctionEffect;
import org.scify.moonwalker.app.game.rules.ConversationRules;
import org.scify.moonwalker.app.ui.renderables.MoonLandingRenderable;

import java.util.Set;

import static org.scify.engine.renderables.Renderable.ACTOR_EPISODE_MOON_LANDING;

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
            renderable.addBeforeFadeOut(new Runnable() {
                @Override
                public void run() {
                    //gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_DISPOSE_UI, renderable.FOREST_AUDIO_PATH));
                    //gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_DISPOSE_UI, renderable.BORING_MUSIC_AUDIO_PATH));
                }
            });
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
            // these static fields need to be set before the constructor is called, so that
            // the renderable constructor can initialise the corresponding image files

            MoonLandingRenderable.calculateResPaths(gameInfo.getSelectedPlayer(), gameInfo.isGameFullySuccessfullyCompleted());
            renderable = new MoonLandingRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), RENDERABLE_ID, gameInfo.getSelectedPlayer(), gameInfo.isGameFullySuccessfullyCompleted());


            renderable.addAfterFadeIn(new Runnable() {
                @Override
                public void run() {
                    renderable.enableChat();
                }
            });
            currentState.addRenderable(renderable);

            currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_LOOP_UI, renderable.AUDIO_BG_PATH));

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
}
