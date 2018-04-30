package org.scify.moonwalker.app.game.scenarios;

import org.scify.engine.Episode;
import org.scify.engine.EpisodeEndState;
import org.scify.engine.EpisodeEndStateCode;
import org.scify.engine.Scenario;
import org.scify.moonwalker.app.game.GameInfo;
import org.scify.moonwalker.app.game.LocationController;
import org.scify.moonwalker.app.game.SelectedPlayer;
import org.scify.moonwalker.app.game.episodes.*;

public class MoonWalkerScenario extends Scenario {

    public MoonWalkerScenario() {
        createBasicScenario();
//        createTestingScenario();
    }

    @Override
    protected Episode getNextEpisode(EpisodeEndState state) {
        String endStateCode = state.getEndStateCode();
        switch (endStateCode) {
            case EpisodeEndStateCode.SCENARIO_NEEDS_RESTART:
                clear();
                GameInfo.getInstance().setSelectedPlayer(SelectedPlayer.unset);
                return createBasicScenario();
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

    protected Episode createBasicScenario () {
        Episode mainMenu = new MainMenuEpisode();
        setFirstEpisode(mainMenu);
        Episode room = new RoomEpisode();
        addEpisodeAfter(mainMenu, room);
        Episode forest = new ForestEpisode();
        addEpisodeAfter(room, forest);
        Episode cockpit = new CockpitEpisode();
        addEpisodeAfter(forest, cockpit);
        return mainMenu;
    }

    protected Episode createTestingScenario() {
        Episode mapEpisode = new MapEpisode();
        setFirstEpisode(mapEpisode);
        GameInfo info = GameInfo.getInstance();
        LocationController lc = LocationController.getInstance();
        info.setCurrentLocation(lc.getLocations().get(0));
        info.setNextAllowedLocation(lc.getLocations().get(1));

        return mapEpisode;
    }
}
