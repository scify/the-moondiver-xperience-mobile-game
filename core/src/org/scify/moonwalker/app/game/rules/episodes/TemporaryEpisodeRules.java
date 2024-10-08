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
            case UserActionCode.BACK:
                episodeEndedEvents(gsCurrent);
                break;
            default:
                super.handleUserAction(gsCurrent, userAction);
                break;
        }
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        return new EpisodeEndState(EpisodeEndStateCode.TEMP_EPISODE_FINISHED, this.initialGameState);
    }

    @Override
    protected void episodeEndedEvents(GameState gsCurrent) {
        gsCurrent.addGameEvent(new GameEvent(BaseEpisodeRules.GAME_EVENT_EPISODE_FINISHED, null, this));
        gsCurrent.addGameEvent(new GameEvent(GAME_EVENT_PREVIOUS_EPISODE, null, this));
    }

    @Override
    public boolean isEpisodeFinished(GameState gsCurrent) {
        return gsCurrent.eventsQueueContainsEventOwnedBy(GAME_EVENT_PREVIOUS_EPISODE, this);
    }
}
