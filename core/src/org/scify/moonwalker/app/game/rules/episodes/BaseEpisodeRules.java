package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.rules.SinglePlayerRules;

public class BaseEpisodeRules extends SinglePlayerRules {

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        gsCurrent = super.getNextState(gsCurrent, userAction);
        if(isGamePaused(gsCurrent))
            return gsCurrent;
        gameStartedEvents(gsCurrent);
        gameResumedEvents(gsCurrent);
        if(userAction != null)
            handleUserAction(gsCurrent, userAction);
        return gsCurrent;
    }

    protected void handleUserAction(GameState gsCurrent, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case CALCULATOR_EPISODE:
                gameEndedEvents(gsCurrent);
                gsCurrent.addGameEvent(new GameEvent("CALCULATOR_STARTED", null, this));
                break;
            case MAP_EPISODE:
                gameEndedEvents(gsCurrent);
                gsCurrent.addGameEvent(new GameEvent("MAP_EPISODE_STARTED", null, this));
                break;
            case FINISH_EPISODE:
                gameEndedEvents(gameState);
                break;
            case BACK:
                gsCurrent.addGameEvent(new GameEvent("BACK", null, this));
                gameEndedEvents(gameState);
                break;
        }
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return gsCurrent.eventsQueueContainsEventOwnedBy("EPISODE_FINISHED", this);
    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        if(currentState.eventsQueueContainsEventOwnedBy("CALCULATOR_STARTED", this))
            return new EpisodeEndState(EpisodeEndStateCode.CALCULATOR_STARTED, cleanUpState(currentState));
        return null;
    }

    public void gameStartedEvents(GameState currentState) {

    }

    public void gameEndedEvents(GameState currentState) {
        currentState.addGameEvent(new GameEvent("EPISODE_FINISHED", null, this));
    }

    public void gameResumedEvents(GameState currentState) {

    }

    protected GameState cleanUpState(GameState currentState) {
        currentState.removeAllGameEventsOwnedBy(this);
        return currentState;
    }

    protected boolean gameHasStarted(GameState currentState) {
        return currentState.eventsQueueContainsEventOwnedBy("EPISODE_STARTED", this);
    }

    protected void addGameStartedEvents(GameState currentState) {
        currentState.addGameEvent(new GameEvent("EPISODE_STARTED", null, this));
    }
}
