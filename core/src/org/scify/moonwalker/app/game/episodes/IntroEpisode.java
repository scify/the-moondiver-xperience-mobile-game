package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.EpisodeWithEndState;
import org.scify.moonwalker.app.game.rules.episodes.IntroEpisodeRules;

public class IntroEpisode extends EpisodeWithEndState {
    public IntroEpisode() { super(new IntroEpisodeRules());}
}
