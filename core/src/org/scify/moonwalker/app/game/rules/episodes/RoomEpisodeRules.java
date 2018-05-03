package org.scify.moonwalker.app.game.rules.episodes;


import org.scify.engine.*;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.renderables.effects.EffectSequence;
import org.scify.engine.renderables.effects.FadeEffect;
import org.scify.engine.renderables.effects.FunctionEffect;
import org.scify.moonwalker.app.game.SelectedPlayer;
import org.scify.moonwalker.app.game.rules.ConversationRules;
import org.scify.moonwalker.app.ui.renderables.RoomRenderable;

import java.util.ArrayList;
import java.util.Set;

public class RoomEpisodeRules extends FadingEpisodeRules<RoomRenderable> {
    public static final String RING_PHONE = "ring_phone";
    public static final String CONVERSATION_FINISHED = "CONVERSATION_FINISHED";
    public static final String TOGGLE = "toggle";
    public static final String ROOM_ID = "room";
    protected boolean outroInitiated;


    public RoomEpisodeRules() {
        super();
        outroInitiated = false;
    }

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        if (conversationRules != null && conversationRules.isFinished() && !outroInitiated) {
            outroInitiated = true;
            renderable.turnOffPhone();
            if (gameInfo.getSelectedPlayer() == SelectedPlayer.boy) {
                gsCurrent.addGameEvent(new GameEvent("AUDIO_STOP_UI", renderable.BOY_MUSIC_AUDIO_PATH));
                gsCurrent.addGameEvent(new GameEvent("AUDIO_DISPOSE_UI", renderable.BOY_MUSIC_AUDIO_PATH));
            } else {
                gsCurrent.addGameEvent(new GameEvent("AUDIO_STOP_UI", renderable.GIRL_MUSIC_AUDIO_PATH));
                gsCurrent.addGameEvent(new GameEvent("AUDIO_DISPOSE_UI", renderable.GIRL_MUSIC_AUDIO_PATH));
            }
            //THIS HOW WE END AN EPISODE AND INITIATE FADE-OUT-EFFECT
            endEpisodeAndAddEventWithType(gsCurrent, "");

        } else if (renderable != null && renderable.isChatEnabled()) {
            createConversation(gsCurrent, "conversations/episode_room.json");
        }
        return super.getNextState(gsCurrent, userAction);
    }

    @Override
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            if (gameInfo.getSelectedPlayer() == SelectedPlayer.boy)
                renderable = new RoomRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), ROOM_ID, true);
            else
                renderable = new RoomRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), ROOM_ID, false);
            renderable.addAfterFadeIn(new Runnable() {
                @Override
                public void run() {
                    renderable.enableChat();
                }
            });
            currentState.addRenderables(new ArrayList<>(renderable.getAllRenderables()));
            currentState.addRenderable(renderable);

            super.episodeStartedEvents(currentState);
            if (gameInfo.getSelectedPlayer() == SelectedPlayer.boy) {
                currentState.addGameEvent(new GameEvent("AUDIO_START_LOOP_UI", renderable.BOY_MUSIC_AUDIO_PATH));
                currentState.addGameEvent(new GameEvent("AUDIO_DISPOSE_UI", renderable.GIRL_MUSIC_AUDIO_PATH));
            } else {
                currentState.addGameEvent(new GameEvent("AUDIO_START_LOOP_UI", renderable.GIRL_MUSIC_AUDIO_PATH));
                currentState.addGameEvent(new GameEvent("AUDIO_DISPOSE_UI", renderable.BOY_MUSIC_AUDIO_PATH));

            }
        }
    }

    @Override
    protected void onEnterConversationOrder(GameState gsCurrent, ConversationLine lineEntered) {
        Set<String> eventTrigger;
        eventTrigger = (Set<String>) gsCurrent.getGameEventsWithType(ConversationRules.ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT).parameters;
        if (eventTrigger.contains(RING_PHONE)) {
            gsCurrent.addGameEvent(new GameEvent("AUDIO_START_UI", renderable.MOBILE_AUDIO_PATH));
            renderable.togglePhone();
        }
        if (eventTrigger.contains(TOGGLE)) {
            renderable.togglePhone();
        }
        super.onEnterConversationOrder(gsCurrent, lineEntered);
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        String code = EpisodeEndStateCode.EPISODE_FINISHED_FAILURE;
        if (gsCurrent.eventsQueueContainsEvent(CONVERSATION_FINISHED)) {
            code = EpisodeEndStateCode.EPISODE_FINISHED_SUCCESS;
            conversationRules.cleanUpState(gsCurrent);
        }
        return new EpisodeEndState(code, cleanUpState(gsCurrent));
    }

    protected GameState cleanUpState(GameState currentState) {
        currentState.removeAllGameEventsOwnedBy(this);
        currentState.removeGameEventsWithType("CONVERSATION_READY_TO_FINISH");
        currentState.removeGameEventsWithType("CONVERSATION_FINISHED");
        currentState.removeGameEventsWithType("CONVERSATION_STARTED");
        currentState.removeGameEventsWithType(CONVERSATION_FINISHED);
        return currentState;
    }
}
