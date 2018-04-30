package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.EpisodeWithEndState;
import org.scify.engine.GameState;
import org.scify.moonwalker.app.game.rules.episodes.MapEpisodeRules;

public class MapEpisode extends EpisodeWithEndState {
    public MapEpisode() {
        super(new MapEpisodeRules(false));
    }
}
