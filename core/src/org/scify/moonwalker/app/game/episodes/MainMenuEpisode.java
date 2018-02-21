package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.EpisodeWithEndState;
import org.scify.engine.RenderingEngine;
import org.scify.engine.UserInputHandler;
import org.scify.moonwalker.app.game.rules.episodes.MainMenuEpisodeRules;

public class MainMenuEpisode extends EpisodeWithEndState {

    public MainMenuEpisode() {
        super(new MainMenuEpisodeRules());
    }
}
