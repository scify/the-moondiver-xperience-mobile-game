package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.Renderable;
import org.scify.moonwalker.app.ui.actors.ActionButton;

import java.util.Date;

public class CockpitRenderable extends Renderable {

    public final String ENGINE_PERFORMANCE_LABEL = "Αποδοση κινητηρα";
    public final String REMAINING_ENERGY_LABEL = "Διαθεσιμη ενεργεια";
    public final String DESTINATION_DISTANCE_LABEL = "Αποσταση προορισμου";
    public final String POSITION_LABEL = "Τοποθεσια";
    public final String DAYS_LEFT_LABEL = "Days left:";

    protected String enginePerformanceValue = "0";
    protected String remainingEnergyValue = "0";
    protected String destinationDistanceValue = "0";
    protected String positionValue = "0";
    protected String daysLeftValue = "0";

    protected ActionButton navigationButton;
    protected ActionButton vesselButton;
    protected ActionButton mapButton;
    protected ActionButton contactButton;

    protected long renderableLastUpdated = new Date().getTime();

    public CockpitRenderable(float xPos, float yPos, float width, float height, String type, String id) {
        super(xPos, yPos, width, height, type, id);
    }

    public String getEnginePerformanceValue() {
        return enginePerformanceValue;
    }

    public void setEnginePerformanceValue(String enginePerformanceValue) {
        this.enginePerformanceValue = enginePerformanceValue;
        this.renderableLastUpdated = new Date().getTime();
    }

    public String getRemainingEnergyValue() {
        return remainingEnergyValue;
    }

    public void setRemainingEnergyValue(String remainingEnergyValue) {
        this.remainingEnergyValue = remainingEnergyValue;
        this.renderableLastUpdated = new Date().getTime();
    }

    public String getDestinationDistanceValue() {
        return destinationDistanceValue;
    }

    public void setDestinationDistanceValue(String destinationDistanceValue) {
        this.destinationDistanceValue = destinationDistanceValue;
        this.renderableLastUpdated = new Date().getTime();
    }

    public String getPositionValue() {
        return positionValue;
    }

    public void setPositionValue(String positionValue) {
        this.positionValue = positionValue;
        this.renderableLastUpdated = new Date().getTime();
    }

    public ActionButton getNavigationButton() {
        return navigationButton;
    }

    public void setNavigationButton(ActionButton navigationButton) {
        this.navigationButton = navigationButton;
    }

    public ActionButton getVesselButton() {
        return vesselButton;
    }

    public void setVesselButton(ActionButton vesselButton) {
        this.vesselButton = vesselButton;
    }

    public ActionButton getMapButton() {
        return mapButton;
    }

    public void setMapButton(ActionButton mapButton) {
        this.mapButton = mapButton;
    }

    public ActionButton getContactButton() {
        return contactButton;
    }

    public void setContactButton(ActionButton contactButton) {
        this.contactButton = contactButton;
    }

    public String getDaysLeftValue() {
        return daysLeftValue;
    }

    public void setDaysLeftValue(String daysLeftValue) {
        this.daysLeftValue = daysLeftValue;
        this.renderableLastUpdated = new Date().getTime();
    }

    public long getRenderableLastUpdated() {
        return renderableLastUpdated;
    }

    public void setRenderableLastUpdated(long renderableLastUpdated) {
        this.renderableLastUpdated = renderableLastUpdated;
    }
}
