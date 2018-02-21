package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.*;
import org.scify.engine.EpisodeWithEndState;
import org.scify.moonwalker.app.game.rules.episodes.KnightQuestionsRules;

public class KnightQuestionsEpisode extends EpisodeWithEndState {

    public KnightQuestionsEpisode() {
        super(new KnightQuestionsRules());
    }

}
