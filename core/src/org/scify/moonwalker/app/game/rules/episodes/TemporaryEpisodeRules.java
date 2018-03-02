package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;

public class TemporaryEpisodeRules extends BaseEpisodeRules {

    public TemporaryEpisodeRules(GameState gsCurrent) {
        this.initialGameState = gsCurrent;
    }

    @Override
    protected void handleUserAction(GameState gsCurrent, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case BACK:
                gameEndedEvents(gsCurrent);
                break;
        }
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        GameState toReturn = gsCurrent;
        if(initialGameState != null) {
            toReturn = this.initialGameState;
        }
        if(gsCurrent.eventsQueueContainsEvent("PREVIOUS_EPISODE"))
            return new EpisodeEndState(EpisodeEndStateCode.TEMP_EPISODE_FINISHED, toReturn);
        return new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_FAILURE, toReturn);
    }

    @Override
    public void gameEndedEvents(GameState gsCurrent) {
        gsCurrent.addGameEvent(new GameEvent("PREVIOUS_EPISODE"));
        gsCurrent.addGameEvent(new GameEvent("EPISODE_FINISHED"));
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return gsCurrent.eventsQueueContainsEvent("PREVIOUS_EPISODE");
    }
}
