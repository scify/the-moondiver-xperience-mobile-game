package org.scify.moonwalker.app.game.rules.episodes;


import org.scify.engine.*;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.renderables.effects.FadeEffect;
import org.scify.moonwalker.app.game.SelectedPlayer;
import org.scify.moonwalker.app.game.rules.ConversationRules;
import org.scify.moonwalker.app.ui.renderables.RoomRenderable;

import java.util.ArrayList;
import java.util.Set;

import static org.scify.moonwalker.app.game.rules.ConversationRules.EVENT_RING_PHONE;

public class RoomEpisodeRules extends FadingEpisodeRules<RoomRenderable> {
    public static final String TOGGLE = "toggle";
    public static final String REVEAL_SKIP_BUTTON = "reveal_skip_button";
    public static final String RENDERABLE_ID = "room";
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
            if (gameInfo.getSelectedPlayer().equals(SelectedPlayer.boy)) {
                gsCurrent.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI, renderable.BOY_MUSIC_AUDIO_PATH));
            } else {
                gsCurrent.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI, renderable.GIRL_MUSIC_AUDIO_PATH));
            }
            //THIS HOW WE END AN EPISODE AND INITIATE FADE-OUT-EFFECT
            endEpisodeAndAddEventWithType(gsCurrent, "");

        } else if (renderable != null && renderable.isChatEnabled()) {
            if (gameInfo.getSelectedPlayer().equals(SelectedPlayer.boy))
                createConversation(gsCurrent, "conversations/episode_room.json", renderable.BOY_CONVERSATION_BG_IMG_PATH);
            else
                createConversation(gsCurrent, "conversations/episode_room.json", renderable.GIRL_CONVERSATION_BG_IMG_PATH);
        }
        return super.getNextState(gsCurrent, userAction);
    }

    @Override
    protected void handleUserAction(GameState gameState, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case UserActionCode.FINISH_EPISODE:
                gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.CLICK_AUDIO_PATH));
                appInfo.logEpisodeSkipped("ROOM_EPISODE");
                conversationRules.getLastConversationRenderable().addEffect(new FadeEffect(1,0,1000));
                break;
        }
        super.handleUserAction(gameState, userAction);
    }

    @Override
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            if (gameInfo.isFromLoad())
                currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI));
            if (gameInfo.getSelectedPlayer().equals(SelectedPlayer.boy)) {
                renderable = new RoomRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), RENDERABLE_ID, true);
            }
            else {
                renderable = new RoomRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), RENDERABLE_ID, false);
            }
            appInfo.logEpisodeStarted("Room episode");
            renderable.addAfterFadeIn(new Runnable() {
                @Override
                public void run() {
                    renderable.enableChat();
                }
            });
            currentState.addRenderables(new ArrayList<>(renderable.getAllRenderables()));
            currentState.addRenderable(renderable);
            currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_DISPOSE_UI));
            if (gameInfo.getSelectedPlayer().equals(SelectedPlayer.boy)) {
                currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_LOOP_UI, renderable.BOY_MUSIC_AUDIO_PATH));
            } else {
                currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_LOOP_UI, renderable.GIRL_MUSIC_AUDIO_PATH));
            }
            gameInfo.setMainEpisodeCounter(2);
            gameInfo.save();
            super.episodeStartedEvents(currentState);
        }
    }

    @Override
    protected void episodeEndedEvents(GameState currentState) {
        appInfo.logEpisodeEnded("Room episode");
        super.episodeEndedEvents(currentState);
    }

    @Override
    protected void onEnterConversationOrder(GameState gsCurrent, ConversationLine lineEntered) {
        Set<String> eventTrigger;
        eventTrigger = (Set<String>) gsCurrent.getGameEventWithType(ConversationRules.ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT).parameters;
        if (eventTrigger.contains(EVENT_RING_PHONE)) {
            gsCurrent.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.MOBILE_AUDIO_PATH));
            renderable.togglePhone();
        }
        if (eventTrigger.contains(TOGGLE)) {
            renderable.togglePhone();
        }
        if (eventTrigger.contains(REVEAL_SKIP_BUTTON)) {
            renderable.reveal(renderable.getSkipDialogButtonRenderable());
        }
        super.onEnterConversationOrder(gsCurrent, lineEntered);
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
