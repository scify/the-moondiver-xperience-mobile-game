package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.rules.SinglePlayerRules;

public class BaseEpisodeRules extends SinglePlayerRules {

    public BaseEpisodeRules () {
        super();
    }

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        gsCurrent = super.getNextState(gsCurrent, userAction);
        if(isGamePaused(gsCurrent))
            return gsCurrent;
        episodeStartedEvents(gsCurrent);
        if(userAction != null)
            handleUserAction(gsCurrent, userAction);
        return gsCurrent;
    }

    protected void handleUserAction(GameState gsCurrent, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case FINISH_EPISODE:
                episodeEndedEvents(gsCurrent);
                break;
            case BACK:
                gsCurrent.addGameEvent(new GameEvent("BACK", null, this));
                episodeEndedEvents(gsCurrent);
                break;
        }
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return gsCurrent.eventsQueueContainsEventOwnedBy("EPISODE_FINISHED", this);
    }

    protected boolean isEpisodeStarted(GameState gsCurrent) {
        return gsCurrent.eventsQueueContainsEventOwnedBy("EPISODE_STARTED", this);
    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        if(currentState.eventsQueueContainsEventOwnedBy("CALCULATOR_STARTED", this))
            return new EpisodeEndState(EpisodeEndStateCode.CALCULATOR_STARTED, cleanUpState(currentState));
        return null;
    }

    protected void episodeStartedEvents(GameState currentState) {
        currentState.addGameEvent(new GameEvent("EPISODE_STARTED", null, this));
    }

    protected void episodeEndedEvents(GameState currentState) {
        currentState.addGameEvent(new GameEvent("EPISODE_FINISHED", null, this));
    }

    protected GameState cleanUpState(GameState currentState) {
        currentState.removeAllGameEventsOwnedBy(this);
        currentState.clearRendereablesList();
        return currentState;
    }

    protected void endGameAndAddEventWithType(GameState gsCurrent, String gameEventType) {
        episodeEndedEvents(gsCurrent);
        gsCurrent.addGameEvent(new GameEvent(gameEventType, null, this));
    }
}
