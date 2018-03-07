package org.scify.moonwalker.app.game;

public class GameInfo {

    protected int currentDay;
    protected int motorEfficiency;
    protected int remainingEnergy;
    protected Location currentLocation;
    protected Location nextLocation;
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
        daysLeftForDestination = -1;
        setMoonPhases();
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void dayPassed() {
        currentDay++;
        remainingEnergy += currentMoonPhase.energyUnits;
        setMoonPhases();
    }

    protected void setMoonPhases() {
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

    public int getNextLocationDistance() {
        return nextLocation.getDistanceInKilometers();
    }

    public void setNextLocation(Location location) {
        this.nextLocation = location;
    }

    public String getCurrentLocationName() {
        return currentLocation.getName();
    }

    public void setCurrentLocation(Location location) {
        this.currentLocation = location;
    }

    public Location getCurrentLocation() {
        return currentLocation;
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

    public Location getNextLocation() {
        return nextLocation;
    }
}
