package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.Renderable;
import org.scify.moonwalker.app.ui.actors.ActionButton;

public class MapLocationRenderable extends Renderable{

    protected String destinationName;
    protected float destinationDistance;
    protected String destinationInfo;
    protected ActionButton button;

    public MapLocationRenderable(float xPos, float yPos, float width, float height, String id) {
        super(xPos, yPos, width, height, "map_location", id);
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public void setDestinationDistance(float destinationDistance) {
        this.destinationDistance = destinationDistance;
    }

    public void setDestinationInfo(String destinationInfo) {
        this.destinationInfo = destinationInfo;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public float getDestinationDistance() {
        return destinationDistance;
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
