package org.scify.moonwalker.app.game.rules.episodes;

import com.badlogic.gdx.utils.Timer;
import org.scify.engine.*;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.renderables.NextConversationRenderable;
import org.scify.moonwalker.app.game.SelectedPlayer;
import org.scify.moonwalker.app.ui.renderables.RoomRenderable;

import java.util.Date;
import java.util.Iterator;

public class RoomEpisodeRules extends BaseEpisodeRules {
    protected RoomRenderable room;
    protected boolean conversationStarted;
    protected boolean ringStarted;
    protected boolean nextButtonActivated;
    protected long lastRingDate;
    protected boolean ringStopped;
    protected boolean readyToEndEpisode;


    public RoomEpisodeRules() {
        super();
        conversationStarted = false;
        ringStarted = false;
        ringStopped = false;
        nextButtonActivated = false;
        readyToEndEpisode = false;
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
                currentState.addGameEvent(new GameEvent("AUDIO_DISPOSE_UI", "audio/room_episode/girl/music.mp3"));
            } else {
                currentState.addGameEvent(new GameEvent("AUDIO_START_LOOP_UI", "audio/room_episode/girl/music.mp3"));
                currentState.addGameEvent(new GameEvent("AUDIO_DISPOSE_UI", "audio/room_episode/boy/music.mp3"));

            }
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    initiateConversation();
                }
            }, 2, 1, 1);
        }
    }

    protected void initialize(GameState currentState) {
        room = new RoomRenderable(0, 0, appInfo.getScreenWidth(),
                appInfo.getScreenHeight(), "room", "room");
        currentState.addRenderable(room);
    }


    protected GameState handleConversationRules(GameState gsCurrent, UserAction userAction) {
        // begin conversation with Yoda
        // if the conversation has not started and has not finished too
        // TODO add conversation id in case we have multiple conversations in an episode
        if (conversationHasNotStartedAndNotFinished(gsCurrent)) {
            // call base class create method, passing the resource file for this specific conversation
            createConversation(gsCurrent, "conversations/episode_room.json");
            //createConversation(gsCurrent, "conversations/episode_room_test_font.json");
        }
        if (isConversationOngoing(gsCurrent)) {
            // ask the conversation rules to alter the current game state accordingly
            gsCurrent = conversationRules.getNextState(gsCurrent, userAction);

        }
        if (isConversationFinished(gsCurrent)) {
            if (gameInfo.getSelectedPlayer() == SelectedPlayer.boy) {
                gsCurrent.addGameEvent(new GameEvent("AUDIO_STOP_UI", "audio/room_episode/boy/music.mp3"));
                gsCurrent.addGameEvent(new GameEvent("AUDIO_DISPOSE_UI", "audio/room_episode/boy/music.mp3"));
            }
            else {
                gsCurrent.addGameEvent(new GameEvent("AUDIO_STOP_UI", "audio/room_episode/girl/music.mp3"));
                gsCurrent.addGameEvent(new GameEvent("AUDIO_DISPOSE_UI", "audio/room_episode/girl/music.mp3"));
            }
            readyToEndEpisode = true;
        }

        handleTriggerEventForCurrentConversationLine(gsCurrent);
        return gsCurrent;
    }

    protected void handleTriggerEventForCurrentConversationLine(GameState gameState) {
        ConversationLine currLine = conversationRules.getCurrentConversationLine(gameState);
        switch (currLine.getTriggerEvent()) {
            case "ring_start":
                if (ringStarted == false) {
                    ringStarted = true;
                    ((NextConversationRenderable)conversationRules.getLastConversationRenderable()).setButtonNextInActive();

                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            room.togglePhone();
                            if (!nextButtonActivated) {
                                nextButtonActivated = true;
                                ((NextConversationRenderable) conversationRules.getLastConversationRenderable()).setButtonNextActive();
                            }
                        }
                    }, 3, 1);

                    gameState.addGameEvent(new GameEvent("AUDIO_START_LOOP_UI", "audio/room_episode/mobile.mp3", new Date().getTime() + 3000, false));
                }
                break;
            case "ring_stop":
                if (ringStopped == false) {
                    ringStopped = true;
                    Timer.instance().clear();
                    gameState.addGameEvent(new GameEvent("AUDIO_STOP_UI", "audio/room_episode/mobile.mp3"));
                    gameState.addGameEvent(new GameEvent("AUDIO_DISPOSE_UI", "audio/room_episode/mobile.mp3"));
                    room.turnOnPhone();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return readyToEndEpisode;
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
        Iterator iter = currentState.getEventQueue().iterator();
        System.out.println("GameEvents:");
        while (iter.hasNext()) {
            System.out.println("\t" + ((GameEvent)iter.next()).type);
        }
        return currentState;
    }
}
