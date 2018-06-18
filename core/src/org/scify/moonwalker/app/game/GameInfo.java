package org.scify.moonwalker.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.Iterator;

public class GameInfo {
    protected String selectedPlayer;
    protected int currentDay;
    protected int remainingEnergy;
    protected int initialDaysToSuccessfullyCompleteGame;
    protected int inventoryItemsCounter;
    protected int mainEpisodeCounter;
    protected Location currentLocation;
    protected Location nextLocation;
    protected Location nextAllowedLocation;
    LocationController lc;
    protected MoonPhase currentMoonPhase;
    protected MoonPhase nextMoonPhase;
    protected MoonPhase postNextMoonPhase;
    protected MoonPhasesController moonPhasesController;
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
    protected boolean showArrivalConversation;
    protected boolean isFromLoad;
    protected Preferences prefs;

    /**
     * The travel percentage complete in the LAST travel
     */
    protected float previousTravelPercentageComplete;

    /**
     * The travel percentage we will reach in the NEXT travel
     */
    protected float nextTravelPercentagePossible;


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

    public void reset() {
        selectedPlayer = SelectedPlayer.unset;
        atForest = true;
        currentDay = 1;
        mainEpisodeCounter = 0;
        initialDaysToSuccessfullyCompleteGame = 30;
        remainingEnergy = 0;
        setMoonPhases();
        resetFlags();
        setContactRequestFlag();
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
        showArrivalConversation = false;
        isFromLoad = false;
    }

    public void save() {
        System.err.println("INFO: Saving game... Episode " + String.valueOf(mainEpisodeCounter));
        prefs.putString("selectedPlayer", selectedPlayer);
        prefs.putString("currentLocationName", currentLocation.getName());
        if (nextLocation == null)
            prefs.putString("nextLocationName", "");
        else
            prefs.putString("nextLocationName", nextLocation.getName());
        if (nextAllowedLocation == null) {
            prefs.putString("nextAllowedLocationName", "");
        }else
            prefs.putString("nextAllowedLocationName", nextAllowedLocation.getName());
        prefs.putInteger("currentDay", currentDay);
        prefs.putInteger("mainEpisodeCounter", mainEpisodeCounter);
        prefs.putInteger("remainingEnergy", remainingEnergy);
        prefs.putInteger("initialDaysToSuccessfullyCompleteGame", initialDaysToSuccessfullyCompleteGame);
        prefs.putInteger("inventoryItemsCounter", inventoryItemsCounter);
        prefs.putBoolean("tutorialMode", tutorialMode);
        prefs.putBoolean("contactRequestFlag", contactRequestFlag);
        prefs.putBoolean("mapRequestFlag", mapRequestFlag);
        prefs.putBoolean("chargeRequestFlag", chargeRequestFlag);
        prefs.putBoolean("inventoryRequestFlag",inventoryRequestFlag );
        prefs.putBoolean("travelRequestFlag", travelRequestFlag);
        prefs.putBoolean("launchRequestFlag",launchRequestFlag );
        prefs.putBoolean("inventoryIncreased",inventoryIncreased );
        prefs.putBoolean("atForest", atForest);
        prefs.putBoolean("afterTravel", afterTravel);
        prefs.putBoolean("afterLocationQuizEpisode", afterLocationQuizEpisode);
        prefs.putBoolean("lastQuizSuccessFull", lastQuizSuccessFull);
        prefs.putBoolean("quizFirstTime", quizFirstTime);
        prefs.putBoolean("showArrivalConversation", showArrivalConversation);
        prefs.putBoolean("selectFirstMiddleOfNowhere", LocationController.isSelectFirstMiddleOfNowhere());
        prefs.putFloat("previousTravelPercentageComplete", previousTravelPercentageComplete);
        prefs.putFloat("nextTravelPercentagePossible", nextTravelPercentagePossible);
        prefs.flush();
        System.err.println("INFO: Saving game... Done.");
        isFromLoad = false;
    }

