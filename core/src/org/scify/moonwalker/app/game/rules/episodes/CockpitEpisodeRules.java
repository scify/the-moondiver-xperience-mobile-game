package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.moonwalker.app.game.Location;
import org.scify.moonwalker.app.game.LocationController;
import org.scify.moonwalker.app.game.rules.ConversationRules;
import org.scify.moonwalker.app.ui.renderables.CockpitRenderable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import static org.scify.engine.EpisodeEndStateCode.*;
import static org.scify.moonwalker.app.game.rules.ConversationRules.EVENT_SHOW_QUIZ_EPISODE;
import static org.scify.moonwalker.app.game.rules.episodes.MapEpisodeRules.ORIGIN_MIDDLE_OF_NOWHERE;
import static org.scify.moonwalker.app.game.scenarios.MoonWalkerScenario.NEXT_LOCATION;

public class CockpitEpisodeRules extends FadingEpisodeRules<CockpitRenderable> {

    public static final String COCKPIT_ID = "cockpit";
    public static final String TOGGLE_BUTTON = "TOGGLE_BUTTON";
    public static final int MAX_INVENTORY_ITEMS = 7;
    protected LocationController locationController;

    protected boolean buttonsEnabled;
    protected boolean showArrivalConversation;
    protected Location episodeLocation;

    public CockpitEpisodeRules() {
        super();
        buttonsEnabled = false;
    }

    public void init() {
        showArrivalConversation = false;
        locationController = LocationController.getInstance();
    }

