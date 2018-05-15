package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.EpisodeWithEndState;
import org.scify.moonwalker.app.game.LocationController;
import org.scify.moonwalker.app.game.rules.episodes.LocationEpisodeRules;

public class BerlinEpisode extends EpisodeWithEndState {
    public BerlinEpisode() {
        super(new LocationEpisodeRules(LocationController.getInstance().getLocations().get(0)));
    }
}
