package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.renderables.effects.DelayEffect;
import org.scify.engine.renderables.effects.EffectSequence;
import org.scify.engine.renderables.effects.FunctionEffect;
import org.scify.moonwalker.app.game.Location;
import org.scify.moonwalker.app.game.rules.ConversationRules;
import org.scify.moonwalker.app.ui.renderables.ForestRenderable;
import org.scify.moonwalker.app.ui.renderables.LocationRenderable;

import java.util.Set;

public class LocationEpisodeRules extends FadingEpisodeRules<LocationRenderable> {

    public static final String RENDERABLE_ID = "location";
    protected boolean outroInitiated;
    protected Location location;

    public LocationEpisodeRules (Location location) {
        super();
        renderable = null;
        outroInitiated = false;
        this.location = location;
    }

    @Override
    public void episodeStartedEvents(final GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            renderable = new LocationRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), RENDERABLE_ID, location.getEpisodeBackgroundImagePath());
            renderable.addAfterFadeIn(new Runnable() {
                @Override
                public void run() {
                    renderable.enableChat();
                }
            });
            currentState.addRenderable(renderable);
            super.episodeStartedEvents(currentState);
        }
    }

    @Override
    public GameState getNextState(final GameState gameState, UserAction userAction) {
        if (conversationRules != null && conversationRules.isFinished() && !outroInitiated) {
            outroInitiated = true;

        } else if (renderable != null && renderable.isChatEnabled()) {
            // Initialize conversation
            createConversation(gameState, location.getConversationPath());
        }
        return super.getNextState(gameState, userAction);
    }

    @Override
    protected void onEnterConversationOrder(GameState gsCurrent, ConversationLine lineEntered) {
        Set<String> eventTrigger;
        if (gsCurrent.eventsQueueContainsEvent(ConversationRules.ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT)) {
            eventTrigger = (Set<String>) gsCurrent.getGameEventWithType(ConversationRules.ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT).parameters;
            /*if (eventTrigger.contains("ring_phone")) {
                gsCurrent.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.MOBILE_AUDIO_PATH));
            }*/
        }

        super.onEnterConversationOrder(gsCurrent, lineEntered);
    }

    @Override
    protected void onExitConversationOrder(GameState gsCurrent, ConversationLine lineExited) {
        Set<String> eventTrigger;

        // If we received a conversation "fail" event
        if (gsCurrent.eventsQueueContainsEvent(ConversationRules.ON_EXIT_CONVERSATION_ORDER_TRIGGER_EVENT)) {
            eventTrigger = (Set<String>) gsCurrent.getGameEventWithType(ConversationRules.ON_EXIT_CONVERSATION_ORDER_TRIGGER_EVENT).parameters;
            /*if (eventTrigger.contains(conversationRules.TAG_FAIL)) {
                gsCurrent.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_LOAD_UI, renderable.MAINMENU_AUDIO_PATH));
                gsCurrent.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI, renderable.FOREST_AUDIO_PATH));
                gsCurrent.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.BORING_MUSIC_AUDIO_PATH));
                gsCurrent.addGameEvent(new GameEvent(conversationRules.CONVERSATION_FAILED));
            }*/
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
