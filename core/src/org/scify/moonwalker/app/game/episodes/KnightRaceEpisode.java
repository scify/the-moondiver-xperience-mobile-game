package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.EpisodeWithEndState;
import org.scify.moonwalker.app.game.rules.episodes.KnightRaceRules;

public class KnightRaceEpisode extends EpisodeWithEndState {

    public KnightRaceEpisode() {
        super(new KnightRaceRules());
    }

}
