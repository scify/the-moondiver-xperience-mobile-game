package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.GameState;


public class SplashScreenEpisodeRules extends SimpleTimedImageEpisodeRules {

    protected final int MILLISECONDS_FOR_EPISODE = 2000;

    @Override
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            setFieldsForTimedEpisode(initialGameState, "img/episode_splash/bg.png", MILLISECONDS_FOR_EPISODE);
        }
        super.episodeStartedEvents(currentState);
    }

}
