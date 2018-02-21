package org.scify.moonwalker.app.game.scenarios;

import org.scify.engine.Episode;
import org.scify.engine.RenderingEngine;
import org.scify.engine.Scenario;
import org.scify.engine.UserInputHandler;
import org.scify.moonwalker.app.game.episodes.KnightQuestionsEpisode;
import org.scify.moonwalker.app.game.episodes.KnightRaceEpisode;

public class KnightAdventuresScenario extends Scenario {

    public KnightAdventuresScenario() {
        super();
        Episode firstEpisode = new KnightRaceEpisode();
        setFirstEpisode(new KnightRaceEpisode());
        addEpisodeAfter(firstEpisode, new KnightQuestionsEpisode());
    }
}
