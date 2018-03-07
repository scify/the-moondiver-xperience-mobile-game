package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.Renderable;
import org.scify.moonwalker.app.ui.actors.ActionButton;

public class CockpitRenderable extends Renderable {

    public final String MOTOR_EFFICIENCY_IMG_PATH = "img/motor_efficiency.png";
    public final String REMAINING_ENERGY_IMG_PATH = "img/remaining_energy.png";
    public final String DESTINATION_DISTANCE_IMG_PATH = "img/destination_distance.png";
    public final String POSITION_LABEL_IMG_PATH = "img/location.png";
    public final String DAYS_LEFT_LABEL = "Days left:";

    protected String motorEfficiencyValue;
    protected String remainingEnergyValue;
    protected String destinationDistanceValue;
    protected String positionValue;
    protected String daysLeftValue;

    protected ActionButton launchButton;
    protected ActionButton chargeButton;
    protected ActionButton spaceshipPartsButton;
    protected ActionButton mapButton;
    protected ActionButton contactButton;

    public CockpitRenderable(float xPos, float yPos, float width, float height, String type, String id) {
        super(xPos, yPos, width, height, type, id);
    }

    public String getMotorEfficiencyValue() {
        return motorEfficiencyValue;
    }

    public void setMotorEfficiencyValue(String motorEfficiencyValue) {
        this.motorEfficiencyValue = motorEfficiencyValue;
        renderableWasUpdated();
    }

    public String getRemainingEnergyValue() {
        return remainingEnergyValue;
    }

    public void setRemainingEnergyValue(String remainingEnergyValue) {
        this.remainingEnergyValue = remainingEnergyValue;
        renderableWasUpdated();
    }

    public String getDestinationDistanceValue() {
        return destinationDistanceValue;
    }

    public void setDestinationDistanceValue(String destinationDistanceValue) {
        this.destinationDistanceValue = destinationDistanceValue;
        renderableWasUpdated();
    }

    public String getPositionValue() {
        return positionValue;
    }

    public void setPositionValue(String positionValue) {
        this.positionValue = positionValue;
        renderableWasUpdated();
    }

    public ActionButton getLaunchButton() {
        return launchButton;
    }

    public void setLaunchButton(ActionButton launchButton) {
        this.launchButton = launchButton;
    }

    public ActionButton getSpaceshipPartsButton() {
        return spaceshipPartsButton;
    }

    public void setSpaceshipPartsButton(ActionButton spaceshipPartsButton) {
        this.spaceshipPartsButton = spaceshipPartsButton;
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
        renderableWasUpdated();
    }

    public ActionButton getChargeButton() {
        return chargeButton;
    }

    public void setChargeButton(ActionButton chargeButton) {
        this.chargeButton = chargeButton;
    }
}
