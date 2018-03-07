package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.GameState;

public class ForestLoadingEpisodeRules extends SimpleTimedImageEpisodeRules {

    protected final int MILLISECONDS_FOR_EPISODE = 4000;

    @Override
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            setFieldsForTimedEpisode(initialGameState, "img/shady-forest.jpg", MILLISECONDS_FOR_EPISODE);
        }
        super.episodeStartedEvents(currentState);
    }

}
