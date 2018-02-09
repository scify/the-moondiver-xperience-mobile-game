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
        pushEpisode(new KnightRaceEpisode(renderingEngine, userInputHandler));
    }
}
