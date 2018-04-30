package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.scify.engine.renderables.Renderable;
import org.scify.moonwalker.app.game.Location;
import org.scify.moonwalker.app.ui.renderables.MapEpisodeRenderable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapEpisodeActor extends FadingTableActor<MapEpisodeRenderable> implements Updateable<MapEpisodeRenderable> {

    Map<Location, ImageWithEffect> mPointsPerLocation = new HashMap<>();
    protected LabelWithEffect missionHUD;
    protected LabelWithEffect distanceHUD;
    protected LabelWithEffect locationNameHUD;


    public MapEpisodeActor(Skin skin, MapEpisodeRenderable rRenderable) {
        super(skin, rRenderable);

        createAndAddLocationPoints();
        createAndAddLocationImages();
        createAndAddLocationNameHUD();
        createAndAddDistanceHUD();
        createAndAddMissionHUD();
    }

    private void createAndAddMissionHUD() {
        missionHUD = (LabelWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getMissionHUD());
        getChildrenActorsAndRenderables().put(missionHUD, renderable.getMissionHUD());
    }

    private void createAndAddDistanceHUD() {
        distanceHUD = (LabelWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getDistanceHUD());
        getChildrenActorsAndRenderables().put(distanceHUD, renderable.getDistanceHUD());
    }

    private void createAndAddLocationNameHUD() {
        locationNameHUD = (LabelWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getLocationNameHUD());
        getChildrenActorsAndRenderables().put(locationNameHUD, renderable.getLocationNameHUD());
    }

    private void createAndAddLocationImages() {
        // TODO: Later
    }

    private void createAndAddLocationPoints() {
        // For each location point
        for (Renderable rCur : renderable.getLocationPoints()) {
            // Create corresponding point img
            Actor aTmp = (Actor)bookKeeper.getUIRepresentationOfRenderable(rCur);
            // and add it to important children
            getChildrenActorsAndRenderables().put(aTmp, rCur);
        };
    }


    @Override
    public void update(MapEpisodeRenderable renderable) {
        // If location is selected
        if (renderable.isLocationSelected()) {
            Location lTarget = renderable.getNextAllowedLocation();
            // Update HUD
            locationNameHUD.setText(lTarget.getName());
            // Update distance
            distanceHUD.setText(String.valueOf(lTarget.getDistanceFromLocation(renderable.getCurrentLocation())));
            // Update mission
            missionHUD.setText(lTarget.getMission());

            // Share that we performed the update as needed
            renderable.wasUpdated();
        }
    }
}
