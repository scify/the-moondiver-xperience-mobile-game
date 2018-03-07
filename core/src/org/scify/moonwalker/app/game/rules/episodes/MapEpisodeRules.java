package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.GameInfo;
import org.scify.moonwalker.app.game.Location;
import org.scify.moonwalker.app.game.LocationController;
import org.scify.moonwalker.app.ui.actors.ActionButton;
import org.scify.moonwalker.app.ui.renderables.MapLocationRenderable;

import java.util.LinkedList;
import java.util.List;

public class MapEpisodeRules extends TemporaryEpisodeRules {

    protected List<Renderable> mapLocationRenderables;
    protected LocationController locationController;
    protected GameInfo gameInfo;

    @Override
    protected void handleUserAction(GameState gsCurrent, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case FINISH_EPISODE:
                setFieldsForTimedEpisode(gsCurrent, "img/next_day.jpg", 4000);
                gsCurrent.addGameEvent(new GameEvent("SIMPLE_TIMED_IMAGE_EPISODE_STARTED", null, this));
                episodeEndedEvents(gsCurrent);
                setLocation(userAction);
                break;
            default:
                super.handleUserAction(gsCurrent, userAction);
                break;
        }
    }
    @Override
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            locationController = new LocationController();
            gameInfo = GameInfo.getInstance();
            super.episodeStartedEvents(currentState);
            addEpisodeBackgroundImage(currentState, "img/map.jpg");
            addPlayerAvatar(currentState);
            ActionButton escape = createEscapeButton();
            escape.setUserAction(new UserAction(UserActionCode.BACK));
            currentState.addRenderable(escape);
            createMapLocationRenderables();
            currentState.addRenderables(mapLocationRenderables);
        }
    }

    protected void createMapLocationRenderables() {
        // use linked list to be ordered
        mapLocationRenderables = new LinkedList<>();
        for(Location location : locationController.getLocations()) {
            MapLocationRenderable renderable = new MapLocationRenderable(appInfo.pixelsWithDensity(location.getPosX()), appInfo.pixelsWithDensity((location.getPosY())), appInfo.pixelsWithDensity(150), appInfo.pixelsWithDensity(100), "location");
            renderable.setLocation(location);
            ActionButton locationBtn = new ActionButton(appInfo.pixelsWithDensity(50), appInfo.pixelsWithDensity(25), appInfo.pixelsWithDensity(20), appInfo.pixelsWithDensity(20), "image_button", "location1Btn");
            //TODO
            locationBtn.setUserAction(new UserAction(UserActionCode.FINISH_EPISODE, location));
            locationBtn.setImgPath(location.getImgPath());
            renderable.setButton(locationBtn);
            renderable.setImgPath("img/component_background.png");
            mapLocationRenderables.add(renderable);
        }

    }

    protected void setLocation(UserAction userAction) {
        gameInfo.setNextLocation((Location) userAction.getActionPayload());
    }

}
