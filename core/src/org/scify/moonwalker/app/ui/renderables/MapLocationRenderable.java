package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.moonwalker.app.game.Location;

public class MapLocationRenderable extends FadingTableRenderable {

    protected Location location;
    protected String destinationInfo;
    protected ActionButtonRenderable button;

    public MapLocationRenderable(float xPos, float yPos, float width, float height, String id) {
        super(xPos, yPos, width, height, "map_location", id,  "");
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setDestinationInfo(String destinationInfo) {
        this.destinationInfo = destinationInfo;
    }

    public String getDestinationName() {
        return location.getName();
    }

    public float getDestinationDistance() {
        return location.getDistanceInKilometers();
    }

    public String getDestinationInfo() {
        return destinationInfo;
    }

    public ActionButtonRenderable getButton() {
        return button;
    }

    public void setButton(ActionButtonRenderable button) {
        this.button = button;
    }
}
