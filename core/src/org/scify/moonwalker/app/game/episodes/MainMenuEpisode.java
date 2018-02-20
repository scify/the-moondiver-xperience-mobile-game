package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.EpisodeWithEndState;
import org.scify.engine.RenderingEngine;
import org.scify.engine.UserInputHandler;
import org.scify.moonwalker.app.game.rules.episodes.MainMenuEpisodeRules;

public class MainMenuEpisode extends EpisodeWithEndState {

    public MainMenuEpisode(RenderingEngine renderingEngine, UserInputHandler userInputHandler) {
        super(renderingEngine, userInputHandler, "MainMenuEpisode", new MainMenuEpisodeRules());
    }
}
