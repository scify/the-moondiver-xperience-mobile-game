package org.scify.moonwalker.app.game.episodes.locations;

import org.scify.moonwalker.app.game.LocationController;
import org.scify.moonwalker.app.game.episodes.LocationEpisode;
import org.scify.moonwalker.app.game.rules.episodes.LocationEpisodeRules;

public class MadridEpisode extends LocationEpisode {
    public MadridEpisode() {
        super(new LocationEpisodeRules(LocationController.getInstance().getLocations().get(1)));
    }
}
