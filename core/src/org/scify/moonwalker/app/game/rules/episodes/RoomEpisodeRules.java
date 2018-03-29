package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.conversation.ConversationLine;
import org.scify.moonwalker.app.game.SelectedPlayer;
import org.scify.moonwalker.app.ui.renderables.RoomRenderable;

import java.util.Date;

public class RoomEpisodeRules extends BaseEpisodeRules {
    protected RoomRenderable room;

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        gsCurrent = handleConversationRules(gsCurrent, userAction);
        return super.getNextState(gsCurrent, userAction);
    }

    @Override
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            super.episodeStartedEvents(currentState);
            //addEpisodeBackgroundImage(currentState, "img/episode_0/bg.png");
            initialize(currentState);
            if (gameInfo.getSelectedPlayer() == SelectedPlayer.boy) {
                currentState.addGameEvent(new GameEvent("AUDIO_START_LOOP_UI", "audio/room_episode/boy/music.mp3"));
                currentState.addGameEvent(new GameEvent("AUDIO_STOP_UI", "audio/room_episode/boy/music.mp3", new Date().getTime() + 3000, false));
            }
            else {
                currentState.addGameEvent(new GameEvent("AUDIO_START_LOOP_UI", "audio/room_episode/girl/music.mp3"));
                currentState.addGameEvent(new GameEvent("AUDIO_STOP_UI", "audio/room_episode/girl/music.mp3", new Date().getTime() + 3000, false));
            }

            currentState.addGameEvent(new GameEvent("AUDIO_START_UI", "audio/room_episode/mobile.mp3", new Date().getTime() + 3000, false));
            currentState.addGameEvent(new GameEvent("ALTER_ROOM_IMAGE_UI", room, new Date().getTime() + 3000, false));
        }
    }

    protected void initialize(GameState currentState) {
        room = new RoomRenderable(0,0, appInfo.getScreenWidth(),
                appInfo.getScreenHeight(), "room", "room");
        room.setImgPath("img/episode_room/bg.png");
        currentState.addRenderable(room);
    }


    protected GameState handleConversationRules(GameState gsCurrent, UserAction userAction) {
        // begin conversation with Yoda
        // if the conversation has not started and has not finished too
        // TODO add conversation id in case we have multiple conversations in an episode
        if(conversationHasNotStartedAndNotFinished(gsCurrent)) {
            // call base class create method, passing the resource file for this specific conversation
            createConversation(gsCurrent, "conversations/episode_room.json");
        }
        if (isConversationOngoing(gsCurrent)) {
            // ask the conversation rules to alter the current game state accordingly
            gsCurrent = conversationRules.getNextState(gsCurrent, userAction);
        }
        if(isConversationFinished(gsCurrent))
            gsCurrent.addGameEvent(new GameEvent("CALCULATOR_STARTED", null, this));

        handleTriggerEventForCurrentConversationLine(gsCurrent);
        return gsCurrent;
    }

    protected void handleTriggerEventForCurrentConversationLine(GameState gameState) {
        ConversationLine currLine = conversationRules.getCurrentConversationLine(gameState);
        switch (currLine.getTriggerEvent()) {

        }
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return gsCurrent.eventsQueueContainsEvent("CONVERSATION_FINISHED");
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        EpisodeEndStateCode code = EpisodeEndStateCode.EPISODE_FINISHED_FAILURE;
        if(gsCurrent.eventsQueueContainsEvent("CONVERSATION_FINISHED"))
            code = EpisodeEndStateCode.EPISODE_FINISHED_SUCCESS;
        return new EpisodeEndState(code, cleanUpState(gsCurrent));
    }

    protected GameState cleanUpState(GameState currentState) {
        currentState.removeAllGameEventsOwnedBy(this);
        currentState.removeGameEventsWithType("CONVERSATION_READY_TO_FINISH");
        currentState.removeGameEventsWithType("CONVERSATION_FINISHED");
        currentState.removeGameEventsWithType("CONVERSATION_STARTED");
        return currentState;
    }
}
