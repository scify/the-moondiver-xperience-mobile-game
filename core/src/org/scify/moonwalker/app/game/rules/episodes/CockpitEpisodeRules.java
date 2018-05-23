package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.moonwalker.app.game.Location;
import org.scify.moonwalker.app.game.LocationController;
import org.scify.moonwalker.app.ui.renderables.CockpitRenderable;

import java.util.ArrayList;
import java.util.Date;

import static org.scify.engine.EpisodeEndStateCode.*;
import static org.scify.moonwalker.app.game.rules.episodes.MapEpisodeRules.ORIGIN_MIDDLE_OF_NOWHERE;

public class CockpitEpisodeRules extends FadingEpisodeRules<CockpitRenderable> {

    public static final String COCKPIT_ID = "cockpit";
    public static final String TOGGLE_BUTTON = "TOGGLE_BUTTON";
    protected LocationController locationController;

    protected boolean buttonsEnabled;
    protected boolean contactClickable;
    protected boolean mapClickable;
    protected boolean launchClickable;
    protected boolean travelClickable;
    protected boolean chargeClickable;
    protected boolean inventoryClickable;
    protected boolean showArrivalConversation;
    protected Location episodeLocation;

    public CockpitEpisodeRules() {
        super();
        buttonsEnabled = false;
        contactClickable = false;
        launchClickable = false;
        travelClickable = false;
        mapClickable = true;
        chargeClickable = false;
        inventoryClickable = false;
    }

    public void init() {
        showArrivalConversation = false;
        locationController = new LocationController();
    }

