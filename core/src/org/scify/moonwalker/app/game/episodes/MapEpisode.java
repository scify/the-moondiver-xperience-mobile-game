package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.EpisodeWithEndState;
import org.scify.moonwalker.app.game.rules.episodes.MapEpisodeOBSOLETERules;

public class MapEpisode extends EpisodeWithEndState {
    public MapEpisode() {
        super(new MapEpisodeOBSOLETERules(false));
    }
}
