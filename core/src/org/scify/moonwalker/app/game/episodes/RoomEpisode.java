package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.Episode;
import org.scify.engine.EpisodeEndState;
import org.scify.engine.RenderingEngine;
import org.scify.engine.UserInputHandler;
import org.scify.moonwalker.app.game.rules.episodes.RoomEpisodeRules;

public class RoomEpisode extends Episode<EpisodeEndState>{

    public RoomEpisode(RenderingEngine renderingEngine, UserInputHandler userInputHandler) {
        super(renderingEngine, userInputHandler, "RoomEpisode");
        gameEngine.initialize(this.renderingEngine, this.userInputHandler, new RoomEpisodeRules());
    }

    @Override
    public EpisodeEndState call() {
        EpisodeEndState endState = gameEngine.execute();
        return endState;
    }
}
