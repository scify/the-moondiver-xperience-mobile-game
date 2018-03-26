package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.LocationController;
import org.scify.moonwalker.app.ui.actors.ActionButton;
import org.scify.moonwalker.app.ui.renderables.CockpitRenderable;

public class CockpitEpisodeRules extends BaseEpisodeRules {

    protected CockpitRenderable cockpit;
    protected LocationController locationController;

    @Override
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            locationController = new LocationController();
            super.episodeStartedEvents(currentState);
            addEpisodeBackgroundImage(currentState, "img/cockpit/generic_background.png");
            initializeAndAddCockpit(currentState);
        } else {

        }
    }

    protected void initializeAndAddCockpit(GameState currentState) {
        cockpit = new CockpitRenderable(0,0, appInfo.getScreenWidth(),
                appInfo.getScreenHeight(), "cockpit", "cockpit");
        setCockpitFieldValues();
        setCockpitButtons();
        cockpit.setImgPath("img/cockpit/cockpit_background.png");
        currentState.addRenderable(cockpit);
    }

    @Override
    protected void handleUserAction(GameState gsCurrent, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case MAP_EPISODE:
                gsCurrent.addGameEvent(new GameEvent("MAP_EPISODE_STARTED", null, this));
                episodeEndedEvents(gsCurrent);
                break;
            case CHARGE_SPACESHIP_EPISODE:
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
        if(endStateFromParent != null)
            return endStateFromParent;
        else if(currentState.eventsQueueContainsEventOwnedBy("MAP_EPISODE_STARTED", this))
            return new EpisodeEndState(EpisodeEndStateCode.MAP_EPISODE_STARTED, cleanUpState(currentState));
        else if(currentState.eventsQueueContainsEventOwnedBy("SPACESHIP_CHARGER_EPISODE_STARTED", this))
            return new EpisodeEndState(EpisodeEndStateCode.SPACESHIP_CHARGER_EPISODE_STARTED, cleanUpState(currentState));
        return new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_FAILURE, cleanUpState(currentState));
    }

    protected void setCockpitFieldValues() {
        if(gameInfo.getCurrentLocation() != null)
            cockpit.setPositionValue(gameInfo.getCurrentLocationName());
        cockpit.setRemainingEnergyValue(String.valueOf(gameInfo.getRemainingEnergy()));
        cockpit.setMotorEfficiencyValue(String.valueOf(gameInfo.getMotorEfficiency()));
        cockpit.setPositionValue(String.valueOf(gameInfo.getCurrentLocation().getName()));
        cockpit.setDaysLeftValue("99");
        cockpit.setDestinationDistanceValue(1000);
    }

    protected void setCockpitButtons() {
        ActionButton navigateBtn = createCockpitButton("navigation_button", "img/cockpit/navigate.png", UserActionCode.FINISH_EPISODE);
        ActionButton launchBtn = createCockpitButton("launch_button", "img/cockpit/launch.png", UserActionCode.FINISH_EPISODE);
        ActionButton spaceshipPartsButton = createCockpitButton("spaceship_parts_button", "img/cockpit/spaceship.png", UserActionCode.SPACESHIP_PARTS_EPISODE);
        ActionButton mapBtn = createCockpitButton("map_button", "img/cockpit/map.png", UserActionCode.MAP_EPISODE);
        ActionButton contactBtn = createCockpitButton("contact_button", "img/cockpit/contact.png", UserActionCode.FINISH_EPISODE);
        ActionButton chargeBtn = createCockpitButton("charge_button", "img/cockpit/charge.png", UserActionCode.CHARGE_SPACESHIP_EPISODE);


        cockpit.setNavigateButton(navigateBtn);
        cockpit.setLaunchButton(launchBtn);
        cockpit.setSpaceshipPartsButton(spaceshipPartsButton);
        cockpit.setMapButton(mapBtn);
        cockpit.setContactButton(contactBtn);
        cockpit.setChargeButton(chargeBtn);
    }

    protected ActionButton createCockpitButton(String id, String imgPath, UserActionCode code) {
        return createImageButton(id, imgPath, new UserAction(code), 0, 0);
    }
}
