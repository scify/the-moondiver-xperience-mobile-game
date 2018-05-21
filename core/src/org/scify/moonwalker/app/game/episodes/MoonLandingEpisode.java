package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.EpisodeWithEndState;
import org.scify.moonwalker.app.game.rules.episodes.MoonLandingEpisodeRules;

public class MoonLandingEpisode extends EpisodeWithEndState{
    public MoonLandingEpisode() {
        super(new MoonLandingEpisodeRules());
    }
}
