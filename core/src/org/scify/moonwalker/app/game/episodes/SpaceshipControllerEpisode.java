package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.EpisodeWithEndState;
import org.scify.moonwalker.app.game.rules.episodes.SpaceshipControllerEpisodeRules;

public class SpaceshipControllerEpisode extends EpisodeWithEndState {
    public SpaceshipControllerEpisode() {
        super(new SpaceshipControllerEpisodeRules());
    }
}
