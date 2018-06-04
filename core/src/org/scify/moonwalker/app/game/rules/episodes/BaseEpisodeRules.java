package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.rules.SinglePlayerRules;

public class BaseEpisodeRules extends SinglePlayerRules {

    public static final String GAME_EVENT_EPISODE_FINISHED = "GAME_EVENT_EPISODE_FINISHED";
    public static final String GAME_EVENT_EPISODE_STARTED = "GAME_EVENT_EPISODE_STARTED";
    public static final String GAME_EVENT_EPISODE_BACK = "BACK";
    public static final String GAME_EVENT_CALCULATOR_STARTED = "GAME_EVENT_CALCULATOR_STARTED";

    //audio related
    public static final String GAME_EVENT_AUDIO_START_UI = "GAME_EVENT_AUDIO_START_UI";
    public static final String GAME_EVENT_AUDIO_START_LOOP_UI = "GAME_EVENT_AUDIO_START_LOOP_UI";
    public static final String GAME_EVENT_AUDIO_STOP_UI = "GAME_EVENT_AUDIO_STOP_UI";
    public static final String GAME_EVENT_AUDIO_DISPOSE_UI = "GAME_EVENT_AUDIO_DISPOSE_UI";
    public static final String GAME_EVENT_AUDIO_TOGGLE_UI = "GAME_EVENT_AUDIO_TOGGLE_UI";
    public static final String GAME_EVENT_PREVIOUS_EPISODE = "GAME_EVENT_PREVIOUS_EPISODE";

    protected boolean episodeStarted;

    public BaseEpisodeRules() {
        super();
        episodeStarted = false;
    }

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        gsCurrent = super.getNextState(gsCurrent, userAction);
        if (isGamePaused(gsCurrent))
            return gsCurrent;
        episodeStartedEvents(gsCurrent);
        if (userAction != null)
            handleUserAction(gsCurrent, userAction);
        return gsCurrent;
    }

    protected void handleUserAction(GameState gsCurrent, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case UserActionCode.FINISH_EPISODE:
                episodeEndedEvents(gsCurrent);
                break;
        }
    }

    @Override
    public boolean isEpisodeFinished(GameState gsCurrent) {
        return gsCurrent.eventsQueueContainsEventOwnedBy(GAME_EVENT_EPISODE_FINISHED, this);
    }

    protected boolean isEpisodeStarted(GameState gsCurrent) {
        return gsCurrent.eventsQueueContainsEventOwnedBy(GAME_EVENT_EPISODE_STARTED, this);
    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        if (currentState.eventsQueueContainsEventOwnedBy(GAME_EVENT_CALCULATOR_STARTED, this))
            return new EpisodeEndState(EpisodeEndStateCode.CALCULATOR_STARTED, cleanUpGameState(currentState));
        return null;
    }

    protected void episodeStartedEvents(GameState currentState) {
        if (!episodeStarted) {
            currentState.addGameEvent(new GameEvent(GAME_EVENT_EPISODE_STARTED, null, this));
            episodeStarted = true;
        }
    }

    protected void episodeEndedEvents(GameState currentState) {
        currentState.addGameEvent(new GameEvent(GAME_EVENT_EPISODE_FINISHED, null, this));
    }

    @Override
    protected GameState cleanUpGameState(GameState currentState) {
        super.cleanUpGameState(currentState);
        currentState.clearRendereablesList();
        return currentState;
    }

    protected void endEpisodeAndAddEventWithType(GameState gsCurrent, String gameEventType) {
        gsCurrent.addGameEvent(new GameEvent(gameEventType, null, this));
        episodeEndedEvents(gsCurrent);
    }
}
