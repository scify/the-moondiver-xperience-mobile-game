package org.scify.moonwalker.app.game;

import org.scify.engine.Rules;
import org.scify.engine.UserInputHandler;
import org.scify.engine.RenderingEngine;

/**
 * Defines all the properties needed in order to initialize a new @see GameEngine instance
 */
public class GameProps {

    public RenderingEngine renderingEngine;
    public Rules rules;
    public UserInputHandler userInputHandler;
    public GameType gameType;
    public GameLevel gameLevel;

    public GameProps(RenderingEngine renderingEngine, Rules rules, UserInputHandler userInputHandler, GameType gameType, GameLevel gameLevel) {
        this.renderingEngine = renderingEngine;
        this.rules = rules;
        this.userInputHandler = userInputHandler;
        this.gameType = gameType;
        this.gameLevel = gameLevel;
    }
}
