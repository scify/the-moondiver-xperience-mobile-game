package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.EpisodeWithEndState;
import org.scify.moonwalker.app.game.rules.episodes.RoomEpisodeRules;

public class RoomEpisode extends EpisodeWithEndState {
    public RoomEpisode() {
        super(new RoomEpisodeRules());
    }
}
