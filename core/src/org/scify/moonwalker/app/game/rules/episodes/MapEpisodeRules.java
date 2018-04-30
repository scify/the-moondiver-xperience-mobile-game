package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.GameState;
import org.scify.engine.UserAction;
import org.scify.moonwalker.app.game.GameInfo;
import org.scify.moonwalker.app.game.Location;
import org.scify.moonwalker.app.game.LocationController;
import org.scify.moonwalker.app.ui.renderables.MapEpisodeRenderable;


// 1st step: constructor
//

public class MapEpisodeRules extends FadingEpisodeRules<MapEpisodeRenderable> {
    public static final String LOCATION_SELECTED = "LOCATION_SELECTED";
    protected LocationController locationController;
    protected boolean travelOnly;


    public MapEpisodeRules(boolean bTravelOnly) {
        super();
        travelOnly = bTravelOnly;
        locationController = new LocationController();
    }

    @Override
    protected void episodeStartedEvents(GameState currentState) {
        if (!episodeStarted) {
            // Create main renderable
            renderable = new MapEpisodeRenderable(0.0f, 0.0f, appInfo.getScreenWidth(), appInfo.getScreenHeight(),
                    "mapEpisodeRenderable", new LocationController().getLocations());

            // Get main properties from game state and apply to renderable
            renderable.setCurrentLocation(gameInfo.getCurrentLocation());
            renderable.setNextAllowedLocation(gameInfo.getNextAllowedLocation());
            renderable.setLocationSelected(false);

            super.episodeStartedEvents(currentState);
        }
    }



    @Override
    protected void episodeEndedEvents(GameState currentState) {
        // Update main properties in game info
        gameInfo.setNextLocation(renderable.getNextAllowedLocation());

        super.episodeEndedEvents(currentState);
    }

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        return super.getNextState(gsCurrent, userAction);
    }

    @Override
    protected void handleUserAction(GameState gsCurrent, UserAction userAction) {
        // If location was selected
        if (userAction.getActionCode().equals(LOCATION_SELECTED)) {
            // Update next location
            renderable.setNextLocation((Location)userAction.getActionPayload());
            // Also update game info
            gameInfo.setNextLocation((Location)userAction.getActionPayload());
        }

        super.handleUserAction(gsCurrent, userAction);
    }

}
