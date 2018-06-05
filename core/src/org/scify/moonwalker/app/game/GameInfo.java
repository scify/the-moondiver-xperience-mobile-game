package org.scify.moonwalker.app.game;

public class GameInfo {

    protected int currentDay;
    protected int remainingEnergy;
    protected int initialDaysToSuccessfullyCompleteGame;
    protected int inventoryItemsCounter;
    protected Location currentLocation;
    protected Location nextLocation;
    protected Location nextAllowedLocation;
    protected MoonPhase currentMoonPhase;
    protected MoonPhase nextMoonPhase;
    protected MoonPhase postNextMoonPhase;
    protected MoonPhasesController moonPhasesController;
    protected String selectedPlayer;
    protected boolean tutorialMode;
    protected boolean contactRequestFlag;
    protected boolean mapRequestFlag;
    protected boolean chargeRequestFlag;
    protected boolean inventoryRequestFlag;
    protected boolean travelRequestFlag;
    protected boolean launchRequestFlag;
    protected boolean inventoryIncreased;
    protected boolean atForest;
    protected boolean afterTravel;
    protected boolean afterLocationQuizEpisode;
    protected boolean lastQuizSuccessFull;
    protected boolean BackGroundMusicPlaying;
    protected boolean quizFirstTime;

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

    private static GameInfo instance;

    public static GameInfo getInstance() {
        if (instance == null)
            instance = new GameInfo();
        return instance;
    }

    public void setQuizFirstTime(boolean quizFirstTime) { this.quizFirstTime = quizFirstTime; }

    private GameInfo() {
        moonPhasesController = new MoonPhasesController();
        atForest = true;
        currentDay = 1;
        initialDaysToSuccessfullyCompleteGame = 30;
        remainingEnergy = 0;
        LocationController lc = LocationController.getInstance();
        setMoonPhases();
        tutorialMode = true;
        resetFlags();
        setContactRequestFlag();
        //setMapRequestFlag();
        setCurrentLocation(lc.getInitialLocation());
        setNextAllowedLocation(lc.getAfterInitialLocation());
        //inventory
        inventoryItemsCounter = 0;
        inventoryIncreased = false;
        afterTravel = false;
        afterLocationQuizEpisode = false;
        lastQuizSuccessFull = false;
        BackGroundMusicPlaying = false;
        quizFirstTime = true;
    }

    public boolean isQuizFirstTime () { return quizFirstTime; }


    public int getCurrentDay() {
        return currentDay;
    }

    public void dayPassed() {
        currentDay++;
        setMoonPhases();
    }

    protected void setMoonPhases() {
        currentMoonPhase = moonPhasesController.getMoonPhaseForDay(currentDay);
        nextMoonPhase = moonPhasesController.getMoonPhaseForDay(currentDay + 1);
        postNextMoonPhase = moonPhasesController.getMoonPhaseForDay(currentDay + 2);
    }

    public int getMotorEfficiency() {
        if (inventoryItemsCounter == 0)
            return 10;
        else if (inventoryItemsCounter == 1)
            return 15;
        else
            return 20;
    }

    public int getRemainingEnergy() {
        return remainingEnergy;
    }

    public void setRemainingEnergy(int remainingEnergy) {
        this.remainingEnergy = remainingEnergy;
    }

    public void addEnergy(int remainingEnergy) {
        this.remainingEnergy += remainingEnergy;
    }

    public int getNextLocationDistance() {
        return nextLocation.getDistanceInKilometers();
    }

    public void setNextLocation(Location location) {
        this.nextLocation = location;
    }

