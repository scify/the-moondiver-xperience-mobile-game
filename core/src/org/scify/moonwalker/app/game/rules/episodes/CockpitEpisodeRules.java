package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.LocationController;
import org.scify.moonwalker.app.ui.renderables.CockpitRenderable;

import java.util.ArrayList;
import java.util.Date;

public class CockpitEpisodeRules extends FadingEpisodeRules<CockpitRenderable> {

    public static final String COCKPIT_ID = "cockpit";
    protected LocationController locationController;
    protected boolean buttonsEnabled;
    protected boolean contactClickable;

    public CockpitEpisodeRules() {
        super();
        buttonsEnabled = false;
        contactClickable = false;
    }

    @Override
    public void episodeStartedEvents(final GameState gameState) {
        if (!isEpisodeStarted(gameState)) {
            locationController = new LocationController();
            renderable = new CockpitRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), COCKPIT_ID);
            setCockpitFieldValues();
            renderable.addAfterFadeIn(new Runnable() {
                @Override
                public void run() {
                    if (gameInfo.getContactRequestFlag()) {
                        gameState.addGameEvent(new GameEvent("TOOGLE_CONTACT_BUTTON", new Date().getTime() + 500, false, this));
                        renderable.toogleButtonLight(renderable.getContactLightedButton());
                        contactClickable = true;
                    }else {
                        buttonsEnabled = true;
                    }
                }
            });
            setOutsideBackground();
            gameState.addGameEvent(new GameEvent(AUDIO_START_LOOP_UI, renderable.SPACESHIP_BG_AUDIO_PATH));
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

    @Override
    protected void handleUserAction(GameState gameState, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case UserActionCode.CONTACT_SCREEN_EPISODE:
                if (contactClickable || buttonsEnabled) {
                    gameState.addGameEvent(new GameEvent(AUDIO_START_UI, renderable.CLICK_AUDIO_PATH));
                    contactClickable = false;
                    renderable.turnOffLightOffAllButtons();
                    renderable.fadeoutOutsideBackground();
                    gameState.addGameEvent(new GameEvent("CONTACT_SCREEN_EPISODE_STARTED", null, this));
                    episodeEndedEvents(gameState);
                }
                break;
            case UserActionCode.MAP_EPISODE:
                if (buttonsEnabled) {
                    gameState.addGameEvent(new GameEvent("MAP_EPISODE_STARTED", null, this));
                    episodeEndedEvents(gameState);
                }
                break;
            case UserActionCode.CHARGE_SPACESHIP_EPISODE:
                if (buttonsEnabled) {
                    gameState.addGameEvent(new GameEvent("SPACESHIP_CHARGER_EPISODE_STARTED", null, this));
                    episodeEndedEvents(gameState);
                }
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
        else if (currentState.eventsQueueContainsEventOwnedBy("CONTACT_SCREEN_EPISODE_STARTED", this))
            return new EpisodeEndState(EpisodeEndStateCode.CONTACT_SCREEN_EPISODE_STARTED, cleanUpGameState(currentState));
        else if (currentState.eventsQueueContainsEventOwnedBy("MAP_EPISODE_STARTED", this))
            return new EpisodeEndState(EpisodeEndStateCode.MAP_EPISODE_STARTED, cleanUpGameState(currentState));
        else if (currentState.eventsQueueContainsEventOwnedBy("SPACESHIP_CHARGER_EPISODE_STARTED", this))
            return new EpisodeEndState(EpisodeEndStateCode.SPACESHIP_CHARGER_EPISODE_STARTED, cleanUpGameState(currentState));
        return new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_FAILURE, cleanUpGameState(currentState));
    }

    protected void setCockpitFieldValues() {
        if (gameInfo.getCurrentLocation() != null)
            renderable.setLocationValue(gameInfo.getCurrentLocationName());
        renderable.setRemainingEnergyValue(String.valueOf(gameInfo.getRemainingEnergy()));
        renderable.setMotorEfficiencyValue(String.valueOf(gameInfo.getMotorEfficiency()));
        renderable.setDaysLeftValue("99");
        renderable.setDestinationDistanceValue(1000);
    }


    @Override
    public GameState getNextState(GameState gameState, UserAction userAction) {
        long timestamp = new Date().getTime();
        GameEvent contactToggleEvent = gameState.getGameEventWithType("TOOGLE_CONTACT_BUTTON");
        if (contactToggleEvent != null && timestamp > contactToggleEvent.delay) {
            gameState.removeGameEventsWithType("TOOGLE_CONTACT_BUTTON");
            if (contactClickable) {
                renderable.toogleButtonLight(renderable.getContactLightedButton());
                gameState.addGameEvent(new GameEvent("TOOGLE_CONTACT_BUTTON", new Date().getTime() + 500, false, this));
            }
        }
        return super.getNextState(gameState, userAction);
    }
}
