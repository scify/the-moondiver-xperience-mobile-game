package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.GameState;
import org.scify.engine.UserAction;

import java.util.Date;

public class SimpleTimedImageEpisodeRules extends TemporaryEpisodeRules {

    protected long episodeEndTimestamp;

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        episodeStartedEvents(gsCurrent);
        if(new Date().getTime() > episodeEndTimestamp)
            episodeEndedEvents(gsCurrent);
        return super.getNextState(gsCurrent, userAction);
    }

    @Override
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            episodeEndTimestamp = new Date().getTime() + (int) initialGameState.getAdditionalDataEntry("timed_episode_milliseconds");
            super.episodeStartedEvents(currentState);
            addEpisodeBackgroundImage(currentState, (String) initialGameState.getAdditionalDataEntry("timed_episode_img_path"));
        }
    }

}
