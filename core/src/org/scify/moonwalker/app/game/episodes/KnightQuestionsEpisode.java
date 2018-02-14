package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.rules.episodes.KnightQuestionsRules;

public class KnightQuestionsEpisode extends EpisodeWithEndState {

    public KnightQuestionsEpisode(RenderingEngine renderingEngine, UserInputHandler userInputHandler) {
        super(renderingEngine, userInputHandler, "KnightQuestionsEpisode");
        gameEngine.initialize(this.renderingEngine, this.userInputHandler, new KnightQuestionsRules());
    }

}
