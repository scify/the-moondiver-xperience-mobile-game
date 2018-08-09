package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.EpisodeEndState;
import org.scify.engine.GameState;
import org.scify.engine.UserAction;
import org.scify.engine.renderables.Renderable;
import org.scify.moonwalker.app.ui.renderables.FullImageRenderable;

import java.util.Date;

import static org.scify.engine.EpisodeEndStateCode.EPISODE_FINISHED_SUCCESS;
import static org.scify.engine.EpisodeEndStateCode.TEMP_EPISODE_FINISHED;

public class SimpleTimedImageEpisodeRules extends FadingEpisodeRules<FullImageRenderable> {

    protected long episodeEndTimestamp;


    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        episodeStartedEvents(gsCurrent);
        if(new Date().getTime() > episodeEndTimestamp) {
            super.episodeEndedEvents(gsCurrent);
        }
        return super.getNextState(gsCurrent, userAction);
    }

    @Override
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            if (initialGameState.additionalDataEntryExists("timed_episode_milliseconds") ) {
                episodeEndTimestamp = new Date().getTime() + (int) initialGameState.getAdditionalDataEntry("timed_episode_milliseconds");
            }
            if (initialGameState.additionalDataEntryExists("timed_episode_img_path")) {
                renderable = new FullImageRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), Renderable.ACTOR_EPISODE_FULL_IMAGE, "full_image", (String) initialGameState.getAdditionalDataEntry("timed_episode_img_path"));
            }
            super.episodeStartedEvents(currentState);
        }
    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        return new EpisodeEndState(EPISODE_FINISHED_SUCCESS, cleanUpGameState(currentState));
    }
}
