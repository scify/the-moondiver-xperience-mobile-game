package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.ui.actors.ActionButton;
import org.scify.moonwalker.app.ui.renderables.CockpitRenderable;

public class CockpitRules extends BaseEpisodeRules {

    CockpitRenderable cockpit;

    @Override
    protected void handleUserAction(GameState gsCurrent, UserAction userAction) {
        switch (userAction.getActionCode()) {
            // TODO update
            case FINISH_EPISODE:
                cockpit.setRemainingEnergyValue("1124564");
                break;
        }
    }

    @Override
    public void gameStartedEvents(GameState currentState) {
        if (!currentState.eventsQueueContainsEvent("EPISODE_STARTED")) {
            currentState.addGameEvent(new GameEvent("EPISODE_STARTED"));
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
        return new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_FAILURE, cleanUpState(currentState));
    }
    
    protected void initializeAndAddCockpit(GameState currentState) {
        cockpit = new CockpitRenderable(0,0, gameInfo.getScreenWidth(),
                gameInfo.getScreenHeight(), "cockpit", "cockpit");
        cockpit.setImgPath("img/cockpit.png");
        ActionButton navigationBtn = createCockpitButton("navigation_button", "img/navigation.png", UserActionCode.FINISH_EPISODE);
        ActionButton vesselBtn = createCockpitButton("vessel_button", "img/rocket.png", UserActionCode.FINISH_EPISODE);
        ActionButton mapBtn = createCockpitButton("map_button", "img/map.png", UserActionCode.FINISH_EPISODE);
        ActionButton contactBtn = createCockpitButton("contact_button", "img/contact.png", UserActionCode.FINISH_EPISODE);

        cockpit.setNavigationButton(navigationBtn);
        cockpit.setVesselButton(vesselBtn);
        cockpit.setMapButton(mapBtn);
        cockpit.setContactButton(contactBtn);

        currentState.addRenderable(cockpit);
    }

    protected ActionButton createCockpitButton(String id, String imgPath, UserActionCode code) {
        return createImageButton(id, imgPath, new UserAction(code), 100, 100);
    }
}
