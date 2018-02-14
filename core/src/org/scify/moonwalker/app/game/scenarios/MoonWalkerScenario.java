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
        // TODO change
        Episode firstEpisode = new RoomEpisode(renderingEngine, userInputHandler);
        //Episode firstEpisode = new CalculatorEpisode(renderingEngine, userInputHandler);
        setFirstEpisode(firstEpisode);
        appendEpisode(new KnightRaceEpisode(renderingEngine, userInputHandler));
    }

    @Override
    protected Episode getNextEpisode(EpisodeEndState state) {
        EpisodeEndStateCode endStateCode = state.getEndStateCode();
        switch (endStateCode) {
            case CALCULATOR_STARTED:
                // add as first candidate for execution the calculator episode
                Episode calcEpisode = new CalculatorEpisode(renderingEngine, userInputHandler, state.getGameState());
                episodeListMap.get(currentEpisode).add(0, calcEpisode);
                try {
                    // Use new episode LIKE the current one (i.e. NOT the same)
                    addAfterXEpisodeLikeY(calcEpisode, currentEpisode);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                    return null;
                }
                break;
            case CALCULATOR_FINISHED:
                // remove calc episode from episode set
                episodeListMap.remove(episodeListMap.get(lastEpisode).get(0));
                // remove calculator episode from first candidate position of the previous episode
                episodeListMap.get(lastEpisode).remove(0);
                return lastEpisode;
            default:
                break;
        }
        return super.getNextEpisode(state);
    }
}
