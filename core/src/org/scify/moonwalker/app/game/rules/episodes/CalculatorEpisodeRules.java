package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.rules.SinglePlayerRules;

/**
 * This is a self-contained episode (meaning that it usually gets invoked
 * by another episode), presenting a simple calculator to the user.
 * The constructor takes a {@link GameState} instance as an argument
 * in order to set the already defined (in another episode) game state
 * to the rules.
 */
public class CalculatorEpisodeRules extends SinglePlayerRules{

    protected GameState gsCurrent;

    public CalculatorEpisodeRules(GameState gsCurrent) {
        this.gsCurrent = gsCurrent;
    }

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        gsCurrent = super.getNextState(gsCurrent, userAction);
        if(isGamePaused(gsCurrent))
            return gsCurrent;
        handleGameStartingRules(gsCurrent);
        if(episodeFinished(gsCurrent)) {
            super.handleGameFinishedEvents(gsCurrent);
            this.handleGameFinishedEvents(gsCurrent);
        }
        return gsCurrent;
    }

    protected void handleGameStartingRules(GameState gsCurrent) {
        if (!gsCurrent.eventsQueueContainsEvent("EPISODE_STARTED")) {
            gsCurrent.addGameEvent(new GameEvent("EPISODE_STARTED"));
            gsCurrent.addGameEvent(new GameEvent("BACKGROUND_IMG_UI", "img/calculator_episode/bg.jpg"));
        }
    }

    /**
     * This method is similar to isGameFinished
     * However, it is used internally by the getNextState method
     * in order to decide whether the ending Game Events should be added
     * to the current game state
     * @param gsCurrent the current {@link GameState}
     * @return
     */
    protected boolean episodeFinished(GameState gsCurrent) {
        // this episode is considered finished either
        // when the player has reached the top border of the screen
        // or the base rules class decides that should finish
        return gsCurrent.eventsQueueContainsEvent("CALCULATOR_FINISHED") || super.isGameFinished(gsCurrent);
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return episodeFinished(gsCurrent) && gsCurrent.eventsQueueContainsEvent("EPISODE_FINISHED");
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        if(gsCurrent.eventsQueueContainsEvent("CALCULATOR_FINISHED"))
            return new EpisodeEndState(EpisodeEndStateCode.CALCULATOR_FINISHED, this.gsCurrent);
        return new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_FAILURE, this.gsCurrent);
    }
}
