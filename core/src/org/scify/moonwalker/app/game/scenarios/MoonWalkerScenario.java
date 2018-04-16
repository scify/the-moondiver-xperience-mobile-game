package org.scify.moonwalker.app.game.scenarios;

import org.scify.engine.*;
import org.scify.engine.EpisodeEndStateCode;
import org.scify.moonwalker.app.game.episodes.*;

public class MoonWalkerScenario extends Scenario {

    public MoonWalkerScenario() {
        Episode mainMenuEpisode = new MainMenuEpisode();
        Episode roomEpisode = new RoomEpisode();
        Episode cockpitEpisode = new CockpitEpisode();

//        Episode effectPlaygroundEpisode = new EffectPlaygroundEpisode();
//        setFirstEpisode(effectPlaygroundEpisode);

        setFirstEpisode(mainMenuEpisode);
        addEpisodeAfterCurrent(roomEpisode);
        //setFirstEpisode(new CockpitEpisode());



//        addEpisodeAfterCurrent(roomEpisode);
//        addEpisodeAfterCurrent(cockpitEpisode);

//        Episode forestLoadingEpisode = new ForestLoadingEpisode();
//        addEpisodeAfter(roomEpisode, forestLoadingEpisode);
//
//        Episode forestEpisode = new ForestEpisode();
//        addEpisodeAfter(forestLoadingEpisode, forestEpisode);
//
//        Episode cockpitEpisode = new CockpitEpisode();
//        addEpisodeAfter(forestEpisode, cockpitEpisode);

    }

    @Override
    protected Episode getNextEpisode(EpisodeEndState state) {
        EpisodeEndStateCode endStateCode = state.getEndStateCode();
        switch (endStateCode) {
            case CALCULATOR_STARTED:
                addTemporaryEpisode(new CalculatorEpisode());
                break;
            case CONTACT_SCREEN_EPISODE_STARTED:
                addTemporaryEpisode(new ContactScreenEpisode());
                break;
            case MAP_EPISODE_STARTED:
                addTemporaryEpisode(new MapEpisode());
                break;
            case SPACESHIP_CHARGER_EPISODE_STARTED:
                addTemporaryEpisode(new SpaceshipChargerEpisode());
                break;
            case SIMPLE_TIMED_IMAGE_EPISODE:
                addTemporaryEpisode(new SimpleTimedImageEpisode());
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
