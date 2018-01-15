package org.scify.moonwalker.app;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.GameLevel;
import org.scify.moonwalker.app.game.GameType;
import org.scify.moonwalker.app.rules.RulesFactory;

public class KnightRaceEpisode extends Episode{

    public KnightRaceEpisode(RenderingEngine renderingEngine, UserInputHandler userInputHandler) {
        super(renderingEngine, userInputHandler);
        RulesFactory rulesFactory = new RulesFactory();
        GameProps gameProps = new GameProps(renderingEngine, rulesFactory.createRules(GameType.SINGLE_PLAYER), userInputHandler, new GameLevel());
        gameEngine = new GameEngine();
        gameEngine.initialize(gameProps);
    }
}
