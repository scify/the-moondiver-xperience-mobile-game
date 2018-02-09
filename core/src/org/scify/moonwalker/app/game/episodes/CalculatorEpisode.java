package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.rules.episodes.CalculatorEpisodeRules;

public class CalculatorEpisode extends Episode<EpisodeEndState>{

    public CalculatorEpisode(RenderingEngine renderingEngine, UserInputHandler userInputHandler, GameState gsCurrent) {
        super(renderingEngine, userInputHandler, "CalculatorEpisode");
        gameEngine.initialize(this.renderingEngine, this.userInputHandler, new CalculatorEpisodeRules(gsCurrent));
    }

    @Override
    public EpisodeEndState call() {
        EpisodeEndState endState = gameEngine.execute();
        return endState;
    }
}
