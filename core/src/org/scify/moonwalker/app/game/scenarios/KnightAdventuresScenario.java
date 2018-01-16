package org.scify.moonwalker.app.game.scenarios;

import org.scify.engine.Episode;
import org.scify.engine.RenderingEngine;
import org.scify.engine.Scenario;
import org.scify.engine.UserInputHandler;
import org.scify.moonwalker.app.game.episodes.KnightQuestionsEpisode;
import org.scify.moonwalker.app.game.episodes.KnightRaceEpisode;

public class KnightAdventuresScenario extends Scenario {

    public KnightAdventuresScenario(RenderingEngine renderingEngine, UserInputHandler userInputHandler) {
        super();
        Episode firstEpisode = new KnightRaceEpisode(renderingEngine, userInputHandler);
        Episode secondEpisode = new KnightQuestionsEpisode(renderingEngine, userInputHandler);
        setFirstEpisode(firstEpisode);
        addEpisodeAfter(firstEpisode, secondEpisode);
    }
}
