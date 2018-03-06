package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.EpisodeWithEndState;
import org.scify.moonwalker.app.game.rules.episodes.SimpleTimedImageEpisodeRules;

public class SimpleTimedImageEpisode extends EpisodeWithEndState {
    public SimpleTimedImageEpisode() {
        super(new SimpleTimedImageEpisodeRules());
    }
}
