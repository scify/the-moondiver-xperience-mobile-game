package org.scify.moonwalker.app.game.scenarios;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.GameInfo;
import org.scify.moonwalker.app.game.Location;
import org.scify.moonwalker.app.game.LocationController;
import org.scify.moonwalker.app.game.episodes.*;
import org.scify.moonwalker.app.game.rules.episodes.LocationEpisodeRules;

import java.util.List;
import java.util.Random;

public class MoonWalkerScenario extends Scenario {

    public static final String NEXT_LOCATION = "NEXT_LOCATION";

    public MoonWalkerScenario() {
        if (true) {
            createBasicScenario(0);
        }
        else {
            setFirstEpisode(createTestingScenario());
        }
    }



    @Override
    protected Episode getNextEpisode(EpisodeEndState state) {
        String endStateCode = state.getEndStateCode();
        Episode newCurrentEpisode = null;
        switch (endStateCode) {
            case EpisodeEndStateCode.SCENARIO_LOAD:
                GameInfo gameInfo = GameInfo.getInstance();
                gameInfo.load();
                return createBasicScenario(gameInfo.getMainEpisodeCounter());
            case EpisodeEndStateCode.SCENARIO_NEEDS_RESTART:
                GameInfo.getInstance().reset();
                return createBasicScenario(0);
            case EpisodeEndStateCode.CONTACT_SCREEN_EPISODE_STARTED:
                if (currentEpisode instanceof CockpitEpisode)
                    newCurrentEpisode = new CockpitEpisode();
                addTemporaryEpisode(new ContactScreenEpisode(), newCurrentEpisode);
                break;
            case EpisodeEndStateCode.SPACESHIP_CHARGER_EPISODE_STARTED:
                if (currentEpisode instanceof CockpitEpisode)
                    newCurrentEpisode = new CockpitEpisode();
                addTemporaryEpisode(new ChargeEpisode(), newCurrentEpisode);
                break;
            case EpisodeEndStateCode.SELECT_LOCATION_ON_MAP_EPISODE:
                if (currentEpisode instanceof CockpitEpisode)
                    newCurrentEpisode = new CockpitEpisode();
                addTemporaryEpisode(new MapEpisode(false), newCurrentEpisode);
                break;
            case EpisodeEndStateCode.TRAVEL_ON_MAP_EPISODE:
                if (currentEpisode instanceof CockpitEpisode)
                    newCurrentEpisode = new CockpitEpisode();
                addTemporaryEpisode(new MapEpisode(true), newCurrentEpisode);
                break;
            case EpisodeEndStateCode.SPACESHIP_INVENTORY_EPISODE_STARTED:
                if (currentEpisode instanceof CockpitEpisode)
                    newCurrentEpisode = new CockpitEpisode();
                addTemporaryEpisode(new SpaceshipInventoryEpisode(), newCurrentEpisode);
                break;
            case EpisodeEndStateCode.LOCATION_EPISODE_STARTED:
                if (currentEpisode instanceof CockpitEpisode)
                    newCurrentEpisode = new CockpitEpisode();
                GameState gameState = state.getGameState();
                Location target = (Location) gameState.getAdditionalDataEntry(NEXT_LOCATION);
                addTemporaryEpisode(new LocationEpisode(new LocationEpisodeRules(target)), newCurrentEpisode);
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

    protected Episode createBasicScenario (int episodeCounter) {
        clear();
        Episode ret;
        Episode splash = new SplashScreenEpisode();
        setFirstEpisode(splash);
        ret = splash;
        Episode mainMenu = new MainMenuEpisode();
        if (episodeCounter == 1) {
            setFirstEpisode(mainMenu);
            ret = mainMenu;
        }
        addEpisodeAfter(splash, mainMenu);
        Episode intro = new IntroEpisode();
        if (episodeCounter == 2) {
            setFirstEpisode(intro);
            ret = intro;
        }
        addEpisodeAfter(mainMenu, intro);
        Episode room = new RoomEpisode();
        if (episodeCounter == 3) {
            setFirstEpisode(room);
            ret = room;
        }
        addEpisodeAfter(intro, room);
        Episode forest = new ForestEpisode();
        if (episodeCounter == 4) {
            setFirstEpisode(forest);
            ret = forest;
        }
        addEpisodeAfter(room, forest);
        Episode cockpit = new CockpitEpisode();
        if (episodeCounter == 5) {
            setFirstEpisode(cockpit);
            ret = cockpit;
        }
        addEpisodeAfter(forest, cockpit);
        Episode moonLanding = new MoonLandingEpisode();
        if (episodeCounter == 6) {
            setFirstEpisode(moonLanding);
            ret = moonLanding;
        }
        addEpisodeAfter(cockpit, moonLanding);
        Episode dreamingRoomEpisode = new DreamingRoomEpisode();
        if (episodeCounter == 7) {
            setFirstEpisode(dreamingRoomEpisode);
            ret = dreamingRoomEpisode;
        }
        addEpisodeAfter(moonLanding, dreamingRoomEpisode);
        Episode creditsEpisode = new CreditsEpisode();
        addEpisodeAfter(dreamingRoomEpisode, creditsEpisode);
        return ret;
    }

    protected Episode createTestingScenario() {
        if (false) {
            return new ForestEpisode();
        } else {
            return new CreditsEpisode();
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

        info.setNextLocation(llLocations.get(iTo));
        info.setNextTravelPercentagePossible(75);
        return mapEpisode;
    }
}
