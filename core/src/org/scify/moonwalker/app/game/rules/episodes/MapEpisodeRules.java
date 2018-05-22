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

import static org.scify.engine.EpisodeEndStateCode.TEMP_EPISODE_FINISHED;


/* Guide:
 1st step: constructor
 2nd step: episodeStartedEvents, where we create renderables (and call super... to perform fade in etc.)
 3rd step: handleUserAction, where we handle any user action. The constants for the UserAction types should
 be declared in the corresponding Renderable.  (and call super... to handle episode end and others)
 4th step: episodeEndedEvents, where we may update the gameInfo, as needed (and call super... to perform fade out etc.).
*/

public class MapEpisodeRules extends BaseEpisodeRules {

    public static final String ORIGIN_MIDDLE_OF_NOWHERE = "Μέση του\nΠουθενά";
    public static final String TARGET_MIDDLE_OF_NOWHERE = "Άκρη του\nΠουθενά";
    protected LocationController locationController;
    protected boolean travelOnly;
    protected MapEpisodeRenderable renderable;
    protected Location originMiddleOfNowhere;
    protected Location targetMiddleOfNowhere;
    protected Location originLocation;
    protected Location targetLocation;


    public MapEpisodeRules(boolean bTravelOnly) {
        super();
        travelOnly = bTravelOnly;
        locationController = LocationController.getInstance();
    }

