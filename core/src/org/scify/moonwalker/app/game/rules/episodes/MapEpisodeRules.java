package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.Location;
import org.scify.moonwalker.app.game.LocationController;
import org.scify.moonwalker.app.ui.renderables.MapEpisodeRenderable;


/* Guide:
 1st step: constructor
 2nd step: episodeStartedEvents, where we create renderables (and call super... to perform fade in etc.)
 3rd step: handleUserAction, where we handle any user action. The constants for the UserAction types should
 be declared in the corresponding Renderable.  (and call super... to handle episode end and others)
 4th step: episodeEndedEvents, where we may update the gameInfo, as needed (and call super... to perform fade out etc.).
*/

public class MapEpisodeRules extends FadingEpisodeRules<MapEpisodeRenderable> {
    protected LocationController locationController;
    protected boolean travelOnly;


    public MapEpisodeRules(boolean bTravelOnly) {
        super();
        travelOnly = bTravelOnly;
        locationController = LocationController.getInstance();
    }

    @Override
    protected void episodeStartedEvents(GameState currentState) {
        if (!episodeStarted) {
            // Create main renderable
            renderable = new MapEpisodeRenderable(0.0f, 0.0f, appInfo.getScreenWidth(), appInfo.getScreenHeight(),
                    "mapEpisodeRenderable", locationController.getLocations());

            // Add important children
            currentState.addRenderable(renderable.getCloseButton());
            currentState.addRenderable(renderable.getDistanceHUD());
            currentState.addRenderable(renderable.getLocationNameHUD());
            currentState.addRenderable(renderable.getMissionHUD());

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
        String actionCode = userAction.getActionCode();
        switch (actionCode) {
            case MapEpisodeRenderable.MAP_SELECT_ACTION:
                // Update next location
                renderable.setNextLocation((Location)userAction.getActionPayload());
                // Also update game info
                gameInfo.setNextLocation((Location)userAction.getActionPayload());
                break;
            case UserActionCode.QUIT:
                endEpisodeAndAddEventWithType(gsCurrent, "");
        }

        super.handleUserAction(gsCurrent, userAction);
    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        return new EpisodeEndState(EpisodeEndStateCode.TEMP_EPISODE_FINISHED, currentState);
    }
}
