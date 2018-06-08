package org.scify.moonwalker.app.game.rules.episodes;


import org.scify.engine.*;
import org.scify.engine.conversation.ConversationLine;
import org.scify.moonwalker.app.game.SelectedPlayer;
import org.scify.moonwalker.app.game.rules.ConversationRules;
import org.scify.moonwalker.app.ui.renderables.DreamingRoomRenderable;

import java.util.ArrayList;
import java.util.Set;

import static org.scify.moonwalker.app.game.rules.ConversationRules.EVENT_RING_PHONE;

public class DreamingRoomEpisodeRules extends FadingEpisodeRules<DreamingRoomRenderable> {
    public static final String TOGGLE = "toggle";
    public static final String RENDERABLE_ID = "dreaming_room";
    protected boolean outroInitiated;


    public DreamingRoomEpisodeRules() {
        super();
        outroInitiated = false;
    }

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        if (conversationRules != null && conversationRules.isFinished() && !outroInitiated) {
            outroInitiated = true;
            gsCurrent.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.CREDITS_AUDIO_PATH));
            if (gameInfo.getSelectedPlayer() == SelectedPlayer.boy) {
                gsCurrent.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI, renderable.BOY_MUSIC_AUDIO_PATH));
            } else {
                gsCurrent.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI, renderable.GIRL_MUSIC_AUDIO_PATH));
            }
            endEpisodeAndAddEventWithType(gsCurrent, "");

        } else if (renderable != null && renderable.isChatEnabled()) {
            if (gameInfo.getSelectedPlayer() == SelectedPlayer.boy)
                createConversation(gsCurrent, "conversations/episode_dreaming_room.json", renderable.BOY_CONVERSATION_BG_IMG_PATH);
            else
                createConversation(gsCurrent, "conversations/episode_dreaming_room.json", renderable.GIRL_CONVERSATION_BG_IMG_PATH);
        }
        return super.getNextState(gsCurrent, userAction);
    }

    @Override
    public void episodeStartedEvents(final GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            if (gameInfo.getSelectedPlayer() == SelectedPlayer.boy) {
                renderable = new DreamingRoomRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), RENDERABLE_ID, true);
            }
            else {
                renderable = new DreamingRoomRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), RENDERABLE_ID, false);
            }
            currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_DISPOSE_UI));
            renderable.addAfterFadeIn(new Runnable() {
                @Override
                public void run() {
                    renderable.enableChat();
                    currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.DAYPASSED_AUDIO_PATH));
                    if (gameInfo.getSelectedPlayer() == SelectedPlayer.boy) {
                        currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_LOOP_UI, renderable.BOY_MUSIC_AUDIO_PATH));
                    } else {
                        currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_LOOP_UI, renderable.GIRL_MUSIC_AUDIO_PATH));
                    }
                    renderable.getEyesRenderable().setVisible(true);
                }
            });
            currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI));
            //currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_LOAD_UI, renderable.DAYPASSED_AUDIO_PATH));
            currentState.addRenderables(new ArrayList<>(renderable.getAllRenderables()));
            currentState.addRenderable(renderable);
            currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.WAKEUP_AUDIO_PATH));
            //currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_LOAD_UI, renderable.CREDITS_AUDIO_PATH));
            super.episodeStartedEvents(currentState);
        }
    }

    @Override
    protected void onEnterConversationOrder(GameState gsCurrent, ConversationLine lineEntered) {
        Set<String> eventTrigger;
        eventTrigger = (Set<String>) gsCurrent.getGameEventWithType(ConversationRules.ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT).parameters;
        if (eventTrigger.contains(EVENT_RING_PHONE)) {
            gsCurrent.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.MOBILE_AUDIO_PATH));
        }
        if (eventTrigger.contains(TOGGLE)) {
            renderable.togglePhone();
        }
        super.onEnterConversationOrder(gsCurrent, lineEntered);
    }

    @Override
    protected void onExitConversationOrder(GameState gsCurrent, ConversationLine lineEntered) {
        Set<String> eventTrigger;
        eventTrigger = (Set<String>) gsCurrent.getGameEventWithType(ConversationRules.ON_EXIT_CONVERSATION_ORDER_TRIGGER_EVENT).parameters;
        if (eventTrigger.contains(EVENT_RING_PHONE)) {
            gsCurrent.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.MOBILE_AUDIO_PATH));
        }
        if (eventTrigger.contains(TOGGLE)) {
            renderable.togglePhone();
        }
        super.onExitConversationOrder(gsCurrent, lineEntered);
    }


    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        String code = EpisodeEndStateCode.EPISODE_FINISHED_FAILURE;
        if (gsCurrent.eventsQueueContainsEvent(conversationRules.CONVERSATION_FINISHED)) {
            code = EpisodeEndStateCode.EPISODE_FINISHED_SUCCESS;
            conversationRules.cleanUpState(gsCurrent);
        }
        return new EpisodeEndState(code, cleanUpGameState(gsCurrent));
    }
}
