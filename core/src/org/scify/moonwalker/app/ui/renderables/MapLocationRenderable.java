package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.Renderable;
import org.scify.moonwalker.app.game.Location;
import org.scify.moonwalker.app.ui.actors.ActionButton;

public class MapLocationRenderable extends Renderable{

    protected Location location;
    protected String destinationInfo;
    protected ActionButton button;

    public MapLocationRenderable(float xPos, float yPos, float width, float height, String id) {
        super(xPos, yPos, width, height, "map_location", id);
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

    public ActionButton getButton() {
        return button;
    }

    public void setButton(ActionButton button) {
        this.button = button;
    }
}
