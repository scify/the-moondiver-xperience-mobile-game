package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.TextLabelRenderable;
import org.scify.moonwalker.app.game.Location;
import org.scify.moonwalker.app.ui.actors.ImageWithEffect;

import java.util.ArrayList;
import java.util.List;

public class MapEpisodeRenderable extends FadingTableRenderable {

    public static final String MAP_SELECT_ACTION = "MAP_SELECT";
    public static final String BG_IMAGE_PATH = "img/episode_map/bg.png";

    protected Location currentLocation;
    protected Location nextAllowedLocation;
    protected boolean locationSelected;
    protected List<Location> locations;
    protected List<Renderable> locationPoints;
    protected List<Renderable> locationNames;

    protected ActionButtonRenderable button;
    protected TextLabelRenderable missionHUD;

    protected TextLabelRenderable locationNameHUD;
    protected TextLabelRenderable distanceHUD;


    public MapEpisodeRenderable(float xPos, float yPos, float width, float height, String id, List<Location> lLocations) {
        super(xPos, yPos, width, height, Renderable.MAP_LOCATION, id, BG_IMAGE_PATH);

        locations = lLocations;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
        renderableWasUpdated();
    }

    public void setNextAllowedLocation(Location nextAllowedLocation) {
        this.nextAllowedLocation = nextAllowedLocation;
        renderableWasUpdated();
    }

    public void setLocationSelected(boolean locationSelected) {
        this.locationSelected = locationSelected;
        renderableWasUpdated();
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public Location getNextAllowedLocation() {
        return nextAllowedLocation;
    }

    public boolean isLocationSelected() {
        return locationSelected;
    }

    public ActionButtonRenderable getButton() {
        return button;
    }

    public void setButton(ActionButtonRenderable button) {
        this.button = button;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public Renderable getMissionHUD() {
        if (missionHUD == null) {
            missionHUD = new TextLabelRenderable(0f,0f, 100, 20, Renderable.LABEL, "missionHUD");
            missionHUD.setLabel("Mission");
        }

        return missionHUD;
    }
    public TextLabelRenderable getLocationNameHUD() {
        if (locationNameHUD == null) {
            locationNameHUD = new TextLabelRenderable(0f,25f, 100, 20, Renderable.LABEL, "locationNameHUD");
            locationNameHUD.setLabel("Location");
        }
        return locationNameHUD;
    }

    public TextLabelRenderable getDistanceHUD() {
        if (distanceHUD == null) {
            distanceHUD = new TextLabelRenderable(0f,65f, 100, 20, Renderable.LABEL, "distanceHUD");
            distanceHUD.setLabel("Distance");
        }
        return distanceHUD;
    }

    public List<Renderable> getLocationPoints() {
        if (locationPoints == null) {
            locationPoints = new ArrayList<>();

            // For each location (point)
            for (Location lCur : getLocations()) {
                if (lCur.equals(nextAllowedLocation)) {
                    ActionButtonRenderable btn = new ActionButtonRenderable(lCur.getPosX(), lCur.getPosY(), 100, 50, Renderable.IMAGE_BUTTON, "nextAllowedPoint");
                    btn.setImgPath("img/episode_map/athens.png");
                    btn.setUserAction(new UserAction(MAP_SELECT_ACTION, lCur));
                    locationPoints.add(btn);
                }
                else {
                    // Create corresponding image with effect
                    locationPoints.add(new ImageRenderable(lCur.getPosX(), lCur.getPosY(), 10, 10, lCur.toString(), "img/episode_map/city_dot.png"));
                }
            }
        }

        return locationPoints;
    }

    public List<Renderable> getLocationNameLabels() {
        List<Renderable> lRes = new ArrayList<>();

        // For each location (name)
        for (Location lCur: getLocations()) {
            // Create corresponding image with effect
            locationNames.add(new ImageRenderable(lCur.getPosX(), lCur.getPosY(), 10, 10, lCur.toString(), lCur.getImgPath()));
        }

        return lRes;
    }

    public void setNextLocation(Location newLocation) {
        renderableWasUpdated();
    }
}
