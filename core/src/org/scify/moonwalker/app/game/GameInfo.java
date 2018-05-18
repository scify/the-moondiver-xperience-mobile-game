package org.scify.moonwalker.app.game;

public class GameInfo {

    protected int currentDay;
    protected int motorEfficiency;
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
    protected boolean contactRequestFlag;
    protected boolean mapRequestFlag;
    protected boolean inventoryIncreased;
    // we keep track of the last answer of the user to the quiz
    protected boolean lastQuizAnswerCorrect;
    protected String conversationFileForContactScreen;

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

    private GameInfo() {
        moonPhasesController = new MoonPhasesController();
        currentDay = 1;
        initialDaysToSuccessfullyCompleteGame = 90;
        motorEfficiency = 10;
        remainingEnergy = 0;
        LocationController lc = new LocationController();
        currentLocation = lc.getLocations().get(0);
        setMoonPhases();
        //TODO set to unset
        selectedPlayer = SelectedPlayer.girl;
        contactRequestFlag = true;
        mapRequestFlag = false;
        setCurrentLocation(lc.getLocations().get(5));
        setNextAllowedLocation(lc.getLocations().get(0));
        setPreviousTravelPercentageComplete(0);
        setNextTravelPercentagePossible(100);
        //inventory
        inventoryItemsCounter = 0;
        inventoryIncreased = false;
    }

    public boolean getContactRequestFlag() {
        return contactRequestFlag;
    }

    public void setContactRequestFlag(Boolean contactRequestFlag) {
        this.contactRequestFlag = contactRequestFlag;
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

    public void setMotorEfficiency(int newMotorEfficiency) {
        this.motorEfficiency = motorEfficiency;
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

    public int getInventoryItemsCounter() {
        return inventoryItemsCounter;
    }

    public void setInventoryItemsCounter(int numberOfItems) {
        inventoryItemsCounter = numberOfItems;
    }

    public int increaseInventoryItemsCounter() {
        inventoryItemsCounter++;
        inventoryIncreased = false;
        return inventoryItemsCounter;
    }

    public boolean isInventoryIncreased() {
        return inventoryIncreased;
    }

    public void addNextInventoryItem() {
        inventoryIncreased = true;
    }

    public int getUnitsOfMoonPhase(MoonPhase moonPhase) {
        return moonPhasesController.getUnitsOfMoonPhase(moonPhase, inventoryItemsCounter);
    }

    public boolean isLastQuizAnswerCorrect() {
        return lastQuizAnswerCorrect;
    }

    public void setLastQuizAnswerCorrect(boolean lastQuizAnswerCorrect) {
        this.lastQuizAnswerCorrect = lastQuizAnswerCorrect;
    }

    public String getConversationFileForContactScreen() {
        return this.conversationFileForContactScreen;
    }

    public void setConversationFileForContactScreen(String conversationFileForContactScreen) {
        this.conversationFileForContactScreen = conversationFileForContactScreen;
    }
}
