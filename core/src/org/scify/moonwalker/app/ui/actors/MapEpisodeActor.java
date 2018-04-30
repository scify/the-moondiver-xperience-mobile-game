package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.scify.engine.renderables.Renderable;
import org.scify.moonwalker.app.game.Location;
import org.scify.moonwalker.app.ui.renderables.MapEpisodeRenderable;

import java.util.ArrayList;
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
        setWidth(appInfo.getScreenWidth());
        setHeight(appInfo.getScreenHeight());
        addBackground(renderable.getTableBGRenderable(), appInfo.getScreenWidth(), appInfo.getScreenHeight());

        init();
    }

    protected void init() {
        createAndAddLocationNameHUD();
        createAndAddDistanceHUD();
        createAndAddMissionHUD();

        createAndAddLocationPoints();
        createAndAddLocationImages();

        createAndAddExitButton();

    }

    protected Actor createAndAddExitButton() {
        Actor closeButton = (Actor)(bookKeeper.getUIRepresentationOfRenderable(renderable.getCloseButton()));
        getChildrenActorsAndRenderables().put(closeButton, renderable.getCloseButton());
        return closeButton;
    }

    private Actor createAndAddMissionHUD() {
        missionHUD = (LabelWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getMissionHUD());
        getChildrenActorsAndRenderables().put(missionHUD, renderable.getMissionHUD());
        return missionHUD;
    }

    private Actor createAndAddDistanceHUD() {
        distanceHUD = (LabelWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getDistanceHUD());
        getChildrenActorsAndRenderables().put(distanceHUD, renderable.getDistanceHUD());

        return distanceHUD;
    }

    private Actor createAndAddLocationNameHUD() {
        locationNameHUD = (LabelWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getLocationNameHUD());
        getChildrenActorsAndRenderables().put(locationNameHUD, renderable.getLocationNameHUD());
        return locationNameHUD;
    }

    private List<Actor> createAndAddLocationImages() {
        List<Actor> lRes = new ArrayList<>();
        // For each location point
        for (Renderable rCur : renderable.getLocationNameLabels()) {
            // Create corresponding point img
            Actor aTmp = (Actor)bookKeeper.getUIRepresentationOfRenderable(rCur);
            // and add it to important children
            getChildrenActorsAndRenderables().put(aTmp, rCur);
            // add it to return list
            lRes.add(aTmp);
        };

        return lRes;
    }

    private List<Actor> createAndAddLocationPoints() {
        List<Actor> lRes = new ArrayList<>();
        // For each location point
        for (Renderable rCur : renderable.getLocationPoints()) {
            // Create corresponding point img
            Actor aTmp = (Actor)bookKeeper.getUIRepresentationOfRenderable(rCur);
            // and add it to important children
            getChildrenActorsAndRenderables().put(aTmp, rCur);
            // add it to return list
            lRes.add(aTmp);
        };

        return lRes;
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
        }
    }
}
