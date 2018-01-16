package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.EpisodeEndState;
import org.scify.engine.GameState;
import org.scify.engine.UserAction;
import org.scify.moonwalker.app.game.rules.SinglePlayerRules;

public class KnightRaceRules extends SinglePlayerRules {

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        gsCurrent = super.getNextState(gsCurrent, userAction);
        if(isGamePaused(gsCurrent))
            return gsCurrent;
        return gsCurrent;
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return gsCurrent.eventsQueueContainsEvent("PLAYER_TOP_BORDER") || super.isGameFinished(gsCurrent);
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        if(gsCurrent.eventsQueueContainsEvent("PLAYER_TOP_BORDER"))
            return EpisodeEndState.EPISODE_FINISHED_SUCCESS;
        return EpisodeEndState.EPISODE_FINISHED_FAILURE;
    }
}
