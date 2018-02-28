package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.EpisodeWithEndState;
import org.scify.moonwalker.app.game.rules.episodes.ForestEpisodeRules;

public class ForestEpisode extends EpisodeWithEndState{
    public ForestEpisode() {
        super(new ForestEpisodeRules());
    }
}