    @Override
    protected void episodeStartedEvents(final GameState currentState) {
        if (!episodeStarted) {
            // Check where we start
            initOriginLocation();

            // Check where we can reach
            initTargetLocation();

            // Check if we need to add "middle of nowhere" locations and add them to the normal list
            List<Location> llLocations = getFullLocationList();

            // Create main renderable
            renderable = new MapEpisodeRenderable(0.0f, 0.0f, appInfo.getScreenWidth(), appInfo.getScreenHeight(),
                    "mapEpisodeRenderable", llLocations, gameInfo.getNextAllowedLocation(),
                    originLocation, travelOnly);
            renderable.setOriginLocation(originLocation);
            renderable.setTargetLocation(targetLocation);

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

    protected List<Location> getFullLocationList() {
        List<Location> llLocations = new ArrayList<>(locationController.getLocations());
        if (originMiddleOfNowhere != null) {
            llLocations.add(originMiddleOfNowhere);
        }
        if (targetMiddleOfNowhere != null) {
            llLocations.add(targetMiddleOfNowhere);
        }
        return llLocations;
    }

    protected void initTargetLocation() {
        double dPercentageOfNextMovePossible = gameInfo.getNextTravelPercentagePossible();
        if(!travelOnly)
            dPercentageOfNextMovePossible = 100.0;
        if (dPercentageOfNextMovePossible < 100.0) {
            // Create "middle of nowhere" location for target
            double dMoNX = gameInfo.getCurrentLocation().getPosX() + (gameInfo.getNextAllowedLocation().getPosX() - gameInfo.getCurrentLocation().getPosX()) * dPercentageOfNextMovePossible / 100;
            double dMoNY = gameInfo.getCurrentLocation().getPosY() + (gameInfo.getNextAllowedLocation().getPosY() - gameInfo.getCurrentLocation().getPosY()) * dPercentageOfNextMovePossible / 100;
            //targetMiddleOfNowhere = new Location(ORIGIN_MIDDLE_OF_NOWHERE, "", (int)dMoNX, (int)dMoNY, NO_MISSION,"", "", "", "");
            targetMiddleOfNowhere = locationController.getNowhereLocation(ORIGIN_MIDDLE_OF_NOWHERE, (int)dMoNX, (int)dMoNY);
            targetLocation = targetMiddleOfNowhere;
        }
        else {
            // else we should not use "middle of nowhere"
            targetMiddleOfNowhere = null;
            targetLocation = gameInfo.getNextAllowedLocation();
        }
        // Update distance from target, if origin or target in the middle of nowhere
        if ((originMiddleOfNowhere != null) || (targetMiddleOfNowhere != null)) {
            double dRemaining;
            // Calculate how much distance percentage we have remaining
//            if (gameInfo.getPreviousTravelPercentageComplete() > gameInfo.getNextTravelPercentagePossible())
//                dRemaining = gameInfo.getNextTravelPercentagePossible();
//            else
            dRemaining = gameInfo.getNextTravelPercentagePossible() - gameInfo.getPreviousTravelPercentageComplete();

            targetLocation.setDistanceToLocation(originLocation, (int)(gameInfo.getNextAllowedLocation().getDistanceFromLocation(gameInfo.getCurrentLocation()) *
                    dRemaining / 100));
        }
    }

    protected void initOriginLocation() {
        double dPercentageOfPreviousMoveComplete = gameInfo.getPreviousTravelPercentageComplete();
        if (dPercentageOfPreviousMoveComplete != 0.0) {
            // Create "middle of nowhere" location for origin
            double dMoNX = gameInfo.getCurrentLocation().getPosX() + (gameInfo.getNextAllowedLocation().getPosX() - gameInfo.getCurrentLocation().getPosX()) * dPercentageOfPreviousMoveComplete / 100.0;
            double dMoNY = gameInfo.getCurrentLocation().getPosY() + (gameInfo.getNextAllowedLocation().getPosY() - gameInfo.getCurrentLocation().getPosY()) * dPercentageOfPreviousMoveComplete / 100.0;
            //originMiddleOfNowhere = new Location(TARGET_MIDDLE_OF_NOWHERE, "", (int)dMoNX, (int)dMoNY, NO_MISSION, "", "", "", "");
            originMiddleOfNowhere = locationController.getNowhereLocation(TARGET_MIDDLE_OF_NOWHERE, (int)dMoNX, (int)dMoNY);
            originLocation = originMiddleOfNowhere;
        }
        else {
            // else we should not use "middle of nowhere"
            originMiddleOfNowhere = null;
            originLocation = gameInfo.getCurrentLocation();
        }
    }

    @Override
    protected void handleUserAction(final GameState gsCurrent, UserAction userAction) {
        // If location was selected
        String actionCode = userAction.getActionCode();
        switch (actionCode) {
            case MapEpisodeRenderable.MAP_SELECT_ACTION:

                // Update next location
                Location nextLocation = (Location)userAction.getActionPayload();
                renderable.setNextLocation(nextLocation);
                // Also update game info
                gameInfo.setNextLocation(nextLocation);
                gameInfo.getNextLocation().setDistanceInKilometers(gameInfo.getCurrentLocation().getDistanceFromLocation(nextLocation));
                // And show appropriate effect
                if (!travelOnly) {
                    gsCurrent.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.LOCATION_SELECTED_AUDIO_PATH));
                    gameInfo.setMapRequestFlag(false);
                    gameInfo.setChargeRequestFlag(true);
                }else {
                    gsCurrent.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.TRAVEL_AUDIO_PATH));
                }
                createSpaceshipMovementEffect(gsCurrent);
                break;
            case UserActionCode.QUIT:
                EffectSequence eFadeOutAndEnd = renderable.getDefaultFadeOutEffect();
                eFadeOutAndEnd.addEffect(new FunctionEffect(new Runnable() {
                    @Override
                    public void run() {
                        gsCurrent.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_DISPOSE_UI, renderable.LOCATION_SELECTED_AUDIO_PATH));
                        gsCurrent.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_DISPOSE_UI, renderable.TRAVEL_AUDIO_PATH));
                        endEpisodeAndAddEventWithType(gsCurrent, "");
                    }
                }));

                renderable.addEffect(eFadeOutAndEnd);
                renderable.fadeOut();
        }

        super.handleUserAction(gsCurrent, userAction);
    }

    protected void createSpaceshipMovementEffect(final GameState gsCurrent) {
        Renderable rCurLocation = renderable.getRenderableForLocation(renderable.getOriginLocation());
        Renderable rNextLocation = renderable.getRenderableForLocation(renderable.getTargetLocation());
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

            // Add rotation and movement effect to spaceship
            esRes.addEffect(new FunctionEffect(new Runnable() {
                @Override
                public void run() {
                    EffectSequence esRotateAndMove = new EffectSequence();
                    esRotateAndMove.addEffect(getRotateToTargetEffect());
                    esRotateAndMove.addEffect(new MoveEffect(dStartX, dStartY, dEndX, dEndY, dTransitionTime));
                    renderable.getRenderableForLocation(renderable.getCurrentLocation()).addEffect(esRotateAndMove);
                }

                private Effect getRotateToTargetEffect() {
                    Effect eRes;
                    double dTargetX = renderable.getNextAllowedLocation().getPosX();
                    double dTargetY = renderable.getNextAllowedLocation().getPosY();
                    double dOriginX = renderable.getCurrentLocation().getPosX();
                    double dOriginY = renderable.getCurrentLocation().getPosY();
                    double dTargetAngle = Math.atan((dTargetY - dOriginY) / (dTargetX - dOriginX));
                    // Convert to degrees
                    dTargetAngle = (dTargetAngle * (180 / Math.PI));
                    if (dTargetX < dOriginX) dTargetAngle += 180; // Make sure you always move head on to the destination
                    eRes = new RotateEffect(0.0, dTargetAngle, 500);

                    return eRes;
                }
            }));
            // make sure you quit after a while
            esRes.addEffect(new DelayEffect(dTransitionTime));
            esRes.addEffect(new FunctionEffect(new Runnable() {
                @Override
                public void run() {
                    handleUserAction(gsCurrent, new UserAction(UserActionCode.QUIT));
                }
            }));
            rCurLocation.addEffect(esRes);
        }

    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        return new EpisodeEndState(TEMP_EPISODE_FINISHED, cleanUpGameState(currentState));
    }
}
