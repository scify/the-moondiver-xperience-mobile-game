package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.rules.SinglePlayerRules;

public class BaseEpisodeRules extends SinglePlayerRules {

    public static final String EPISODE_FINISHED = "EPISODE_FINISHED";
    public static final String EPISODE_STARTED = "EPISODE_STARTED";
    public static final String EPISODE_BACK = "BACK";
    public static final String CALCULATOR_STARTED = "CALCULATOR_STARTED";

    //audio related
    public static final String AUDIO_START_UI = "AUDIO_START_UI";
    public static final String AUDIO_START_LOOP_UI = "AUDIO_START_LOOP_UI";
    public static final String AUDIO_LOAD_UI = "AUDIO_LOAD_UI";
    public static final String AUDIO_STOP_UI = "AUDIO_STOP_UI";
    public static final String AUDIO_DISPOSE_UI = "AUDIO_DISPOSE_UI";
    public static final String AUDIO_TOGGLE_UI = "AUDIO_TOGGLE_UI";

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
        return gsCurrent.eventsQueueContainsEventOwnedBy(EPISODE_FINISHED, this);
    }

    protected boolean isEpisodeStarted(GameState gsCurrent) {
        return gsCurrent.eventsQueueContainsEventOwnedBy(EPISODE_STARTED, this);
    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        if (currentState.eventsQueueContainsEventOwnedBy(CALCULATOR_STARTED, this))
            return new EpisodeEndState(EpisodeEndStateCode.CALCULATOR_STARTED, cleanUpGameState(currentState));
        return null;
    }

    protected void episodeStartedEvents(GameState currentState) {
        if (!episodeStarted) {
            currentState.addGameEvent(new GameEvent(EPISODE_STARTED, null, this));
            episodeStarted = true;
        }
    }

    protected void episodeEndedEvents(GameState currentState) {
        currentState.addGameEvent(new GameEvent(EPISODE_FINISHED, null, this));
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