    @Override
    public void episodeStartedEvents(final GameState gameState) {
        if (!isEpisodeStarted(gameState)) {
            init();
            // if last travel was successful
            if(hasSpaceshipJustArrivedAtLocation()) {
                spaceshipHasJustArrived();
            } // if last travel was unsuccessful
            else if (hasSpaceshipFailedToArriveAtLocation()){
                episodeLocation = locationController.getNowhereLocation(ORIGIN_MIDDLE_OF_NOWHERE, 0,0);
            } else {
                episodeLocation = gameInfo.getCurrentLocation();
                locationController.resetSelectFirstMiddleOfNowhere();
            }
            renderable = new CockpitRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), COCKPIT_ID, episodeLocation);
            renderable.setZIndex(1);
            setCockpitFieldValues();
            addAfterEffectEventsForEpisodeRenderable(gameState);
            setOutsideBackground(episodeLocation);
            addAudioGameEvents(gameState);
            gameState.addRenderables(new ArrayList<>(renderable.getAllRenderables()));
            gameState.addRenderable(renderable);
            super.episodeStartedEvents(gameState);
        }
    }

    protected void spaceshipHasJustArrived() {
        // update current location in gameInfo
        gameInfo.setCurrentLocation(gameInfo.getNextLocation());
        // set the next location to null, so the user has to select it from the map
        gameInfo.setNextLocation(null);
        // todo what to do in case we landed in paris (so the next location does not exist)?
        gameInfo.setNextAllowedLocation(locationController.getLocationAfter(gameInfo.getCurrentLocation()));
        // reset the travel percentages in gameInfo
        gameInfo.resetTravelState();
        showArrivalConversation = true;
        episodeLocation = gameInfo.getCurrentLocation();
    }

    protected void addAfterEffectEventsForEpisodeRenderable(final GameState gameState) {
        renderable.addAfterFadeIn(new Runnable() {
            @Override
            public void run() {
                if (gameInfo.getContactRequestFlag()) {
                    gameState.addGameEvent(new GameEvent(TOGGLE_BUTTON, renderable.getContactLightedButton(), new Date().getTime() + 500, false, this));
                    renderable.toogleButtonLight(renderable.getContactLightedButton());
                    contactClickable = true;
                } else if (gameInfo.getMapRequestFlag()) {
                    gameState.addGameEvent(new GameEvent(TOGGLE_BUTTON, renderable.getMapLightedButton(), new Date().getTime() + 500, false, this));
                    renderable.toogleButtonLight(renderable.getMapLightedButton());
                    mapClickable = true;
                } else if (gameInfo.getChargeRequestFlag()) {
                    gameState.addGameEvent(new GameEvent(TOGGLE_BUTTON, renderable.getChargeLightedButton(), new Date().getTime() + 500, false, this));
                    renderable.toogleButtonLight(renderable.getChargeLightedButton());
                    chargeClickable = true;
                } else {
                    inventoryClickable = true;
                    if (gameInfo.getNextLocation() != null)
                        chargeClickable = true;
                    if (gameInfo.getRemainingEnergy() > 0 && gameInfo.getNextLocation() != null)
                        travelClickable = true;
                }
                buttonsEnabled = true;
                if(showArrivalConversation)
                    createConversation(gameState, gameInfo.getCurrentLocation().getConversationArrivalFilePath(), renderable.CONVERSATION_BG_IMG_PATH);
            }
        });
    }

    protected void addAudioGameEvents(final GameState gameState) {
        gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_LOAD_UI, renderable.LOW_ENERGY_AUDIO_PATH));
        gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_LOAD_UI, renderable.TAKE_OFF_AUDIO_PATH));
        if(!gameState.eventsQueueContainsEvent(GAME_EVENT_AUDIO_START_LOOP_UI))
            gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_LOOP_UI, renderable.BG_DEFAULT_AUDIO_PATH));
    }

    protected boolean hasSpaceshipJustArrivedAtLocation() {
        return gameInfo.getNextTravelPercentagePossible() == 100.0 && gameInfo.isAfterTravel();
    }

    protected boolean hasSpaceshipFailedToArriveAtLocation() {
        return gameInfo.getNextTravelPercentagePossible() < 100.0 && gameInfo.isAfterTravel();
    }

    protected void setOutsideBackground(Location location) {
        if (gameInfo.isAtForest()) {
            renderable.setOutsideBackground(renderable.FOREST_BG_IMG_PATH);
        } else {
            renderable.setOutsideBackground(location.getCockpitBG());
        }
    }

    protected void goToEpisode(GameState gameState, GameEvent gameEvent) {
        buttonsEnabled = false;
        gameState.removeGameEventsWithType(TOGGLE_BUTTON);
        gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.CLICK_AUDIO_PATH));
        renderable.turnOffLightOffAllButtons();
        renderable.fadeoutOutsideBackground();
        gameState.addGameEvent(gameEvent);
        episodeEndedEvents(gameState);
    }

    @Override
    protected void handleUserAction(GameState gameState, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case UserActionCode.CONTACT_SCREEN_EPISODE:
                if (buttonsEnabled && contactClickable)
                    goToEpisode(gameState, new GameEvent(CONTACT_SCREEN_EPISODE_STARTED, null, this));
                else
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.WRONG_BUTTON_AUDIO_PATH));
                break;
            case UserActionCode.MAP_EPISODE:
                if (buttonsEnabled && !contactClickable && mapClickable) {
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_LOAD_UI, renderable.LOCATION_SELECTED_AUDIO_PATH));
                    goToEpisode(gameState, new GameEvent(SELECT_LOCATION_ON_MAP_EPISODE, null, this));
                    renderable.turnOffButtonLight(renderable.getMapLightedButton());
                }
                else
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.WRONG_BUTTON_AUDIO_PATH));
                break;
            case UserActionCode.CHARGE_SPACESHIP_EPISODE:
                if (buttonsEnabled && !contactClickable && chargeClickable) {
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_LOAD_UI, renderable.POWER_UP_AUDIO_PATH));
                    goToEpisode(gameState, new GameEvent(SPACESHIP_CHARGER_EPISODE_STARTED, null, this));
                }
                else
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.WRONG_BUTTON_AUDIO_PATH));
                break;
            case UserActionCode.SPACESHIP_INVENTORY_EPISODE:
                if (buttonsEnabled && !contactClickable && inventoryClickable) {
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_LOAD_UI, renderable.ADD_ITEM_AUDIO_PATH));
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_LOAD_UI, renderable.UPGRADE_STATS_AUDIO_PATH));
                    goToEpisode(gameState, new GameEvent(SPACESHIP_INVENTORY_EPISODE_STARTED, null, this));
                } else
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.WRONG_BUTTON_AUDIO_PATH));
                break;
            case UserActionCode.TRAVEL:
                handleTravelClick(gameState);
                break;
            case UserActionCode.LAUNCH:
                if (buttonsEnabled && !contactClickable && launchClickable) {

                } else
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.WRONG_BUTTON_AUDIO_PATH));
                break;
            default:
                super.handleUserAction(gameState, userAction);
                break;
        }
    }

    protected void handleTravelClick(GameState gameState) {
        if (buttonsEnabled && !contactClickable && travelClickable) {
            gameInfo.setAtForest(false);
            gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_LOAD_UI, renderable.TRAVEL_AUDIO_PATH));
            gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.TAKE_OFF_AUDIO_PATH));
            calculateAndSetTravelPercentage();
            gameInfo.setAfterTravel(true);
            // end current episode and start map episode
            goToEpisode(gameState, new GameEvent(TRAVEL_ON_MAP_EPISODE, null, this));
        } else {
            if (gameInfo.getRemainingEnergy() == 0)
                gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.LOW_ENERGY_AUDIO_PATH));
            else
                gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.WRONG_BUTTON_AUDIO_PATH));
            if(gameInfo.getNextLocation() == null) {
                createConversation(gameState, "conversations/next_location_not_selected.json", renderable.CONVERSATION_BG_IMG_PATH);
                renderable.turnOnButtonLight(renderable.getMapLightedButton());
            }
        }
    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        EpisodeEndState endStateFromParent = super.determineEndState(currentState);
        if (endStateFromParent != null)
            return endStateFromParent;
        else if (currentState.eventsQueueContainsEventOwnedBy(CONTACT_SCREEN_EPISODE_STARTED, this))
            return new EpisodeEndState(CONTACT_SCREEN_EPISODE_STARTED, cleanUpGameState(currentState));
        else if (currentState.eventsQueueContainsEventOwnedBy(SELECT_LOCATION_ON_MAP_EPISODE, this))
            return new EpisodeEndState(SELECT_LOCATION_ON_MAP_EPISODE, cleanUpGameState(currentState));
        else if (currentState.eventsQueueContainsEventOwnedBy(SPACESHIP_CHARGER_EPISODE_STARTED, this))
            return new EpisodeEndState(SPACESHIP_CHARGER_EPISODE_STARTED, cleanUpGameState(currentState));
        else if (currentState.eventsQueueContainsEventOwnedBy(SPACESHIP_INVENTORY_EPISODE_STARTED, this))
            return new EpisodeEndState(SPACESHIP_INVENTORY_EPISODE_STARTED, cleanUpGameState(currentState));
        else if (currentState.eventsQueueContainsEventOwnedBy(TRAVEL_ON_MAP_EPISODE, this))
            return new EpisodeEndState(TRAVEL_ON_MAP_EPISODE, cleanUpGameState(currentState));
        return new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_FAILURE, cleanUpGameState(currentState));
    }

    protected void setCockpitFieldValues() {
        renderable.setRemainingEnergyValue(String.valueOf(gameInfo.getRemainingEnergy()));
        renderable.setMotorEfficiencyValue(String.valueOf(gameInfo.getMotorEfficiency()));
        renderable.setDaysLeftValue(gameInfo.getDaysLeftForDestination() + "");
        if (gameInfo.getNextLocation() != null) {
            System.out.println();
            int percentageTraveled = (int)((100 - gameInfo.getNextTravelPercentagePossible()) * gameInfo.getNextLocationDistance() / 100);
            renderable.setDestinationDistanceValue(percentageTraveled + "");
        }
        else{
            renderable.setDestinationDistanceValue("--");
        }
    }


    @Override
    public GameState getNextState(GameState gameState, UserAction userAction) {
        long timestamp = new Date().getTime();
        GameEvent buttonToggleEvent = gameState.getGameEventWithType(TOGGLE_BUTTON);
        if (buttonToggleEvent != null && timestamp > buttonToggleEvent.delay) {
            gameState.removeGameEventsWithType(TOGGLE_BUTTON);
            ActionButtonRenderable button = (ActionButtonRenderable) buttonToggleEvent.parameters;
            renderable.toogleButtonLight(button);
            gameState.addGameEvent(new GameEvent(TOGGLE_BUTTON, button, new Date().getTime() + 500, false, this));
        }
        return super.getNextState(gameState, userAction);
    }

    protected void calculateAndSetTravelPercentage() {
        // motor efficiency describes how many kilometers the spaceship
        int motorEfficiency = gameInfo.getMotorEfficiency();
        // get the destination (in kilometers) for the target location
        int destinationKm = gameInfo.getNextLocationDistance();
        // get the remaining energy units from gameInfo
        int remainingEnergy = gameInfo.getRemainingEnergy();
        double kilometersForEnergyUnits = motorEfficiency * remainingEnergy;
        double percentage = (kilometersForEnergyUnits / destinationKm) * 100;
        // if we have more energy than needed, set as full percentage
        System.out.println("percentage " + percentage);
        if(percentage > 100)
            percentage = 100.0;
        gameInfo.setRemainingEnergy(0);
        double newPercentagePossible = gameInfo.getNextTravelPercentagePossible() + percentage;
        if(newPercentagePossible > 100.0)
            newPercentagePossible = 100.0;
        gameInfo.setNextTravelPercentagePossible(newPercentagePossible);
    }
}
