package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.conversation.ConversationLine;
import org.scify.moonwalker.app.game.SelectedPlayer;
import org.scify.moonwalker.app.ui.renderables.RoomRenderable;

import java.util.Date;

public class RoomEpisodeRules extends BaseEpisodeRules {
    protected RoomRenderable room;
    protected boolean conversationStarted;
    protected boolean ringStarted;
    protected long lastRingDate;
    protected boolean ringStopped;


    public RoomEpisodeRules() {
        super();
        conversationStarted = false;
        ringStarted = false;
        ringStopped = false;
        lastRingDate = 0;
    }

    public void initiateConversation() {
        conversationStarted = true;
    }

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        if (conversationStarted)
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
            } else {
                currentState.addGameEvent(new GameEvent("AUDIO_START_LOOP_UI", "audio/room_episode/girl/music.mp3"));

            }
            long timestamp = new Date().getTime();
            currentState.addGameEvent(new GameEvent("ENABLE_ROOM_DIALOG_UI", "", timestamp + 2000, false, this));
        }
    }

    protected void initialize(GameState currentState) {
        room = new RoomRenderable(0, 0, appInfo.getScreenWidth(),
                appInfo.getScreenHeight(), "room", "room");
        room.setImgPath("img/episode_room/bg.png");
        currentState.addRenderable(room);
    }


    protected GameState handleConversationRules(GameState gsCurrent, UserAction userAction) {
        // begin conversation with Yoda
        // if the conversation has not started and has not finished too
        // TODO add conversation id in case we have multiple conversations in an episode
        if (conversationHasNotStartedAndNotFinished(gsCurrent)) {
            // call base class create method, passing the resource file for this specific conversation
            createConversation(gsCurrent, "conversations/episode_room.json");
            //createConversation(gsCurrent, "conversations/episode_0.json");
        }
        if (isConversationOngoing(gsCurrent)) {
            // ask the conversation rules to alter the current game state accordingly
            gsCurrent = conversationRules.getNextState(gsCurrent, userAction);
        }
        if (isConversationFinished(gsCurrent))
            gsCurrent.addGameEvent(new GameEvent("CALCULATOR_STARTED", null, this));

        handleTriggerEventForCurrentConversationLine(gsCurrent);
        return gsCurrent;
    }

    protected void handleTriggerEventForCurrentConversationLine(GameState gameState) {
        ConversationLine currLine = conversationRules.getCurrentConversationLine(gameState);
        switch (currLine.getTriggerEvent()) {
            case "ring_start":
                long timestamp = new Date().getTime();
                if (ringStarted == false) {
                    long ringTimestamp = timestamp + 3000;
                    gameState.addGameEvent(new GameEvent("AUDIO_START_LOOP_UI", "audio/room_episode/mobile.mp3", ringTimestamp, false));
                    lastRingDate = ringTimestamp;
                    gameState.addGameEvent(new GameEvent("ALTER_ROOM_MOBILE_TOGLE_UI", room, ringTimestamp, false, this));
                    ringStarted = true;
                }else {
                    if (timestamp > lastRingDate + 600) {

                        lastRingDate = timestamp;
                        gameState.addGameEvent(new GameEvent("ALTER_ROOM_MOBILE_TOGLE_UI", room, this));
                    }
                }
                break;
            case "ring_stop":
                if (ringStopped == false) {
                    gameState.addGameEvent(new GameEvent("AUDIO_STOP_UI", "audio/room_episode/mobile.mp3"));
                    gameState.addGameEvent(new GameEvent("ALTER_ROOM_MOBILE_ON_UI", room, this));
                    ringStopped = true;
                }
                break;
            case "end":
                if (gameInfo.getSelectedPlayer() == SelectedPlayer.boy)
                    gameState.addGameEvent(new GameEvent("AUDIO_STOP_UI", "audio/room_episode/boy/music.mp3"));
                else
                    gameState.addGameEvent(new GameEvent("AUDIO_STOP_UI", "audio/room_episode/girl/music.mp3"));
                break;
            default:
                break;
        }
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return gsCurrent.eventsQueueContainsEvent("CONVERSATION_FINISHED");
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        EpisodeEndStateCode code = EpisodeEndStateCode.EPISODE_FINISHED_FAILURE;
        if (gsCurrent.eventsQueueContainsEvent("CONVERSATION_FINISHED"))
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
