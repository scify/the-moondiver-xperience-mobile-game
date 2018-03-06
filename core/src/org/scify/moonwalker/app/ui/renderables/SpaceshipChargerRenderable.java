package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.Renderable;
import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.moonwalker.app.game.MoonPhase;
import org.scify.moonwalker.app.ui.actors.ActionButton;

public class SpaceshipChargerRenderable extends Renderable{

    public final String CURRENT_MOON_PHASE_LABEL = "Παρούσα φάση";
    public final String UNITS_LABEL = "Μονάδες";
    public final String NEXT_NIGHTS_LABEL = "Επόμενες νύχτες";
    public final String MOTOR_EFFICIENCY_LABEL = "Απόδοση κινητήρα";
    public final String REMAINING_ENERGY_LABEL = "Διαθέσιμη ενέργεια";
    public final String DESTINATION_DISTANCE_LABEL= "Απόσταση προορισμού";

    protected ActionButton calculatorButton;
    protected ActionButton chargeButton;
    protected ActionButton escapeButton;

    protected MoonPhase currentMoonPhase;
    protected MoonPhase nextMoonPhase;
    protected MoonPhase postNextMoonPhase;

    protected int motorEfficiency;
    protected int remainingEnergy;
    protected int destinationDistance;

    public SpaceshipChargerRenderable(float xPos, float yPos, float width, float height, String id) {
        super(xPos, yPos, width, height, "spaceship_charger", id);
        calculatorButton = new ActionButton("image_button", "calculator_button");
        calculatorButton.setHeight(40);

        calculatorButton.setUserAction(new UserAction(UserActionCode.CALCULATOR_EPISODE));
        calculatorButton.setImgPath("img/button.png");

        chargeButton = new ActionButton("image_button", "charge_button");
        //TODO update
        chargeButton.setUserAction(new UserAction(UserActionCode.FINISH_EPISODE));
        chargeButton.setImgPath("img/button.png");
        chargeButton.setHeight(40);
    }

    public ActionButton getCalculatorButton() {
        return calculatorButton;
    }

    public ActionButton getChargeButton() {
        return chargeButton;
    }

    public void setCurrentMoonPhase(MoonPhase currentMoonPhase) {
        this.currentMoonPhase = currentMoonPhase;
    }

    public void setNextMoonPhase(MoonPhase nextMoonPhase) {
        this.nextMoonPhase = nextMoonPhase;
    }

    public void setPostNextMoonPhase(MoonPhase postNextMoonPhase) {
        this.postNextMoonPhase = postNextMoonPhase;
    }

    public void setMotorEfficiency(int motorEfficiency) {
        this.motorEfficiency = motorEfficiency;
    }

    public void setRemainingEnergy(int remainingEnergy) {
        this.remainingEnergy = remainingEnergy;
    }

    public void setDestinationDistance(int destinationDistance) {
        this.destinationDistance = destinationDistance;
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

    public ActionButton getEscapeButton() {
        return escapeButton;
    }

    public void setEscapeButton(ActionButton escapeButton) {
        this.escapeButton = escapeButton;
    }
}
