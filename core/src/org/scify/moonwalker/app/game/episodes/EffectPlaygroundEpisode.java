package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.EpisodeWithEndState;
import org.scify.moonwalker.app.game.rules.episodes.EffectPlaygroundEpisodeRules;

public class EffectPlaygroundEpisode extends EpisodeWithEndState {

    public EffectPlaygroundEpisode() {
        super(new EffectPlaygroundEpisodeRules());
    }
}
