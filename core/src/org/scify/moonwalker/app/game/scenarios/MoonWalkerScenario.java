package org.scify.moonwalker.app.game.scenarios;

import org.scify.engine.*;
import org.scify.engine.EpisodeEndStateCode;
import org.scify.moonwalker.app.game.episodes.*;

public class MoonWalkerScenario extends Scenario {

    public MoonWalkerScenario() {
        Episode firstEpisode = new MainMenuEpisode();
        //setFirstEpisode(new CockpitEpisode());
        setFirstEpisode(firstEpisode);
        Episode secondEpisode = new AvatarSelectionEpisode();
        addEpisodeAfterCurrent(secondEpisode);
        Episode roomEpisode = new RoomEpisode();
        addEpisodeAfter(secondEpisode, roomEpisode);
        Episode forestEpisode = new ForestEpisode();
        addEpisodeAfter(roomEpisode, forestEpisode);
        Episode cockpitEpisode = new CockpitEpisode();
        addEpisodeAfter(forestEpisode, cockpitEpisode);
        Episode spaceshipControllerEpisode = new SpaceshipControllerEpisode();
        addEpisodeAfter(forestEpisode, spaceshipControllerEpisode);
    }

    @Override
    protected Episode getNextEpisode(EpisodeEndState state) {
        EpisodeEndStateCode endStateCode = state.getEndStateCode();
        switch (endStateCode) {
            case CALCULATOR_STARTED:
                // add as first candidate for execution the calculator episode
                try {
                    // Use new episode LIKE the current one (i.e. NOT the same)
                    addIntermediateEpisode(new CalculatorEpisode(state.getGameState()));
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                    return null;
                }
                break;
            case MAP_EPISODE_STARTED:
                try {
                    // Use new episode LIKE the current one (i.e. NOT the same)
                    addIntermediateEpisode(new MapEpisode(state.getGameState()));
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

    protected void addIntermediateEpisode(Episode intermediate) throws CloneNotSupportedException {
        addEpisodeAsFirstCandidateEpisodeAfterCurrentEpisode(intermediate);
        // Use new episode LIKE the current one (i.e. NOT the same)
        addAfterXEpisodeLikeY(intermediate, currentEpisode);
    }
}
