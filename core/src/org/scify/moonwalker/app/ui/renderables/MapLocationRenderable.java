package org.scify.moonwalker.app.ui.renderables;

import org.scify.moonwalker.app.game.Location;
import org.scify.engine.renderables.ActionButtonWithEffect;

public class MapLocationRenderable extends TableRenderable {

    protected Location location;
    protected String destinationInfo;
    protected ActionButtonWithEffect button;

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

    public ActionButtonWithEffect getButton() {
        return button;
    }

    public void setButton(ActionButtonWithEffect button) {
        this.button = button;
    }
}
