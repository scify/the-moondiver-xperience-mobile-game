package org.scify.moonwalker.app.game.scenarios;

import org.scify.engine.*;
import org.scify.engine.EpisodeEndStateCode;
import org.scify.moonwalker.app.game.episodes.*;

public class MoonWalkerScenario extends Scenario {

    protected RenderingEngine renderingEngine;
    protected UserInputHandler userInputHandler;

    public MoonWalkerScenario(RenderingEngine renderingEngine, UserInputHandler userInputHandler) {
        super();
        this.renderingEngine = renderingEngine;
        this.userInputHandler = userInputHandler;
        Episode firstEpisode = new MainMenuEpisode(renderingEngine, userInputHandler);
        setFirstEpisode(firstEpisode);
        appendEpisode(new AvatarSelectionEpisode(renderingEngine, userInputHandler));
        appendEpisode(new RoomEpisode(renderingEngine, userInputHandler));
        appendEpisode(new KnightRaceEpisode(renderingEngine, userInputHandler));
    }

    @Override
    protected Episode getNextEpisode(EpisodeEndState state) {
        EpisodeEndStateCode endStateCode = state.getEndStateCode();
        switch (endStateCode) {
            case CALCULATOR_STARTED:
                // add as first candidate for execution the calculator episode
                Episode calcEpisode = new CalculatorEpisode(renderingEngine, userInputHandler, state.getGameState());
                addEpisodeAsFirstCandidateEpisodeAfterCurrentEpisode(calcEpisode);
                try {
                    // Use new episode LIKE the current one (i.e. NOT the same)
                    addAfterXEpisodeLikeY(calcEpisode, currentEpisode);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                    return null;
                }
                break;
            case CALCULATOR_FINISHED:
                removeLastEpisodeAndCandidateEpisodes();
                return lastEpisode;
            case APP_QUIT:
                return  null;
            default:
                break;
        }
        return super.getNextEpisode(state);
    }
}
