package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.*;
import org.scify.engine.EpisodeWithEndState;
import org.scify.moonwalker.app.game.rules.episodes.KnightRaceRules;

public class KnightRaceEpisode extends EpisodeWithEndState {

    public KnightRaceEpisode(RenderingEngine renderingEngine, UserInputHandler userInputHandler) {
        super(renderingEngine, userInputHandler, "KnightRaceEpisode");
        rules = new KnightRaceRules();
        gameEngine.initialize(this.renderingEngine, this.userInputHandler, rules);
    }

}
