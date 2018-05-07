package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.LocationController;
import org.scify.moonwalker.app.ui.renderables.CockpitRenderable;

import java.util.ArrayList;
import java.util.Date;

public class CockpitEpisodeRules extends FadingEpisodeRules<CockpitRenderable> {

    public static final String COCKPIT_ID = "cockpit";
    protected LocationController locationController;

    public CockpitEpisodeRules() {
        super();
    }

    @Override
    public void episodeStartedEvents(GameState gameState) {
        if (!isEpisodeStarted(gameState)) {
            locationController = new LocationController();
            renderable = new CockpitRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), COCKPIT_ID);
            setCockpitFieldValues();
            if (gameInfo.getContactRequestFlag()) {
                gameState.addGameEvent(new GameEvent("TOOGLE_CONTACT_BUTTON", new Date().getTime() + 500, false, this));
                renderable.toogleContactButton();
            }
            renderable.setOutsideBackground(renderable.FOREST_BG_IMG_PATH);
            gameState.addRenderables(new ArrayList<>(renderable.getAllRenderables()));
            gameState.addRenderable(renderable);
            super.episodeStartedEvents(gameState);
        }
    }

    @Override
    protected void handleUserAction(GameState gsCurrent, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case UserActionCode.CONTACT_SCREEN_EPISODE:
                if (gameInfo.getContactRequestFlag()) {
                    renderable.fadeoutOutsideBackground();
                    gsCurrent.addGameEvent(new GameEvent("CONTACT_SCREEN_EPISODE_STARTED", null, this));
                    episodeEndedEvents(gsCurrent);
                }
                break;
            case UserActionCode.MAP_EPISODE:
                gsCurrent.addGameEvent(new GameEvent("MAP_EPISODE_STARTED", null, this));
                episodeEndedEvents(gsCurrent);
                break;
            case UserActionCode.CHARGE_SPACESHIP_EPISODE:
                gsCurrent.addGameEvent(new GameEvent("SPACESHIP_CHARGER_EPISODE_STARTED", null, this));
                episodeEndedEvents(gsCurrent);
                break;
            default:
                super.handleUserAction(gsCurrent, userAction);
                break;
        }
    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        EpisodeEndState endStateFromParent = super.determineEndState(currentState);
        if (endStateFromParent != null)
            return endStateFromParent;
        else if (currentState.eventsQueueContainsEventOwnedBy("CONTACT_SCREEN_EPISODE_STARTED", this))
            return new EpisodeEndState(EpisodeEndStateCode.CONTACT_SCREEN_EPISODE_STARTED, cleanUpState(currentState));
        else if (currentState.eventsQueueContainsEventOwnedBy("MAP_EPISODE_STARTED", this))
            return new EpisodeEndState(EpisodeEndStateCode.MAP_EPISODE_STARTED, cleanUpState(currentState));
        else if (currentState.eventsQueueContainsEventOwnedBy("SPACESHIP_CHARGER_EPISODE_STARTED", this))
            return new EpisodeEndState(EpisodeEndStateCode.SPACESHIP_CHARGER_EPISODE_STARTED, cleanUpState(currentState));
        return new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_FAILURE, cleanUpState(currentState));
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
        GameEvent contactToggleEvent = gameState.getGameEventsWithType("TOOGLE_CONTACT_BUTTON");
        if (contactToggleEvent != null && timestamp > contactToggleEvent.delay) {
            gameState.removeGameEventsWithType("TOOGLE_CONTACT_BUTTON");
            renderable.toogleContactButton();
            gameState.addGameEvent(new GameEvent("TOOGLE_CONTACT_BUTTON", new Date().getTime() + 500, false, this));
        }
        return super.getNextState(gameState, userAction);
    }
}
