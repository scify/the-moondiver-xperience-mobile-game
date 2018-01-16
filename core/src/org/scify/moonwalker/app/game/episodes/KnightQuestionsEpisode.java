package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.rules.episodes.KnightQuestionsRules;

public class KnightQuestionsEpisode extends Episode<EpisodeEndState> {

    public KnightQuestionsEpisode(RenderingEngine renderingEngine, UserInputHandler userInputHandler) {
        super(renderingEngine, userInputHandler, "KnightQuestionsEpisode");
        gameEngine = new GameEngine();
        gameEngine.initialize(this.renderingEngine, this.userInputHandler, new KnightQuestionsRules());

    }

    @Override
    public EpisodeEndState call() {
        EpisodeEndState endState = gameEngine.execute();
        return endState;
    }

}
