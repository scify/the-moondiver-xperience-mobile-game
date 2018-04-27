package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.moonwalker.app.game.MoonPhase;
import org.scify.engine.renderables.ActionButtonWithEffect;

public class SpaceshipChargerRenderable extends TableRenderable {

    public final String CURRENT_MOON_PHASE_LABEL = "Παρούσα φάση";
    public final String UNITS_LABEL = "Μονάδες";
    public final String NEXT_NIGHTS_LABEL = "Επόμενες νύχτες";
    public final String MOTOR_EFFICIENCY_LABEL = "Απόδοση κινητήρα";
    public final String REMAINING_ENERGY_LABEL = "Διαθέσιμη ενέργεια";
    public final String DESTINATION_DISTANCE_LABEL= "Απόσταση προορισμού";

    protected ActionButtonWithEffect calculatorButton;
    protected ActionButtonWithEffect chargeButton;
    protected ActionButtonWithEffect escapeButton;

    protected MoonPhase currentMoonPhase;
    protected MoonPhase nextMoonPhase;
    protected MoonPhase postNextMoonPhase;

    protected int motorEfficiency;
    protected int remainingEnergy;
    protected int destinationDistance;

    public SpaceshipChargerRenderable(float xPos, float yPos, float width, float height, String id) {
        super(xPos, yPos, width, height, "spaceship_charger", id, "");
        remainingEnergy = 0;
        calculatorButton = new ActionButtonWithEffect("image_button", "calculator_button");
        calculatorButton.setHeight(40);

        calculatorButton.setUserAction(new UserAction(UserActionCode.CALCULATOR_EPISODE));
        calculatorButton.setImgPath("img/button.png");

        chargeButton = new ActionButtonWithEffect("image_button", "charge_button");
        //TODO update
        chargeButton.setUserAction(new UserAction(UserActionCode.CHARGE_SPACESHIP_PASS_DAY));
        chargeButton.setImgPath("img/button.png");
        chargeButton.setHeight(40);
    }

    public ActionButtonWithEffect getCalculatorButton() {
        return calculatorButton;
    }

    public ActionButtonWithEffect getChargeButton() {
        return chargeButton;
    }

    public void setCurrentMoonPhase(MoonPhase currentMoonPhase) {
        this.currentMoonPhase = currentMoonPhase;
        renderableWasUpdated();
    }

    public void setNextMoonPhase(MoonPhase nextMoonPhase) {
        this.nextMoonPhase = nextMoonPhase;
        renderableWasUpdated();
    }

    public void setPostNextMoonPhase(MoonPhase postNextMoonPhase) {
        this.postNextMoonPhase = postNextMoonPhase;
        renderableWasUpdated();
    }

    public void setMotorEfficiency(int motorEfficiency) {
        this.motorEfficiency = motorEfficiency;
    }

    public void addEnergy(int energy) {
        this.remainingEnergy += energy;
        renderableWasUpdated();
    }

    public void setRemainingEnergy(int energy) {
        this.remainingEnergy = energy;
        renderableWasUpdated();
    }

    public void setDestinationDistance(int destinationDistance) {
        this.destinationDistance = destinationDistance;
        renderableWasUpdated();
    }

    public int getCurrentMoonPhaseUnits() {
        return currentMoonPhase.getEnergyUnits();
    }

    public int getNextMoonPhaseUnits() {
        return nextMoonPhase.getEnergyUnits();
    }

    public int getPostNextMoonPhaseUnits() {
        return postNextMoonPhase.getEnergyUnits();
    }

    public int getMotorEfficiency() {
        return motorEfficiency;
    }

    public int getRemainingEnergy() {
        return remainingEnergy;
    }

    public int getDestinationDistance() {
        return destinationDistance;
    }

    public String getNextMoonPhaseImgPath() {
        return nextMoonPhase.getImgPath();
    }

    public String getPostNextMoonPhaseImgPath() {
        return postNextMoonPhase.getImgPath();
    }

    public String getCurrentMoonPhaseImgPath() {
        return currentMoonPhase.getImgPath();
    }

    public ActionButtonWithEffect getEscapeButton() {
        return escapeButton;
    }

    public void setEscapeButton(ActionButtonWithEffect escapeButton) {
        this.escapeButton = escapeButton;
        renderableWasUpdated();
    }
}
