package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.EpisodeWithEndState;
import org.scify.moonwalker.app.game.rules.episodes.SplashScreenEpisodeRules;

public class SplashScreenEpisode extends EpisodeWithEndState {
    public SplashScreenEpisode() {
        super(new SplashScreenEpisodeRules());
    }
}