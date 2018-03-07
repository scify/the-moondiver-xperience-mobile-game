package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.EpisodeWithEndState;
import org.scify.engine.rules.Rules;
import org.scify.moonwalker.app.game.rules.episodes.ForestLoadingEpisodeRules;

public class ForestLoadingEpisode extends EpisodeWithEndState{
    public ForestLoadingEpisode() {
        super(new ForestLoadingEpisodeRules());
    }
}
