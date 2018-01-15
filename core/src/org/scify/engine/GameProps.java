package org.scify.engine;

import org.scify.engine.Rules;
import org.scify.engine.UserInputHandler;
import org.scify.engine.RenderingEngine;
import org.scify.moonwalker.app.game.GameLevel;
import org.scify.moonwalker.app.game.GameType;

/**
 * Defines all the properties needed in order to initialize a new @see GameEngine instance
 */
public class GameProps {

    public RenderingEngine renderingEngine;
    public Rules rules;
    public UserInputHandler userInputHandler;
    public GameLevel gameLevel;

    public GameProps(RenderingEngine renderingEngine, Rules rules, UserInputHandler userInputHandler, GameLevel gameLevel) {
        this.renderingEngine = renderingEngine;
        this.rules = rules;
        this.userInputHandler = userInputHandler;
        this.gameLevel = gameLevel;
    }
}
