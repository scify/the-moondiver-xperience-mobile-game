package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.EpisodeWithEndState;
import org.scify.moonwalker.app.game.rules.episodes.SpaceshipInventoryEpisodeRules;

public class SpaceshipInventoryEpisode extends EpisodeWithEndState {
    public SpaceshipInventoryEpisode() {
        super(new SpaceshipInventoryEpisodeRules());
    }
}
