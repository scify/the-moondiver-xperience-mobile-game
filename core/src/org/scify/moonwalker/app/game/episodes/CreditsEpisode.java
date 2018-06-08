package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.EpisodeWithEndState;
import org.scify.moonwalker.app.game.rules.episodes.CreditsEpisodeRules;


public class CreditsEpisode extends EpisodeWithEndState {
    public CreditsEpisode() {
        super(new CreditsEpisodeRules());
    }
}
