package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.EpisodeWithEndState;
import org.scify.engine.rules.Rules;

public abstract class LocationEpisode extends EpisodeWithEndState {
    public LocationEpisode(Rules rules) {
        super(rules);
    }
}
