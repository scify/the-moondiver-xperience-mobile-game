package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.moonwalker.app.game.LocationController;
import org.scify.moonwalker.app.ui.renderables.CockpitRenderable;

import java.util.ArrayList;
import java.util.Date;

import static org.scify.engine.EpisodeEndStateCode.*;

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

    public CockpitEpisodeRules() {
        super();
        buttonsEnabled = false;
        contactClickable = false;
        launchClickable = false;
        travelClickable = false;
        mapClickable = false;
        chargeClickable = false;
        inventoryClickable = true;
    }

    @Override
    public void episodeStartedEvents(final GameState gameState) {
        if (!isEpisodeStarted(gameState)) {
            locationController = new LocationController();
            renderable = new CockpitRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), COCKPIT_ID);
            renderable.setZIndex(1);
            setCockpitFieldValues();
            renderable.addAfterFadeIn(new Runnable() {
                @Override
                public void run() {
                    /*if (gameInfo.getContactRequestFlag()) {
                        gameState.addGameEvent(new GameEvent(TOGGLE_BUTTON, renderable.getContactLightedButton(), new Date().getTime() + 500, false, this));
                        renderable.toogleButtonLight(renderable.getContactLightedButton());
                        contactClickable = true;
                    } else if (gameInfo.getMapRequestFlag()) {
                        gameState.addGameEvent(new GameEvent(TOGGLE_BUTTON, renderable.getMapLightedButton(), new Date().getTime() + 500, false, this));
                        renderable.toogleButtonLight(renderable.getMapLightedButton());
                        mapClickable = true;
                    }*/
                    buttonsEnabled = true;
                }
            });
            setOutsideBackground();
            gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_LOOP_UI, renderable.SPACESHIP_BG_AUDIO_PATH));
            gameState.addRenderables(new ArrayList<>(renderable.getAllRenderables()));
            gameState.addRenderable(renderable);
            super.episodeStartedEvents(gameState);
        }
    }

    protected void setOutsideBackground() {
        if (gameInfo.getCurrentDay() == 1) {
            renderable.setOutsideBackground(renderable.FOREST_BG_IMG_PATH);
        } else {
            renderable.setOutsideBackground(renderable.getBG_IMG_PATH(gameInfo.getCurrentLocation()));
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
                if (buttonsEnabled && !contactClickable && mapClickable)
                    goToEpisode(gameState, new GameEvent(SELECT_LOCATION_ON_MAP_EPISODE, null, this));
                else
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.WRONG_BUTTON_AUDIO_PATH));
                break;
            case UserActionCode.CHARGE_SPACESHIP_EPISODE:
                if (buttonsEnabled && !contactClickable && chargeClickable)
                    goToEpisode(gameState, new GameEvent(SPACESHIP_CHARGER_EPISODE_STARTED, null, this));
                else
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.WRONG_BUTTON_AUDIO_PATH));
                break;
            case UserActionCode.SPACESHIP_INVENTORY_EPISODE:
                if (buttonsEnabled && !contactClickable && inventoryClickable) {
                    goToEpisode(gameState, new GameEvent(SPACESHIP_INVENTORY_EPISODE_STARTED, null, this));
                } else
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.WRONG_BUTTON_AUDIO_PATH));
                break;
            case UserActionCode.TRAVEL:
                if (buttonsEnabled && !contactClickable && travelClickable) {

                } else
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.WRONG_BUTTON_AUDIO_PATH));
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
        return new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_FAILURE, cleanUpGameState(currentState));
    }

    protected void setCockpitFieldValues() {
        if (gameInfo.getCurrentLocation() != null)
            renderable.setLocationValue(gameInfo.getCurrentLocationName());
        renderable.setRemainingEnergyValue(String.valueOf(gameInfo.getRemainingEnergy()));
        renderable.setMotorEfficiencyValue(String.valueOf(gameInfo.getMotorEfficiency()));
        renderable.setDaysLeftValue(gameInfo.getDaysLeftForDestination() + "");
        if (gameInfo.getNextLocation() != null) {
            renderable.setDestinationDistanceValue(gameInfo.getNextLocationDistance() + "");
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
}
