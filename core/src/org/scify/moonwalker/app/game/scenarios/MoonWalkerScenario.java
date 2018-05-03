package org.scify.moonwalker.app.game.scenarios;

import org.scify.engine.Episode;
import org.scify.engine.EpisodeEndState;
import org.scify.engine.EpisodeEndStateCode;
import org.scify.engine.Scenario;
import org.scify.moonwalker.app.game.GameInfo;
import org.scify.moonwalker.app.game.Location;
import org.scify.moonwalker.app.game.LocationController;
import org.scify.moonwalker.app.game.SelectedPlayer;
import org.scify.moonwalker.app.game.episodes.*;

import java.util.List;
import java.util.Random;

public class MoonWalkerScenario extends Scenario {

    public MoonWalkerScenario() {
        if (true) {
            createBasicScenario();
        }
        else {
            createTestingScenario();
        }
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
                addTemporaryEpisode(new MapEpisode(false));
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
        if (true) {
//            return getCockpitEpisode();
            return getMapEpisode();
        } else {
            return getPlaygroundEpisode();
        }

    }

    private Episode getKnightEpisode() {
        Episode knight = new KnightRaceEpisode();
        setFirstEpisode(knight);
        return knight;
    }

    private Episode getCockpitEpisode() {
        Episode cockpit = new CockpitEpisode();
        setFirstEpisode(cockpit);
        return cockpit;
    }

    private Episode getPlaygroundEpisode() {
        Episode playground = new EffectPlaygroundEpisode();
        setFirstEpisode(playground);
        return playground;
    }

    private Episode getMapEpisode() {
        Random rRnd = new Random();

        Episode mapEpisode = new MapEpisode(false);
        setFirstEpisode(mapEpisode);
        GameInfo info = GameInfo.getInstance();
        LocationController lc = LocationController.getInstance();
        int iSize = lc.getLocations().size();
        List<Location> llLocations = lc.getLocations();
        int iFrom = rRnd.nextInt(iSize);
        info.setCurrentLocation(llLocations.get(iFrom));
        int iTo = rRnd.nextInt(iSize);
        while (iTo == iFrom) {
            iTo = rRnd.nextInt(iSize);
        }

        info.setNextAllowedLocation(llLocations.get(iTo));
        return mapEpisode;
    }
}
