package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.EpisodeEndState;
import org.scify.engine.GameState;
import org.scify.engine.UserAction;
import org.scify.moonwalker.app.MoonWalkerGameState;
import org.scify.moonwalker.app.actors.Player;
import org.scify.moonwalker.app.game.rules.SinglePlayerRules;

public class KnightQuestionsRules extends SinglePlayerRules {

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        return super.getNextState(gsCurrent, userAction);
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        MoonWalkerGameState gameState = (MoonWalkerGameState) gsCurrent;
        Player player = gameState.getPlayer();
        if(player.getScore() >= 2)
            return true;
        return super.isGameFinished(gsCurrent);
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsFinal) {
        if(gsFinal.eventsQueueContainsEvent("CORRECT_ANSWER"))
            return EpisodeEndState.EPISODE_FINISHED_SUCCESS;
        return EpisodeEndState.EPISODE_FINISHED_FAILURE;
    }
}
