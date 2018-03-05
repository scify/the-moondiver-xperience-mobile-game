package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.ui.actors.ActionButton;
import org.scify.moonwalker.app.ui.renderables.CockpitRenderable;

public class CockpitEpisodeRules extends BaseEpisodeRules {

    CockpitRenderable cockpit;

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
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            super.episodeStartedEvents(currentState);
            Renderable spaceImage = new Renderable(0,0, gameInfo.getScreenWidth(), gameInfo.getScreenHeight(), "image", "space");
            spaceImage.setImgPath("img/space1.png");
            currentState.addRenderable(spaceImage);
            addRenderableEntry("space", spaceImage);
            initializeAndAddCockpit(currentState);
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
    
    protected void initializeAndAddCockpit(GameState currentState) {
        cockpit = new CockpitRenderable(0,0, gameInfo.getScreenWidth(),
                gameInfo.getScreenHeight(), "cockpit", "cockpit");
        cockpit.setImgPath("img/cockpit.png");
        ActionButton launchBtn = createCockpitButton("navigation_button", "img/launch.png", UserActionCode.FINISH_EPISODE);
        ActionButton spaceshipPartsButton = createCockpitButton("spaceship_parts_button", "img/rocket.png", UserActionCode.SPACESHIP_PARTS_EPISODE);
        ActionButton mapBtn = createCockpitButton("map_button", "img/map.png", UserActionCode.MAP_EPISODE);
        ActionButton contactBtn = createCockpitButton("contact_button", "img/contact.png", UserActionCode.FINISH_EPISODE);
        ActionButton chargeBtn = createCockpitButton("charge_button", "img/battery.png", UserActionCode.CHARGE_SPACESHIP_EPISODE);

        cockpit.setLaunchButton(launchBtn);
        cockpit.setSpaceshipPartsButton(spaceshipPartsButton);
        cockpit.setMapButton(mapBtn);
        cockpit.setContactButton(contactBtn);
        cockpit.setChargeButton(chargeBtn);

        currentState.addRenderable(cockpit);
    }

    protected ActionButton createCockpitButton(String id, String imgPath, UserActionCode code) {
        return createImageButton(id, imgPath, new UserAction(code), 100, 100);
    }
}
