package org.scify.moonwalker.app.game.scenarios;

import org.scify.engine.Episode;
import org.scify.engine.RenderingEngine;
import org.scify.engine.Scenario;
import org.scify.engine.UserInputHandler;
import org.scify.moonwalker.app.game.episodes.KnightRaceEpisode;
import org.scify.moonwalker.app.game.episodes.RoomEpisode;

public class MoonWalkerScenario extends Scenario {

    public MoonWalkerScenario(RenderingEngine renderingEngine, UserInputHandler userInputHandler) {
        super();
        Episode firstEpisode = new RoomEpisode(renderingEngine, userInputHandler);
        setFirstEpisode(firstEpisode);
        appendEpisode(new KnightRaceEpisode(renderingEngine, userInputHandler));
    }

    //TODO all episodes must be able to start with a given game state instance
    // EpisodeEndState is class, containing the game state instance
    // add ScenarioState class with game state field

     // TODO override getNextEpisode
    // if episode endstate is calculator
    // add calculator episode and re-add previous episode
    // else call parent
    // or add calculator episode as temp episode
    // without changing the current episode
    // and when calculator episode ends, re-run current episode
}