    protected Location getLocationGivenName(String locationName) {
        if (locationName.equals(""))
            return null;
        else {
            Location loc = null;
            Iterator<Location> lcIter = lc.locations.iterator();
            while (loc == null) {
                Location nextLocation = lcIter.next();
                if (nextLocation.getName().equals(locationName)) {
                    loc = nextLocation;
                }
            }
            return loc;
        }
    }

    public void load() {
        reset();
        System.err.println("INFO: Loading game... Done.");

        currentDay = prefs.getInteger("currentDay");
        selectedPlayer = prefs.getString("selectedPlayer");
        setCurrentLocation(getLocationGivenName(prefs.getString("currentLocationName")));
        setNextLocation(getLocationGivenName(prefs.getString("nextLocationName")));
        setNextAllowedLocation(getLocationGivenName(prefs.getString("nextAllowedLocationName")));
        if (nextLocation != null)
            getNextLocation().setDistanceInKilometers(getCurrentLocation().getDistanceFromLocation(nextLocation));
        mainEpisodeCounter = prefs.getInteger("mainEpisodeCounter");
        remainingEnergy = prefs.getInteger("remainingEnergy");
        initialDaysToSuccessfullyCompleteGame = prefs.getInteger("initialDaysToSuccessfullyCompleteGame");
        inventoryItemsCounter = prefs.getInteger("inventoryItemsCounter");
        tutorialMode = prefs.getBoolean("tutorialMode");
        contactRequestFlag = prefs.getBoolean("contactRequestFlag");
        mapRequestFlag = prefs.getBoolean("mapRequestFlag");
        chargeRequestFlag = prefs.getBoolean("chargeRequestFlag");
        inventoryRequestFlag = prefs.getBoolean("inventoryRequestFlag");
        travelRequestFlag = prefs.getBoolean("travelRequestFlag");
        launchRequestFlag = prefs.getBoolean("launchRequestFlag");
        inventoryIncreased = prefs.getBoolean("inventoryIncreased");
        atForest = prefs.getBoolean("atForest");
        afterTravel = prefs.getBoolean("afterTravel");
        afterLocationQuizEpisode = prefs.getBoolean("afterLocationQuizEpisode");
        lastQuizSuccessFull = prefs.getBoolean("lastQuizSuccessFull");
        quizFirstTime = prefs.getBoolean("quizFirstTime");
        showArrivalConversation = prefs.getBoolean("showArrivalConversation");
        LocationController.setSelectFirstMiddleOfNowhere(prefs.getBoolean("selectFirstMiddleOfNowhere"));
        previousTravelPercentageComplete = prefs.getFloat("previousTravelPercentageComplete");
        nextTravelPercentagePossible = prefs.getFloat("nextTravelPercentagePossible");
        setMoonPhases();
        isFromLoad = true;
    }

    private GameInfo() {
        tutorialMode = true;
        moonPhasesController = new MoonPhasesController();
        prefs = Gdx.app.getPreferences("default_save");
        lc = LocationController.getInstance();
        reset();
    }

    public boolean isSaveAvailable() {
        if (prefs.get().size() == 0)
            return false;
        else
            return true;
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

    public float getPreviousTravelPercentageComplete() {
        return previousTravelPercentageComplete;
    }

    public float getNextTravelPercentagePossible() {
        return nextTravelPercentagePossible;
    }

    public void setNextTravelPercentagePossible(float nextTravelPercentagePossible) {
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

    public int getMainEpisodeCounter() {
        return mainEpisodeCounter;
    }

    public void setMainEpisodeCounter(int mainEpisodeCounter) {
        this.mainEpisodeCounter = mainEpisodeCounter;
    }

    public boolean isShowArrivalConversation() { return showArrivalConversation; }

    public void setShowArrivalConversation(boolean showArrivalConversation) { this.showArrivalConversation = showArrivalConversation; }

    public boolean isFromLoad() { return isFromLoad; }

    public void dispose() {
        instance = null;
    }
}
