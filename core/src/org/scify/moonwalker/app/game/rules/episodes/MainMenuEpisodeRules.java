package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.EpisodeEndState;
import org.scify.engine.GameEvent;
import org.scify.engine.GameState;
import org.scify.engine.UserAction;
import org.scify.moonwalker.app.game.rules.SinglePlayerRules;

public class MainMenuEpisodeRules extends SinglePlayerRules {

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        gsCurrent = super.getNextState(gsCurrent, userAction);
        if(isGamePaused(gsCurrent))
            return gsCurrent;
        gameStartedEvents(gsCurrent);
        if(userAction != null)
            handleUserAction(gsCurrent, userAction);
        return gsCurrent;
    }

    @Override
    public boolean isGameFinished(GameState currentState) {
        return false;
    }

    @Override
    public void gameStartedEvents(GameState gsCurrent) {
        if (!gsCurrent.eventsQueueContainsEvent("EPISODE_STARTED")) {
            gsCurrent.addGameEvent(new GameEvent("EPISODE_STARTED"));
            gsCurrent.addGameEvent(new GameEvent("BACKGROUND_IMG_UI", "img/calculator_episode/bg.jpg"));
        }
    }

    private void handleUserAction(GameState gsCurrent, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case FINISH_EPISODE:
                gameEndedEvents(gsCurrent);
                break;
        }
    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        return null;
    }

    @Override
    public void gameEndedEvents(GameState currentState) {

    }

    @Override
    public void gameResumedEvents(GameState currentState) {

    }
}
