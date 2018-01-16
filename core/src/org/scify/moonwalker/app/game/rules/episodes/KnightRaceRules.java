package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.GameState;
import org.scify.engine.UserAction;
import org.scify.moonwalker.app.game.rules.SinglePlayerRules;

public class KnightRaceRules extends SinglePlayerRules {

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        return super.getNextState(gsCurrent, userAction);
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return gsCurrent.eventsQueueContainsEvent("PLAYER_TOP_BORDER") || super.isGameFinished(gsCurrent);
    }
}
