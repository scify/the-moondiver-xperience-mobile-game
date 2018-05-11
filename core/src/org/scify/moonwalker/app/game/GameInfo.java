package org.scify.moonwalker.app.game;

public class GameInfo {

    protected int currentDay;
    protected int motorEfficiency;
    protected int remainingEnergy;
    protected Location currentLocation;
    protected Location nextLocation;
    protected Location nextAllowedLocation;

    /**
     * The travel percentage complete in the LAST travel
     */
    protected double previousTravelPercentageComplete;

    /**
     * The travel percentage we will reach in the NEXT travel
     */
    protected double nextTravelPercentagePossible;


    public Location getNextAllowedLocation() {
        return nextAllowedLocation;
    }

    public void setNextAllowedLocation(Location nextAllowedLocation) {
        this.nextAllowedLocation = nextAllowedLocation;
    }

    protected int daysLeftForDestination;
    protected MoonPhase currentMoonPhase;
    protected MoonPhase nextMoonPhase;
    protected MoonPhase postNextMoonPhase;
    protected MoonPhasesController moonPhasesController;
    protected String selectedPlayer;
    protected boolean contactRequestFlag;
    protected boolean mapRequestFlag;

    private static GameInfo instance;

    public static GameInfo getInstance() {
        if (instance == null)
            instance = new GameInfo();
        return instance;
    }

    private GameInfo() {
        moonPhasesController = new MoonPhasesController();
        currentDay = 1;
        motorEfficiency = 10;
        remainingEnergy = 0;
        daysLeftForDestination = 90;
        LocationController lc = new LocationController();
        currentLocation = lc.getLocations().get(0);
        setMoonPhases();
        selectedPlayer = SelectedPlayer.unset;
        contactRequestFlag = true;
        mapRequestFlag = false;
        setCurrentLocation(lc.getInitialLocation());
        setNextAllowedLocation(lc.getInitialDestination());
    }

    public boolean getContactRequestFlag () { return contactRequestFlag; }

    public void setContactRequestFlag (Boolean contactRequestFlag) { this.contactRequestFlag = contactRequestFlag; }

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

    public void setSelectedPlayer(String selectedPlayer) {
        this.selectedPlayer = selectedPlayer;
    }

    public String getSelectedPlayer() {
        return selectedPlayer;
    }

    public double getPreviousTravelPercentageComplete() {
        return previousTravelPercentageComplete;
    }

    public void setPreviousTravelPercentageComplete(double previousTravelPercentageComplete) {
        this.previousTravelPercentageComplete = previousTravelPercentageComplete;
    }

    public double getNextTravelPercentagePossible() {
        return nextTravelPercentagePossible;
    }

    public void setNextTravelPercentagePossible(double nextTravelPercentagePossible) {
        this.nextTravelPercentagePossible = nextTravelPercentagePossible;
    }

    public boolean getMapRequestFlag() {
        return mapRequestFlag;
    }

    public void setMapRequestFlag(boolean mapRequestFlag) {
        this.mapRequestFlag = mapRequestFlag;
    }

}
