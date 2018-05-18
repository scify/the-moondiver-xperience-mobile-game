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
            /*case EpisodeEndStateCode.GAME_EVENT_CALCULATOR_STARTED:
                addTemporaryEpisode(new CalculatorEpisode());
                break;*/
            case EpisodeEndStateCode.CONTACT_SCREEN_EPISODE_STARTED:
                Episode newCurrentEpisode = null;
                if (currentEpisode instanceof CockpitEpisode)
                    newCurrentEpisode = new CockpitEpisode();
                addTemporaryEpisode(new ContactScreenEpisode(), newCurrentEpisode);
                break;
            case EpisodeEndStateCode.SELECT_LOCATION_ON_MAP_EPISODE:
                addTemporaryEpisode(new MapEpisode(false), new CockpitEpisode());
                break;
            case EpisodeEndStateCode.TRAVEL_ON_MAP_EPISODE:
                addTemporaryEpisode(new MapEpisode(true), new CockpitEpisode());
                break;
            case EpisodeEndStateCode.SPACESHIP_CHARGER_EPISODE_STARTED:
                addTemporaryEpisode(new ChargeEpisode(), new CockpitEpisode());
                break;
            case EpisodeEndStateCode.SPACESHIP_INVENTORY_EPISODE_STARTED:
                addTemporaryEpisode(new SpaceshipInventoryEpisode(), new CockpitEpisode());
                break;
            /*case EpisodeEndStateCode.SIMPLE_TIMED_IMAGE_EPISODE:
                addTemporaryEpisode(new SimpleTimedImageEpisode());
                break;*/
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
        /*Episode mainMenu = new MainMenuEpisode();
        setFirstEpisode(mainMenu);
        Episode room = new RoomEpisode();
        addEpisodeAfter(mainMenu, room);
        Episode forest = new ForestEpisode();
        addEpisodeAfter(room, forest);*/
        Episode cockpit = new CockpitEpisode();
        setFirstEpisode(cockpit);
        //addEpisodeAfter(forest, cockpit);
        //addEpisodeAfter(mainMenu, cockpit);
        return cockpit;
    }

    protected Episode createTestingScenario() {
        if (true) {
            return getLocationEpisode(new MadridEpisode());
        } else {
            return getPlaygroundEpisode();
        }
    }

    private Episode getChargeEpisode() {
        Episode charger = new ChargeEpisode();
        setFirstEpisode(charger);
        return charger;
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

    private Episode getLocationEpisode(LocationEpisode episode) {
        setFirstEpisode(episode);
        return episode;
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
        info.setPreviousTravelPercentageComplete(25);
        info.setNextTravelPercentagePossible(75);
        return mapEpisode;
    }
}
