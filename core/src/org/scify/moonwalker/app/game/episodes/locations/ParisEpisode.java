package org.scify.moonwalker.app.game.episodes.locations;

import org.scify.moonwalker.app.game.LocationController;
import org.scify.moonwalker.app.game.episodes.LocationEpisode;
import org.scify.moonwalker.app.game.rules.episodes.LocationEpisodeRules;

public class ParisEpisode extends LocationEpisode {
    public ParisEpisode() {
        super(new LocationEpisodeRules(LocationController.getInstance().getLocations().get(6)));
    }
}
