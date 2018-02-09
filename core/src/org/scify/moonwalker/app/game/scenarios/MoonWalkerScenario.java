package org.scify.moonwalker.app.game.scenarios;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.episodes.CalculatorEpisode;
import org.scify.moonwalker.app.game.episodes.KnightRaceEpisode;
import org.scify.moonwalker.app.game.episodes.RoomEpisode;

public class MoonWalkerScenario extends Scenario {

    protected RenderingEngine renderingEngine;
    protected UserInputHandler userInputHandler;

    public MoonWalkerScenario(RenderingEngine renderingEngine, UserInputHandler userInputHandler) {
        super();
        this.renderingEngine = renderingEngine;
        this.userInputHandler = userInputHandler;
        Episode firstEpisode = new RoomEpisode(renderingEngine, userInputHandler);
        setFirstEpisode(firstEpisode);
        appendEpisode(new KnightRaceEpisode(renderingEngine, userInputHandler));
    }

    //TODO all episodes must be able to start with a given game state instance
    // EpisodeEndState is class, containing the game state instance
    // add ScenarioState class with game state field

    @Override
    protected Episode getNextEpisode(EpisodeEndState state) {
        EpisodeEndStateCode endStateCode = state.getEndStateCode();
        switch (endStateCode) {
            case CALCULATOR_STARTED:
                // or add calculator episode as temp episode
                // without changing the current episode
                Episode calculatorEpisode = new CalculatorEpisode(renderingEngine, userInputHandler, state.getGameState());
                return calculatorEpisode;
            case CALCULATOR_FINISHED:
                //when calculator episode ends, re-run current episode
                break;
            default:
                break;
        }
        return super.getNextEpisode(state);
    }
}
