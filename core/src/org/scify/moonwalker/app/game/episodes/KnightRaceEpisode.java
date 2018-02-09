package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.*;
import org.scify.engine.EpisodeEndState;
import org.scify.moonwalker.app.game.rules.episodes.KnightRaceRules;

public class KnightRaceEpisode extends Episode<EpisodeEndState>{

    public KnightRaceEpisode(RenderingEngine renderingEngine, UserInputHandler userInputHandler) {
        super(renderingEngine, userInputHandler, "KnightRaceEpisode");
        gameEngine.initialize(this.renderingEngine, this.userInputHandler, new KnightRaceRules());
    }

    @Override
    public EpisodeEndState call() {
        EpisodeEndState endState = gameEngine.execute();
        return endState;
    }

}
