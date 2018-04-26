package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.ActionButtonWithEffect;

public class CockpitRenderable extends Renderable {

    public final String MOTOR_EFFICIENCY_IMG_PATH = "img/cockpit/motor_efficiency.png";
    public final String REMAINING_ENERGY_IMG_PATH = "img/cockpit/remaining_energy.png";
    public final String DESTINATION_DISTANCE_IMG_PATH = "img/cockpit/destination_distance.png";
    public final String POSITION_LABEL_IMG_PATH = "img/cockpit/location.png";
    public final String DAYS_LEFT_IMG_PATH = "img/cockpit/days_left.png";

    protected String motorEfficiencyValue;
    protected String remainingEnergyValue;
    protected int destinationDistanceValue;
    protected String positionValue;
    protected String daysLeftValue;

    protected ActionButtonWithEffect navigateButton;
    protected ActionButtonWithEffect launchButton;
    protected ActionButtonWithEffect chargeButton;
    protected ActionButtonWithEffect spaceshipPartsButton;
    protected ActionButtonWithEffect mapButton;

    protected boolean contactButtonIsLighted;
    protected ActionButtonWithEffect contactButtonSimple;
    protected ActionButtonWithEffect contactButtonLighted;


    public CockpitRenderable(float xPos, float yPos, float width, float height, String type, String id) {
        super(xPos, yPos, width, height, type, id);
        setImgPath("img/cockpit/bg.png");
        contactButtonIsLighted = false;
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

    public int getDestinationDistanceValue() {
        return destinationDistanceValue;
    }

    public void setDestinationDistanceValue(int destinationDistanceValue) {
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

    public ActionButtonWithEffect getNavigateButton() {
        return navigateButton;
    }

    public ActionButtonWithEffect getLaunchButton() {
        return launchButton;
    }

    public void setNavigateButton(ActionButtonWithEffect navigateButton) {
        this.navigateButton = navigateButton;
    }

    public void setLaunchButton(ActionButtonWithEffect launchButton) {
        this.launchButton = launchButton;
    }

    public ActionButtonWithEffect getSpaceshipPartsButton() {
        return spaceshipPartsButton;
    }

    public void setSpaceshipPartsButton(ActionButtonWithEffect spaceshipPartsButton) {
        this.spaceshipPartsButton = spaceshipPartsButton;
    }

    public ActionButtonWithEffect getMapButton() {
        return mapButton;
    }

    public void setMapButton(ActionButtonWithEffect mapButton) {
        this.mapButton = mapButton;
    }

    public boolean isContactButtonLighted() {
        return contactButtonIsLighted;
    }

    public ActionButtonWithEffect getContactButtonSimple() {
        return contactButtonSimple;
    }

    public ActionButtonWithEffect getContactButtonLighted() {
        return contactButtonLighted;
    }

    public void setContactButtons(ActionButtonWithEffect contactButtonSimple, ActionButtonWithEffect contactButtonLighted) {
        this.contactButtonSimple = contactButtonSimple;
        this.contactButtonLighted = contactButtonLighted;
    }

    public void toogleContactButton() {
        if (contactButtonIsLighted) {
            contactButtonIsLighted = false;
        }else {
            contactButtonIsLighted = true;
        }
        renderableWasUpdated();
    }

    public String getDaysLeftValue() {
        return daysLeftValue;
    }

    public void setDaysLeftValue(String daysLeftValue) {
        this.daysLeftValue = daysLeftValue;
        renderableWasUpdated();
    }

    public ActionButtonWithEffect getChargeButton() {
        return chargeButton;
    }

    public void setChargeButton(ActionButtonWithEffect chargeButton) {
        this.chargeButton = chargeButton;
    }
}
