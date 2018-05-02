package org.scify.moonwalker.app.game.rules.episodes;

import com.badlogic.gdx.math.Vector2;
import org.scify.engine.*;
import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.*;
import org.scify.moonwalker.app.game.Location;
import org.scify.moonwalker.app.game.LocationController;
import org.scify.moonwalker.app.ui.renderables.MapEpisodeRenderable;

import java.util.ArrayList;
import java.util.List;


/* Guide:
 1st step: constructor
 2nd step: episodeStartedEvents, where we create renderables (and call super... to perform fade in etc.)
 3rd step: handleUserAction, where we handle any user action. The constants for the UserAction types should
 be declared in the corresponding Renderable.  (and call super... to handle episode end and others)
 4th step: episodeEndedEvents, where we may update the gameInfo, as needed (and call super... to perform fade out etc.).
*/

public class MapEpisodeRules extends BaseEpisodeRules {
    protected LocationController locationController;
    protected boolean travelOnly;
    protected MapEpisodeRenderable renderable;


    public MapEpisodeRules(boolean bTravelOnly) {
        super();
        travelOnly = bTravelOnly;
        locationController = LocationController.getInstance();
    }

    @Override
    protected void episodeStartedEvents(final GameState currentState) {
        if (!episodeStarted) {
            // Create main renderable
            renderable = new MapEpisodeRenderable(0.0f, 0.0f, appInfo.getScreenWidth(), appInfo.getScreenHeight(),
                    "mapEpisodeRenderable", locationController.getLocations(), gameInfo.getNextAllowedLocation(),
                    gameInfo.getCurrentLocation(), travelOnly);

            // Get main properties from game state and apply to renderable
            renderable.setLocationSelected(false);

            // Add important children (taking into account current location,etc.)
            currentState.addRenderables(new ArrayList<>(renderable.getAllRenderables()));
            // and main renderable
            currentState.addRenderable(renderable);

            if (!travelOnly) {
                renderable.fadeIn(null);
            } else {
                renderable.fadeIn(new Runnable() {
                    @Override
                    public void run() {
                        // Apply user action for selection
                        handleUserAction(currentState, new UserAction(MapEpisodeRenderable.MAP_SELECT_ACTION,
                                renderable.getNextAllowedLocation()));
                    }
                });
            }

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
    protected void handleUserAction(final GameState gsCurrent, UserAction userAction) {
        // If location was selected
        String actionCode = userAction.getActionCode();
        switch (actionCode) {
            case MapEpisodeRenderable.MAP_SELECT_ACTION:
                // Update next location
                renderable.setNextLocation((Location)userAction.getActionPayload());
                // Also update game info
                gameInfo.setNextLocation((Location)userAction.getActionPayload());
                // And show appropriate effect
                createSpaceshipMovementEffect(gsCurrent);
                break;
            case UserActionCode.QUIT:
                EffectSequence eFadeOutAndEnd = renderable.getDefaultFadeOutEffect();
                eFadeOutAndEnd.addEffect(new FunctionEffect(new Runnable() {
                    @Override
                    public void run() {
                        endEpisodeAndAddEventWithType(gsCurrent, "");
                    }
                }));

                renderable.addEffect(eFadeOutAndEnd);
                renderable.fadeOut();
        }

        super.handleUserAction(gsCurrent, userAction);
    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        return new EpisodeEndState(EpisodeEndStateCode.TEMP_EPISODE_FINISHED, currentState);
    }

    protected void createSpaceshipMovementEffect(final GameState gsCurrent) {
        Renderable rCurLocation = renderable.getRenderableForLocation(renderable.getCurrentLocation());
        Renderable rNextLocation = renderable.getRenderableForLocation(renderable.getNextAllowedLocation());
        final float dStartX = rCurLocation.getxPos();
        final float dStartY = rCurLocation.getyPos();
        final float dEndX = rNextLocation.getxPos();
        final float dEndY = rNextLocation.getyPos();

        if (!travelOnly) {
            // Add movement effect to spaceship
            Renderable rStar = new ImageRenderable("star_route_img", MapEpisodeRenderable.STAR_IMG_PATH);
            rStar.setVisible(false);
            rStar.setZIndex(10);

            EffectSequence esRes = new EffectSequence();
            // Create points
            List<Vector2> lvPoints = new ArrayList<>();

            int iNumOfSteps = 10;
            for (int iCnt = 1; iCnt < iNumOfSteps; iCnt++) {
                Vector2 vCur = new Vector2();
                vCur.set(dStartX + (dEndX - dStartX) * ((float) iCnt / iNumOfSteps), dStartY + (dEndY - dStartY) * ((float) iCnt / iNumOfSteps));
                lvPoints.add(vCur);
            }
            // Perform route effect
            PointRouteSinglePointTypeEffect pRoute = new PointRouteSinglePointTypeEffect(lvPoints, 1.0,
                    4.0, 4.0, 5000);
            esRes.addEffect(pRoute);

            rStar.addEffect(esRes);
            // Add star to renderables
            gsCurrent.addRenderable(rStar);
        }


        // If only travel
        if (travelOnly) {
            final double dTransitionTime = 3000.0;

            EffectSequence esRes = new EffectSequence();
            // Add movement effect to spaceship
            esRes.addEffect(new FunctionEffect(new Runnable() {
                @Override
                public void run() {
                    renderable.getRenderableForLocation(renderable.getCurrentLocation()).addEffect(new MoveEffect(
                            dStartX, dStartY, dEndX, dEndY, dTransitionTime
                    ));
                }
            }));
            // make sure you quit after a while
            esRes.addEffect(new DelayEffect(dTransitionTime));
            esRes.addEffect(new FunctionEffect(new Runnable() {
                @Override
                public void run() {
                    handleUserAction(gsCurrent, new UserAction(UserActionCode.QUIT));
                    gameInfo.setCurrentLocation(renderable.getNextAllowedLocation());
                }
            }));
            rCurLocation.addEffect(esRes);
        }

    }

}