    public Location setCurrentLocation(Location location) {
        this.currentLocation = location;
        return this.currentLocation;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public int getDaysLeftForDestination() {
        return initialDaysToSuccessfullyCompleteGame - currentDay;
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

    public double getNextTravelPercentagePossible() {
        return nextTravelPercentagePossible;
    }

    public void setNextTravelPercentagePossible(double nextTravelPercentagePossible) {
        // the previous travel is equal as the previous value of the next travel percentage
        this.previousTravelPercentageComplete = this.nextTravelPercentagePossible;
        this.nextTravelPercentagePossible = nextTravelPercentagePossible;
    }

    public int getInventoryItemsCounter() {
        return inventoryItemsCounter;
    }

    public int increaseInventoryItemsCounter() {
        inventoryItemsCounter++;
        inventoryIncreased = false;
        return inventoryItemsCounter;
    }

    public boolean isInventoryIncreased() {
        return inventoryIncreased;
    }

    public int getUnitsOfMoonPhase(MoonPhase moonPhase) {
        return moonPhasesController.getUnitsOfMoonPhase(moonPhase, inventoryItemsCounter);
    }

    public boolean isAtForest() {
        return atForest;
    }

    public void setAtForest(boolean atForest) {
        this.atForest = atForest;
    }

    public boolean isGameFullySuccessfullyCompleted() {
        return currentDay <= initialDaysToSuccessfullyCompleteGame;
    }

    public boolean isTutorialMode() {
        return tutorialMode;
    }

    public void setTutorialMode(boolean tutorialMode) {
        this.tutorialMode = tutorialMode;
    }

    public void resetTravelState() {
        previousTravelPercentageComplete = 0;
        nextTravelPercentagePossible = 0;
        afterTravel = false;
    }

    public void setAfterTravel(boolean afterTravel) {
        this.afterTravel = afterTravel;
    }

    public boolean isAfterTravel() {
        return afterTravel;
    }

    public boolean isAfterLocationQuizEpisode() {
        return afterLocationQuizEpisode;
    }

    public void setAfterLocationQuizEpisode(boolean afterLocationQuizEpisode) {
        this.afterLocationQuizEpisode = afterLocationQuizEpisode;
    }

    public boolean isLastQuizSuccessFull() {
        return lastQuizSuccessFull;
    }

    public void setLastQuizSuccessFull(boolean lastQuizSuccessFull) {
        this.lastQuizSuccessFull = lastQuizSuccessFull;
    }

    public void resetFlags () {
        contactRequestFlag = false;
        mapRequestFlag = false;
        chargeRequestFlag = false;
        inventoryRequestFlag = false;
        travelRequestFlag = false;
        launchRequestFlag = false;
    }

    public boolean isAnyFlagOn () {
        if (contactRequestFlag)
            return true;
        else if (mapRequestFlag)
            return true;
        else if (chargeRequestFlag)
            return true;
        else if (inventoryRequestFlag)
            return true;
        else if (travelRequestFlag)
            return true;
        else if (launchRequestFlag)
            return true;
        else
            return false;
    }

    public boolean isContactRequestFlag() {
        return contactRequestFlag;
    }

    public boolean isMapRequestFlag() {
        return mapRequestFlag;
    }

    public boolean isChargeRequestFlag() {
        return chargeRequestFlag;
    }

    public boolean isInventoryRequestFlag() {
        return inventoryRequestFlag;
    }

    public boolean isTravelRequestFlag() {
        return travelRequestFlag;
    }

    public boolean isLaunchRequestFlag() {
        return launchRequestFlag;
    }

    public void setMapRequestFlag() {
        resetFlags();
        mapRequestFlag = true;
    }

    public void setContactRequestFlag() {
        resetFlags();
        contactRequestFlag = true;
    }

    public void setChargeRequestFlag() {
        resetFlags();
        chargeRequestFlag = true;
    }

    public void setInventoryRequestFlag() {
        resetFlags();
        inventoryRequestFlag = true;
    }

    public void setTravelRequestFlag() {
        resetFlags();
        travelRequestFlag = true;
    }

    public void setLaunchRequestFlag() {
        resetFlags();
        launchRequestFlag = true;
    }

    public void setInventoryIncreased() {
        inventoryIncreased = true;
    }

    public boolean isBackGroundMusicPlaying() {
        return BackGroundMusicPlaying;
    }

    public void setBackGroundMusicPlaying(boolean backGroundMusicPlaying) {
        BackGroundMusicPlaying = backGroundMusicPlaying;
    }
}
