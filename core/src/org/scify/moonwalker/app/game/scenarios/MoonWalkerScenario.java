package org.scify.moonwalker.app.game.scenarios;

import org.scify.engine.*;
import org.scify.engine.EpisodeEndStateCode;
import org.scify.moonwalker.app.game.episodes.*;

public class MoonWalkerScenario extends Scenario {

    public MoonWalkerScenario() {
        Episode mainMenuEpisode = new MainMenuEpisode();
//        Episode roomEpisode = new RoomEpisode();
        Episode forestLoadingEpisode = new ForestEpisode();

//        Episode effectPlaygroundEpisode = new EffectPlaygroundEpisode();
//        setFirstEpisode(effectPlaygroundEpisode);

        setFirstEpisode(mainMenuEpisode);
        //addEpisodeAfterCurrent(roomEpisode);
        addEpisodeAfter(mainMenuEpisode, forestLoadingEpisode);

        addEpisodeAfter(forestLoadingEpisode, new MainMenuEpisode() {
            @Override
            public boolean isAccessible(EpisodeEndState state) {
                return state.getEndStateCode().equals(EpisodeEndStateCode.EPISODE_FINISHED_FAILURE);
            }
        });

        addEpisodeAfter(forestLoadingEpisode, new CockpitEpisode());

//
//        Episode mapEpisode = new MapEpisode();
//        addEpisodeAfter(mainMenuEpisode, mapEpisode);

//        addEpisodeAfter(roomEpisode, forestLoadingEpisode);
//
//        Episode forestEpisode = new ForestEpisode();
//        addEpisodeAfter(forestLoadingEpisode, forestEpisode);
//
//        addEpisodeAfter(forestEpisode, new CockpitEpisode());

//        setFirstEpisode(forestEpisode);


//        addEpisodeAfterCurrent(roomEpisode);
//        addEpisodeAfterCurrent(cockpitEpisode);

//
//
//        Episode cockpitEpisode = new CockpitEpisode();
//        addEpisodeAfter(forestEpisode, cockpitEpisode);

    }

    @Override
    protected Episode getNextEpisode(EpisodeEndState state) {
        String endStateCode = state.getEndStateCode();
        switch (endStateCode) {
            case EpisodeEndStateCode.CALCULATOR_STARTED:
                addTemporaryEpisode(new CalculatorEpisode());
                break;
            case EpisodeEndStateCode.CONTACT_SCREEN_EPISODE_STARTED:
                addTemporaryEpisode(new ContactScreenEpisode());
                break;
            case EpisodeEndStateCode.MAP_EPISODE_STARTED:
                addTemporaryEpisode(new MapEpisode());
                break;
            case EpisodeEndStateCode.SPACESHIP_CHARGER_EPISODE_STARTED:
                addTemporaryEpisode(new SpaceshipChargerEpisode());
                break;
            case EpisodeEndStateCode.SIMPLE_TIMED_IMAGE_EPISODE:
                addTemporaryEpisode(new SimpleTimedImageEpisode());
                break;
            case EpisodeEndStateCode.TEMP_EPISODE_FINISHED:
                break;
            case EpisodeEndStateCode.PREVIOUS_EPISODE:
                return lastEpisode;
            case EpisodeEndStateCode.APP_QUIT:
                return  null;
        }
        return super.getNextEpisode(state);
    }
}
