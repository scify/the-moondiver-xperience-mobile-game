package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.Renderable;
import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.moonwalker.app.ui.actors.ActionButton;

public class SpaceshipControllerRenderable extends Renderable{

    public final String CURRENT_MOON_PHASE_LABEL = "Παρούσα φάση";
    public final String UNITS_LABEL = "Μονάδες";
    public final String NEXT_NIGHTS_LABEL = "Επόμενες νύχτες";
    public final String MOTOR_EFFICIENCY_LABEL = "Απόδοση κινητήρα";
    public final String REMAINING_ENERGY_LABEL = "Διαθέσιμη ενέργεια";
    public final String DESTINATION_DISTANCE_LABEL= "Απόσταση προορισμού";

    protected ActionButton calculatorButton;
    protected ActionButton travelButton;
    protected ActionButton chargeButton;
    protected ActionButton escapeButton;

    protected String currentMoonPhaseImgPath;
    protected String nextMoonPhaseImgPath1;
    protected String nextMoonPhaseImgPath2;

    protected int currentMoonPhase;
    protected int nextMoonPhase1;
    protected int nextMoonPhase2;
    protected int motorEfficiency;
    protected int remainingEnergy;
    protected int destinationDistance;

    public SpaceshipControllerRenderable(float xPos, float yPos, float width, float height, String id) {
        super(xPos, yPos, width, height, "spaceship_controller", id);
        calculatorButton = new ActionButton("image_button", "calculator_button");
        calculatorButton.setHeight(100);
        //TODO update
        calculatorButton.setUserAction(new UserAction(UserActionCode.FINISH_EPISODE));
        calculatorButton.setImgPath("img/button.png");
        travelButton = new ActionButton("image_button", "travel_button");
        travelButton.setHeight(100);
        travelButton.setUserAction(new UserAction(UserActionCode.FINISH_EPISODE));
        travelButton.setImgPath("img/button.png");
        chargeButton = new ActionButton("image_button", "charge_button");
        chargeButton.setUserAction(new UserAction(UserActionCode.FINISH_EPISODE));
        chargeButton.setImgPath("img/button.png");
        chargeButton.setHeight(100);
    }

    public ActionButton getCalculatorButton() {
        return calculatorButton;
    }

    public ActionButton getTravelButton() {
        return travelButton;
    }

    public ActionButton getChargeButton() {
        return chargeButton;
    }

    public void setCurrentMoonPhase(int currentMoonPhase) {
        this.currentMoonPhase = currentMoonPhase;
    }

    public void setNextMoonPhase1(int nextMoonPhase1) {
        this.nextMoonPhase1 = nextMoonPhase1;
    }

    public void setNextMoonPhase2(int nextMoonPhase2) {
        this.nextMoonPhase2 = nextMoonPhase2;
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

    public int getCurrentMoonPhase() {
        return currentMoonPhase;
    }

    public int getNextMoonPhase1() {
        return nextMoonPhase1;
    }

    public int getNextMoonPhase2() {
        return nextMoonPhase2;
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

    public String getNextMoonPhaseImgPath1() {
        return nextMoonPhaseImgPath1;
    }

    public void setNextMoonPhaseImgPath1(String nextMoonPhaseImgPath1) {
        this.nextMoonPhaseImgPath1 = nextMoonPhaseImgPath1;
    }

    public String getNextMoonPhaseImgPath2() {
        return nextMoonPhaseImgPath2;
    }

    public void setNextMoonPhaseImgPath2(String nextMoonPhaseImgPath2) {
        this.nextMoonPhaseImgPath2 = nextMoonPhaseImgPath2;
    }

    public String getCurrentMoonPhaseImgPath() {
        return currentMoonPhaseImgPath;
    }

    public void setCurrentMoonPhaseImgPath(String currentMoonPhaseImgPath) {
        this.currentMoonPhaseImgPath = currentMoonPhaseImgPath;
    }
}
