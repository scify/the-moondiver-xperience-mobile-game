package org.scify.moonwalker.app.game;

public class GameInfo {

    protected int currentDay;
    protected int motorEfficiency;
    protected int remainingEnergy;
    protected int currentDestinationDistance;
    protected String currentDestinationName;
    protected int daysLeftForDestination;
    protected MoonPhase currentMoonPhase;
    protected MoonPhase nextMoonPhase;
    protected MoonPhase postNextMoonPhase;
    protected MoonPhasesController moonPhasesController;

    private static GameInfo instance;

    public static GameInfo getInstance() {
        if(instance == null)
            instance = new GameInfo();
        return instance;
    }

    private GameInfo() {
        moonPhasesController = new MoonPhasesController();
        currentDay = 0;
        motorEfficiency = 0;
        remainingEnergy = 0;
        currentDestinationDistance = -1;
        currentDestinationName = "";
        daysLeftForDestination = -1;
        currentMoonPhase = moonPhasesController.getMoonPhaseForDay(currentDay);
        nextMoonPhase = moonPhasesController.getMoonPhaseForDay(currentDay + 1);
        postNextMoonPhase = moonPhasesController.getMoonPhaseForDay(currentDay + 2);
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void dayPassed() {
        currentDay++;
        currentMoonPhase = moonPhasesController.getMoonPhaseForDay(currentDay);
        nextMoonPhase = moonPhasesController.getMoonPhaseForDay(currentDay + 1);
        postNextMoonPhase = moonPhasesController.getMoonPhaseForDay(currentDay + 2);
    }

    public int getMotorEfficiency() {
        return motorEfficiency;
    }

    public void addMotorEfficiencyUnits(int units) {
        this.motorEfficiency += units;
    }

    public int getRemainingEnergy() {
        return remainingEnergy;
    }

    public void setRemainingEnergy(int remainingEnergy) {
        this.remainingEnergy = remainingEnergy;
    }

    public int getCurrentDestinationDistance() {
        return currentDestinationDistance;
    }

    public void setCurrentDestinationDistance(int currentDestinationDistance) {
        this.currentDestinationDistance = currentDestinationDistance;
    }

    public String getCurrentDestinationName() {
        return currentDestinationName;
    }

    public void setCurrentDestinationName(String currentDestinationName) {
        this.currentDestinationName = currentDestinationName;
    }

    public int getDaysLeftForDestination() {
        return daysLeftForDestination;
    }

    public void setDaysLeftForDestination(int daysLeftForDestination) {
        this.daysLeftForDestination = daysLeftForDestination;
    }

    public MoonPhase getCurrentMoonPhase() {
        return currentMoonPhase;
    }

    public MoonPhase getNextMoonPhase() {
        return nextMoonPhase;
    }

    public MoonPhase getPostNextMoonPhase() {
        return postNextMoonPhase;
    }
}
