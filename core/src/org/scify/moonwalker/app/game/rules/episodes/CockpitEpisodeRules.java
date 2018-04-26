package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.LocationController;
import org.scify.engine.renderables.ActionButtonWithEffect;
import org.scify.moonwalker.app.ui.renderables.CockpitRenderable;

import java.util.Date;

public class CockpitEpisodeRules extends BaseEpisodeRules {

    protected CockpitRenderable renderable;
    protected LocationController locationController;

    @Override
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            locationController = new LocationController();
            super.episodeStartedEvents(currentState);
            //addEpisodeBackgroundImage(currentState, "img/renderable/generic_background.png");
            init(currentState);
        } else {

        }
    }

    protected void init(GameState gameState) {
        renderable = new CockpitRenderable(0,0, appInfo.getScreenWidth(),
                appInfo.getScreenHeight(), "cockpit", "cockpit");
        setCockpitFieldValues();
        setCockpitButtons();
        if (gameInfo.getContactRequestFlag()) {
            gameState.addGameEvent(new GameEvent("TOOGLE_CONTACT_BUTTON", new Date().getTime() + 500, false, this));
            //renderable.toogleContactButton();
        }
        gameState.addRenderable(renderable);
    }

    @Override
    protected void handleUserAction(GameState gsCurrent, UserAction userAction) {
        if (gameInfo.getContactRequestFlag()) {
            if (userAction.getActionCode().equals(UserActionCode.CONTACT_SCREEN_EPISODE)) {
                gsCurrent.addGameEvent(new GameEvent("CONTACT_SCREEN_EPISODE_STARTED", null, this));
                episodeEndedEvents(gsCurrent);
            }
        }else {
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
    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        EpisodeEndState endStateFromParent = super.determineEndState(currentState);
        if(endStateFromParent != null)
            return endStateFromParent;
        else if(currentState.eventsQueueContainsEventOwnedBy("CONTACT_SCREEN_EPISODE_STARTED", this))
            return new EpisodeEndState(EpisodeEndStateCode.CONTACT_SCREEN_EPISODE_STARTED, cleanUpState(currentState));
        else if(currentState.eventsQueueContainsEventOwnedBy("MAP_EPISODE_STARTED", this))
            return new EpisodeEndState(EpisodeEndStateCode.MAP_EPISODE_STARTED, cleanUpState(currentState));
        else if(currentState.eventsQueueContainsEventOwnedBy("SPACESHIP_CHARGER_EPISODE_STARTED", this))
            return new EpisodeEndState(EpisodeEndStateCode.SPACESHIP_CHARGER_EPISODE_STARTED, cleanUpState(currentState));
        return new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_FAILURE, cleanUpState(currentState));
    }

    protected void setCockpitFieldValues() {
        if(gameInfo.getCurrentLocation() != null)
            renderable.setPositionValue(gameInfo.getCurrentLocationName());
        renderable.setRemainingEnergyValue(String.valueOf(gameInfo.getRemainingEnergy()));
        renderable.setMotorEfficiencyValue(String.valueOf(gameInfo.getMotorEfficiency()));
        renderable.setPositionValue(String.valueOf(gameInfo.getCurrentLocation().getName()));
        renderable.setDaysLeftValue("99");
        renderable.setDestinationDistanceValue(1000);
    }

    protected void setCockpitButtons() {
        ActionButtonWithEffect navigateBtn = createCockpitButton("navigation_button", "img/cockpit/navigate.png", UserActionCode.FINISH_EPISODE);
        ActionButtonWithEffect launchBtn = createCockpitButton("launch_button", "img/cockpit/launch.png", UserActionCode.FINISH_EPISODE);
        ActionButtonWithEffect spaceshipPartsButton = createCockpitButton("spaceship_parts_button", "img/cockpit/spaceship.png", UserActionCode.SPACESHIP_PARTS_EPISODE);
        ActionButtonWithEffect mapBtn = createCockpitButton("map_button", "img/cockpit/map.png", UserActionCode.MAP_EPISODE);
        ActionButtonWithEffect chargeBtn = createCockpitButton("charge_button", "img/cockpit/charge.png", UserActionCode.CHARGE_SPACESHIP_EPISODE);
        ActionButtonWithEffect contactBtnSimple = createCockpitButton("contact_button_simple", "img/cockpit/contact.png", UserActionCode.CONTACT_SCREEN_EPISODE);
        ActionButtonWithEffect contactBtnLighted = createCockpitButton("contact_button_lighted", "img/cockpit/contact_lighted.png", UserActionCode.CONTACT_SCREEN_EPISODE);


        renderable.setNavigateButton(navigateBtn);
        renderable.setLaunchButton(launchBtn);
        renderable.setSpaceshipPartsButton(spaceshipPartsButton);
        renderable.setMapButton(mapBtn);
        renderable.setContactButtons(contactBtnSimple, contactBtnLighted);
        renderable.setChargeButton(chargeBtn);
    }

    protected ActionButtonWithEffect createCockpitButton(String id, String imgPath, UserActionCode code) {
        return createImageButton(id, imgPath, new UserAction(code), 0, 0);
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
