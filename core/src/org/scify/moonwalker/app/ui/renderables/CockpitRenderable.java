package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.Renderable;
import org.scify.moonwalker.app.ui.actors.ActionButton;

public class CockpitRenderable extends Renderable {

    public final String ENGINE_EFFICIENCY_LABEL = "Αποδοση κινητηρα";
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

    public CockpitRenderable(float xPos, float yPos, float width, float height, String type, String id) {
        super(xPos, yPos, width, height, type, id);
    }

    public String getEngineEfficiencyValue() {
        return enginePerformanceValue;
    }

    public void setEnginePerformanceValue(String enginePerformanceValue) {
        this.enginePerformanceValue = enginePerformanceValue;
        componentWasUpdated();
    }

    public String getRemainingEnergyValue() {
        return remainingEnergyValue;
    }

    public void setRemainingEnergyValue(String remainingEnergyValue) {
        this.remainingEnergyValue = remainingEnergyValue;
        componentWasUpdated();
    }

    public String getDestinationDistanceValue() {
        return destinationDistanceValue;
    }

    public void setDestinationDistanceValue(String destinationDistanceValue) {
        this.destinationDistanceValue = destinationDistanceValue;
        componentWasUpdated();
    }

    public String getPositionValue() {
        return positionValue;
    }

    public void setPositionValue(String positionValue) {
        this.positionValue = positionValue;
        componentWasUpdated();
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
        componentWasUpdated();
    }

}
