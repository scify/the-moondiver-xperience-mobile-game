package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.*;
import org.scify.engine.EpisodeWithEndState;
import org.scify.moonwalker.app.game.rules.episodes.CalculatorEpisodeRules;

/**
 * This is a self-contained episode (meaning that it usually gets invoked
 * by another episode), presenting a simple calculator to the user.
 * The constructor takes a {@link GameState} instance as an argument
 * in order to set the already defined (in another episode) game state
 * to the rules.
 */
public class CalculatorEpisode extends EpisodeWithEndState {

    public CalculatorEpisode(RenderingEngine renderingEngine, UserInputHandler userInputHandler, GameState gsCurrent) {
        super(renderingEngine, userInputHandler, "CalculatorEpisode", new CalculatorEpisodeRules(gsCurrent));
    }
}
