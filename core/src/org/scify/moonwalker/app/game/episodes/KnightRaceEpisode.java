package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.*;
import org.scify.engine.EpisodeEndState;
import org.scify.moonwalker.app.game.GameLevel;
import org.scify.moonwalker.app.game.rules.SinglePlayerRules;

public class KnightRaceEpisode extends Episode<EpisodeEndState>{

    public KnightRaceEpisode(RenderingEngine renderingEngine, UserInputHandler userInputHandler) {
        super(renderingEngine, userInputHandler);

        GameProps gameProps = new GameProps(renderingEngine, new SinglePlayerRules(), userInputHandler, new GameLevel());
        gameEngine = new GameEngine();
        gameEngine.initialize(gameProps);
    }

    @Override
    public EpisodeEndState call() {
        return gameEngine.call();
    }

    @Override
    public void finalize() {

    }
}
