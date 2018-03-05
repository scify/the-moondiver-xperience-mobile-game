package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;

/**
 * This is a self-contained episode (meaning that it usually gets invoked
 * by another episode).
 * The constructor takes a {@link GameState} instance as an argument
 * in order to set the already defined (in another episode) game state
 * to the rules.
 */
public abstract class TemporaryEpisodeRules extends BaseEpisodeRules {

    @Override
    protected void handleUserAction(GameState gsCurrent, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case BACK:
                episodeEndedEvents(gsCurrent);
                break;
            default:
                super.handleUserAction(gsCurrent, userAction);
                break;
        }
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        GameState toReturn = cleanUpState(gsCurrent);
//        if(initialGameState != null) {
//            toReturn = this.initialGameState;
//        }
        if(gsCurrent.eventsQueueContainsEventOwnedBy("PREVIOUS_EPISODE", this))
            return new EpisodeEndState(EpisodeEndStateCode.TEMP_EPISODE_FINISHED, toReturn);
        return new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_FAILURE, toReturn);
    }

    @Override
    public void episodeEndedEvents(GameState gsCurrent) {
        gsCurrent.addGameEvent(new GameEvent("EPISODE_FINISHED", null, this));
        gsCurrent.addGameEvent(new GameEvent("PREVIOUS_EPISODE", null, this));
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return gsCurrent.eventsQueueContainsEventOwnedBy("PREVIOUS_EPISODE", this);
    }
}
