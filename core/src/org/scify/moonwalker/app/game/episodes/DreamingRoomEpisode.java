package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.EpisodeWithEndState;
import org.scify.moonwalker.app.game.rules.episodes.DreamingRoomEpisodeRules;

public class DreamingRoomEpisode extends EpisodeWithEndState {
    public DreamingRoomEpisode() {
        super(new DreamingRoomEpisodeRules());
    }
}
