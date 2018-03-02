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
    }

    @Override
    protected Episode getNextEpisode(EpisodeEndState state) {
        EpisodeEndStateCode endStateCode = state.getEndStateCode();
        switch (endStateCode) {
            case CALCULATOR_STARTED:
                // add as first candidate for execution the calculator episode
                // Use new episode LIKE the current one (i.e. NOT the same)
                addTemporaryEpisode(new CalculatorEpisode(state.getGameState()));
                break;
            case MAP_EPISODE_STARTED:
                // Use new episode LIKE the current one (i.e. NOT the same)
                addTemporaryEpisode(new MapEpisode(state.getGameState()));
                break;
            case SPACESHIP_CHARGER_EPISODE_STARTED:
                // Use new episode LIKE the current one (i.e. NOT the same)
                addTemporaryEpisode(new SpaceshipChargerEpisode(state.getGameState()));
                break;
            case TEMP_EPISODE_FINISHED:
                break;
            case PREVIOUS_EPISODE:
                return lastEpisode;
            case APP_QUIT:
                return  null;
        }
        return super.getNextEpisode(state);
    }
}