    @Override
    public void episodeStartedEvents(final GameState gameState) {
        if (!isEpisodeStarted(gameState)) {
            init();
            renderable = new CockpitRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), COCKPIT_ID);
            // if last travel was successful
            if (hasSpaceshipJustArrivedAtLocation()) {
                gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI, renderable.BG_NOWHERE1_AUDIO_PATH));
                gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI, renderable.BG_NOWHERE2_AUDIO_PATH));
                locationController.resetSelectFirstMiddleOfNowhere();
                spaceshipHasJustArrived();
            } // if last travel was unsuccessful
            else if (isAtNowhereLocation()) {
                episodeLocation = locationController.getNowhereLocation(ORIGIN_MIDDLE_OF_NOWHERE, 0, 0);
                if (hasJustFailedToArriveAtLocation()) {
                    locationController.toggleSelectFirstMiddleOfNowhere();
                    episodeLocation = locationController.getNowhereLocation(ORIGIN_MIDDLE_OF_NOWHERE, 0, 0);
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI, renderable.BG_NOWHERE1_AUDIO_PATH));
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI, renderable.BG_NOWHERE2_AUDIO_PATH));
                    if (gameInfo.getRemainingEnergy() == 0)
                        gameInfo.setChargeRequestFlag();

                    if (locationController.isSelectFirstMiddleOfNowhere())
                        gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_LOOP_UI, renderable.BG_NOWHERE1_AUDIO_PATH));
                    else
                        gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_LOOP_UI, renderable.BG_NOWHERE2_AUDIO_PATH));
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI, renderable.BG_DEFAULT_AUDIO_PATH));
                    gameInfo.setAfterTravel(false);
                }
            } else {
                episodeLocation = gameInfo.getCurrentLocation();
            }
            renderable.initSubRenderables(episodeLocation);
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
        Location currentLocation = gameInfo.setCurrentLocation(gameInfo.getNextLocation());
        // set the next location to null, so the user has to select it from the map
        gameInfo.setNextLocation(null);
        initNextLocation(currentLocation);
    }

    protected void initNextLocation(Location currentLocation) {
        Location nextAllowedLocation = locationController.getLocationAfter(currentLocation);
        if (nextAllowedLocation != null)
            gameInfo.setNextAllowedLocation(nextAllowedLocation);
        // reset the travel percentages in gameInfo
        gameInfo.resetTravelState();
        showArrivalConversation = true;
        episodeLocation = gameInfo.getCurrentLocation();
    }

    protected void addAfterEffectEventsForEpisodeRenderable(final GameState gameState) {
        renderable.addAfterFadeIn(new Runnable() {
            @Override
            public void run() {
                if (gameInfo.isContactRequestFlag()) {
                    gameState.addGameEvent(new GameEvent(TOGGLE_BUTTON, renderable.getContactLightedButton(), new Date().getTime() + 500, false, this));
                    renderable.toogleButtonLight(renderable.getContactLightedButton());
                } else if (gameInfo.isInventoryRequestFlag()) {
                    gameInfo.setAfterLocationQuizEpisode(false);
                    gameState.addGameEvent(new GameEvent(TOGGLE_BUTTON, renderable.getSpaceshipInventoryLightedButton(), new Date().getTime() + 500, false, this));
                    renderable.toogleButtonLight(renderable.getSpaceshipInventoryLightedButton());
                } else if (gameInfo.isAfterLocationQuizEpisode()) {
                    if (!gameInfo.isLastQuizSuccessFull()) {
                        showArrivalConversation = true;
                    }
                    gameInfo.setAfterLocationQuizEpisode(false);
                } else if (gameInfo.isMapRequestFlag()) {
                    gameState.addGameEvent(new GameEvent(TOGGLE_BUTTON, renderable.getMapLightedButton(), new Date().getTime() + 500, false, this));
                    renderable.toogleButtonLight(renderable.getMapLightedButton());
                } else if (gameInfo.isChargeRequestFlag()) {
                    gameState.addGameEvent(new GameEvent(TOGGLE_BUTTON, renderable.getChargeLightedButton(), new Date().getTime() + 500, false, this));
                    renderable.toogleButtonLight(renderable.getChargeLightedButton());
                } else if (gameInfo.isTravelRequestFlag()) {
                    gameState.addGameEvent(new GameEvent(TOGGLE_BUTTON, renderable.getTravelLightedButton(), new Date().getTime() + 500, false, this));
                    renderable.toogleButtonLight(renderable.getTravelLightedButton());
                } else if (gameInfo.isLaunchRequestFlag()) {
                    gameState.addGameEvent(new GameEvent(TOGGLE_BUTTON, renderable.getLaunchLightedButton(), new Date().getTime() + 500, false, this));
                    renderable.toogleButtonLight(renderable.getLaunchLightedButton());
                }
                if (showArrivalConversation)
                    createConversation(gameState, gameInfo.getCurrentLocation().getConversationArrivalFilePath(), renderable.CONVERSATION_BG_IMG_PATH);
                else
                    buttonsEnabled = true;
            }
        });
    }

    protected void addAudioGameEvents(final GameState gameState) {
        gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_LOAD_UI, renderable.LOW_ENERGY_AUDIO_PATH));
        gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_LOAD_UI, renderable.TAKE_OFF_AUDIO_PATH));
        if (!gameInfo.isBackGroundMusicPlaying()) {
            gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_LOOP_UI, renderable.BG_DEFAULT_AUDIO_PATH));
            gameInfo.setBackGroundMusicPlaying(true);
        }
    }

    protected boolean hasSpaceshipJustArrivedAtLocation() {
        return gameInfo.getNextTravelPercentagePossible() == 100.0 && gameInfo.isAfterTravel();
    }

    protected boolean hasJustFailedToArriveAtLocation() {
        boolean isAtNowhere = isAtNowhereLocation();
        boolean isAfterTravel = gameInfo.isAfterTravel();
        boolean ret = isAfterTravel && isAtNowhere;
        return  ret;
    }

    protected boolean isAtNowhereLocation() {
        boolean ret = gameInfo.getNextTravelPercentagePossible() < 100.0 && gameInfo.getNextTravelPercentagePossible() > 0;
        return ret;
    }

    protected void setOutsideBackground(Location location) {
        if (gameInfo.isAtForest()) {
            renderable.setOutsideBackground(renderable.FOREST_BG_IMG_PATH);
        } else {
            renderable.setOutsideBackground(location.getCockpitBG());
        }
    }

    @Override
    protected void onExitConversationOrder(GameState gameState, ConversationLine lineEntered) {
        Set<String> eventTrigger;
        if (gameState.eventsQueueContainsEvent(ConversationRules.ON_EXIT_CONVERSATION_ORDER_TRIGGER_EVENT)) {
            eventTrigger = (Set<String>) gameState.getGameEventWithType(ConversationRules.ON_EXIT_CONVERSATION_ORDER_TRIGGER_EVENT).parameters;
            if (eventTrigger.contains(EVENT_SHOW_QUIZ_EPISODE)) {
                gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI, renderable.BG_DEFAULT_AUDIO_PATH));
                gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI, renderable.BG_NOWHERE1_AUDIO_PATH));
                gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI, renderable.BG_NOWHERE2_AUDIO_PATH));
                gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_DISPOSE_UI, renderable.BG_NOWHERE1_AUDIO_PATH));
                gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_DISPOSE_UI, renderable.BG_NOWHERE2_AUDIO_PATH));
                gameInfo.setBackGroundMusicPlaying(false);
                gameState.setAdditionalDataEntry(NEXT_LOCATION, gameInfo.getCurrentLocation());
                goToEpisode(gameState, new GameEvent(LOCATION_EPISODE_STARTED, null, this));
            }
        }

        super.onEnterConversationOrder(gameState, lineEntered);
    }

    protected void goToEpisode(GameState gameState, GameEvent gameEvent) {
        buttonsEnabled = false;
        gameState.removeGameEventsWithType(TOGGLE_BUTTON);
        gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.CLICK_AUDIO_PATH));
        if (gameInfo.getInventoryItemsCounter() == MAX_INVENTORY_ITEMS) {
            gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.MOON_TAKE_OFF_AUDIO_PATH));
        }
        renderable.turnOffLightOffAllButtons();
        renderable.fadeoutOutsideBackground();
        gameState.addGameEvent(gameEvent);
        episodeEndedEvents(gameState);
    }

    @Override
    protected void handleUserAction(GameState gameState, UserAction userAction) {
        boolean anyFlagOn = gameInfo.isAnyFlagOn();
        switch (userAction.getActionCode()) {
            case UserActionCode.CONTACT_SCREEN_EPISODE:
                if (buttonsEnabled && gameInfo.isContactRequestFlag())
                    goToEpisode(gameState, new GameEvent(CONTACT_SCREEN_EPISODE_STARTED, null, this));
                else
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.WRONG_BUTTON_AUDIO_PATH));
                break;
            case UserActionCode.MAP_EPISODE:
                if (buttonsEnabled && (gameInfo.isMapRequestFlag() || (gameInfo.getNextLocation() == null && !gameInfo.isAnyFlagOn()))) {
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_LOAD_UI, renderable.LOCATION_SELECTED_AUDIO_PATH));
                    goToEpisode(gameState, new GameEvent(SELECT_LOCATION_ON_MAP_EPISODE, null, this));
                    renderable.turnOffButtonLight(renderable.getMapLightedButton());
                } else
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.WRONG_BUTTON_AUDIO_PATH));
                break;
            case UserActionCode.CHARGE_SPACESHIP_EPISODE:
                if (buttonsEnabled && (!anyFlagOn || (anyFlagOn && gameInfo.isChargeRequestFlag())) && gameInfo.getNextLocation() != null) {
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_LOAD_UI, renderable.POWER_UP_AUDIO_PATH));
                    goToEpisode(gameState, new GameEvent(SPACESHIP_CHARGER_EPISODE_STARTED, null, this));
                } else
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.WRONG_BUTTON_AUDIO_PATH));
                break;
            case UserActionCode.SPACESHIP_INVENTORY_EPISODE:
                if (buttonsEnabled && (!anyFlagOn || (anyFlagOn && gameInfo.isInventoryRequestFlag()))) {
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
                if (buttonsEnabled && gameInfo.isLaunchRequestFlag()) {
                    goToEpisode(gameState, new GameEvent(EPISODE_FINISHED_SUCCESS, null, this));
                } else
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.WRONG_BUTTON_AUDIO_PATH));
                break;
            default:
                super.handleUserAction(gameState, userAction);
                break;
        }
    }

    protected boolean isTravelClickAble() {
        if (!buttonsEnabled)
            return false;
        if (gameInfo.isTravelRequestFlag())
            return true;
        if (gameInfo.isAnyFlagOn())
            return false;
        if (gameInfo.getRemainingEnergy() == 0)
            return false;
        if (gameInfo.getNextLocation() == null)
            return false;
        return true;
    }

    protected void handleTravelClick(GameState gameState) {
        if (isTravelClickAble()) {
            gameInfo.setAtForest(false);
            gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_LOAD_UI, renderable.TRAVEL_AUDIO_PATH));
            gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.TAKE_OFF_AUDIO_PATH));
            calculateAndSetTravelPercentage(gameState);
            gameInfo.setAfterTravel(true);
            // end current episode and start map episode
            goToEpisode(gameState, new GameEvent(TRAVEL_ON_MAP_EPISODE, null, this));
        } else {
            if (gameInfo.getRemainingEnergy() == 0 && !gameInfo.isTutorialMode())
                gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.LOW_ENERGY_AUDIO_PATH));
            else
                gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.WRONG_BUTTON_AUDIO_PATH));
            if (gameInfo.getNextLocation() == null && !gameInfo.isTutorialMode() && !gameInfo.isAnyFlagOn()) {
                createConversation(gameState, "conversations/next_location_not_selected.json", renderable.CONVERSATION_BG_IMG_PATH);
                if (!gameState.eventsQueueContainsEvent(TOGGLE_BUTTON)) {
                    gameInfo.setMapRequestFlag();
                    gameState.addGameEvent(new GameEvent(TOGGLE_BUTTON, renderable.getMapLightedButton(), new Date().getTime() + 500, false, this));
                    renderable.toogleButtonLight(renderable.getMapLightedButton());
                }
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
        else if (currentState.eventsQueueContainsEventOwnedBy(LOCATION_EPISODE_STARTED, this))
            return new EpisodeEndState(LOCATION_EPISODE_STARTED, cleanUpGameState(currentState));
        return new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_FAILURE, cleanUpGameState(currentState));
    }

    protected void setCockpitFieldValues() {
        renderable.setRemainingEnergyValue(String.valueOf(gameInfo.getRemainingEnergy()));
        renderable.setMotorEfficiencyValue(String.valueOf(gameInfo.getMotorEfficiency()));
        renderable.setDaysLeftValue(gameInfo.getDaysLeftForDestination() + "");
        if (gameInfo.getNextLocation() != null) {
            System.out.println();
            int percentageTraveled = (int) ((100 - gameInfo.getNextTravelPercentagePossible()) * gameInfo.getNextLocationDistance() / 100);
            renderable.setDestinationDistanceValue(percentageTraveled + "");
        } else {
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

    protected void calculateAndSetTravelPercentage(GameState gameState) {
        // motor efficiency describes how many kilometers the spaceship
        int motorEfficiency = gameInfo.getMotorEfficiency();
        // get the destination (in kilometers) for the target location
        int destinationKm = gameInfo.getNextLocationDistance();
        // get the remaining energy units from gameInfo
        int remainingEnergy = gameInfo.getRemainingEnergy();
        double kilometersForEnergyUnits = motorEfficiency * remainingEnergy;
        double percentage = (kilometersForEnergyUnits / destinationKm) * 100;
        // if we have more energy than needed, set as full percentage
        gameInfo.resetFlags();
        if (percentage > 100)
            percentage = 100.0;

        if (percentage < 100) {
            gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_LOAD_UI, renderable.BG_NOWHERE1_AUDIO_PATH));
            gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_LOAD_UI, renderable.BG_NOWHERE2_AUDIO_PATH));
        }
        renderable.addAfterFadeOut(new Runnable() {
            @Override
            public void run() {
                gameInfo.setRemainingEnergy(0);
            }
        });
        double newPercentagePossible = gameInfo.getNextTravelPercentagePossible() + percentage;
        if (newPercentagePossible > 100.0)
            newPercentagePossible = 100.0;
        gameInfo.setNextTravelPercentagePossible(newPercentagePossible);
    }
}
