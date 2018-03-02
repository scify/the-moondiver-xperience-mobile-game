package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.EpisodeWithEndState;
import org.scify.engine.GameState;
import org.scify.moonwalker.app.game.rules.episodes.SpaceshipChargerEpisodeRules;

public class SpaceshipChargerEpisode extends EpisodeWithEndState {

    public SpaceshipChargerEpisode(GameState gsCurrent) {
        super(new SpaceshipChargerEpisodeRules(gsCurrent));
    }

    public SpaceshipChargerEpisode() {
        super(new SpaceshipChargerEpisodeRules());
    }
}
