package org.scify.moonwalker.app.game.rules.episodes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import org.scify.engine.*;
import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.moonwalker.app.game.GameInfo;
import org.scify.moonwalker.app.game.Location;
import org.scify.moonwalker.app.game.LocationController;
import org.scify.moonwalker.app.ui.actors.ImageWithEffect;
import org.scify.moonwalker.app.ui.renderables.MapLocationRenderable;

import java.util.LinkedList;
import java.util.List;

public class MapEpisodeRules extends TemporaryEpisodeRules {

    protected List<Renderable> mapLocationRenderables;
    protected LocationController locationController;
    protected GameInfo gameInfo;
    protected boolean travelOnly;


    public MapEpisodeRules(boolean bTravelOnly) {
        travelOnly = bTravelOnly;
    }

    @Override
    protected void handleUserAction(GameState gsCurrent, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case FINISH_EPISODE:
                setFieldsForTimedEpisode(gsCurrent, "img/next_day.jpg", 4000);
                gsCurrent.addGameEvent(new GameEvent("SIMPLE_TIMED_IMAGE_EPISODE_STARTED", null, this));
                setLocation(userAction);
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
            locationController = new LocationController();
            gameInfo = GameInfo.getInstance();
            addEpisodeBackgroundImage(currentState, "img/episode_map/bg.png");

            // Create back button
            ActionButtonRenderable escape = createEscapeButton();
            escape.setUserAction(new UserAction(UserActionCode.BACK));
            currentState.addRenderable(escape);

            createMapLocationRenderables();
            currentState.addRenderables(mapLocationRenderables);

            super.episodeStartedEvents(currentState);
        }
    }

    protected void createMapLocationRenderables() {
        // use linked list to be ordered
        mapLocationRenderables = new LinkedList<>();
        // For each location
        int iCnt = 0;
        for(Location location : locationController.getLocations()) {
            // Create location renderable
            MapLocationRenderable renderable = new MapLocationRenderable(appInfo.pixelsWithDensity(location.getPosX()),
                    appInfo.pixelsWithDensity((location.getPosY())), appInfo.pixelsWithDensity(150), appInfo.pixelsWithDensity(100), "location" + String.valueOf(iCnt++));
            // update its location
            renderable.setLocation(location);
            // and create corresponding button
            ActionButtonRenderable locationBtn = new ActionButtonRenderable(appInfo.pixelsWithDensity(50), appInfo.pixelsWithDensity(25), appInfo.pixelsWithDensity(20), appInfo.pixelsWithDensity(20), "image_button", "location1Btn");
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
