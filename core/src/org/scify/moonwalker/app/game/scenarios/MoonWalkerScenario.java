package org.scify.moonwalker.app.game.scenarios;

import org.scify.engine.*;
import org.scify.engine.EpisodeEndStateCode;
import org.scify.moonwalker.app.game.episodes.*;

public class MoonWalkerScenario extends Scenario {

    public MoonWalkerScenario() {
        Episode firstEpisode = new MainMenuEpisode();
        //setFirstEpisode(new CockpitEpisode(renderingEngine, userInputHandler));
        setFirstEpisode(firstEpisode);
        Episode secondEpisode = new AvatarSelectionEpisode();
        appendEpisode(secondEpisode);
        addEpisodeAfter(secondEpisode, new RoomEpisode());
    }

    @Override
    protected Episode getNextEpisode(EpisodeEndState state) {
        EpisodeEndStateCode endStateCode = state.getEndStateCode();
        switch (endStateCode) {
            case CALCULATOR_STARTED:
                // add as first candidate for execution the calculator episode
                Episode calcEpisode = new CalculatorEpisode(state.getGameState());
                addEpisodeAsFirstCandidateEpisodeAfterCurrentEpisode(calcEpisode);
                try {
                    // Use new episode LIKE the current one (i.e. NOT the same)
                    addAfterXEpisodeLikeY(calcEpisode, currentEpisode);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                    return null;
                }
                break;
            case TEMP_EPISODE_FINISHED:
                removeLastEpisodeAndCandidateEpisodes();
                return lastEpisode;
            case PREVIOUS_EPISODE:
                return lastEpisode;
            case APP_QUIT:
                return  null;
            default:
                break;
        }
        return super.getNextEpisode(state);
    }
}
