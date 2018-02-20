package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.*;
import org.scify.engine.EpisodeWithEndState;
import org.scify.moonwalker.app.game.rules.episodes.RoomEpisodeRules;

public class RoomEpisode extends EpisodeWithEndState {

    public RoomEpisode(RenderingEngine renderingEngine, UserInputHandler userInputHandler) {
        super(renderingEngine, userInputHandler, "RoomEpisode", new RoomEpisodeRules());
    }
}
