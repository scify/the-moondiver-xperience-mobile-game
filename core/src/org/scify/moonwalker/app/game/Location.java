package org.scify.moonwalker.app.game;

import java.util.HashMap;
import java.util.Map;

public class Location {

    protected String name;
    protected String imgPath;
    protected int distanceInKilometers;
    protected int posX;
    protected int posY;
    protected Map<Location, Integer> distanceFromOtherLocations;
    protected String mission;
    protected String conversationPath;
    protected String conversationSuccessFilePath;
    protected String conversationFailureFilePath;
    protected String conversationArrivalFilePath;

    public Location(String name, String imgUrl, int posX, int posY, String mission, String conversationPath, String conversationSuccessFilePath, String conversationFailureFilePath, String conversationArrivelFilePath) {
        this.name = name;
        this.imgPath = imgUrl;
        this.posX = posX;
        this.posY = posY;
        this.mission = mission;
        this.conversationPath = conversationPath;
        distanceFromOtherLocations = new HashMap<>();
        this.conversationSuccessFilePath = conversationSuccessFilePath;
        this.conversationFailureFilePath = conversationFailureFilePath;
        this.conversationArrivalFilePath = conversationArrivelFilePath;
    }

    public int getDistanceInKilometers() {
        return distanceInKilometers;
    }

    public void setDistanceInKilometers(int distanceInKilometers) {
        this.distanceInKilometers = distanceInKilometers;
    }

    public String getName() {
        return name;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void addOtherLocationAndDistance(Location location, int distance) {
        distanceFromOtherLocations.put(location, distance);
    }

    public int getDistanceFromLocation(Location location) {
        if(location == this)
            return 0;
        if(location == null)
            return 5000;
        return distanceFromOtherLocations.get(location);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Location))
            return false;

        return getName().equals(((Location)obj).getName());
    }

    @Override
    public String toString() {
        return String.format("%s. x %d y %d, mission: %s", getName(), getPosX(), getPosY(), getMission());
    }

    public void setDistanceToLocation(Location l, int distanceInKilometers) {
        distanceFromOtherLocations.put(l, distanceInKilometers);
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    public String getMission() {
        return mission;
    }

    public String getEpisodeBackgroundImagePath() {
        return imgPath + "bg.png";
    }

    public String getFlagImagePath() {
        return imgPath + "left_tablet.png";
    }

    public String getCockpitBG() {
        return imgPath + "cockpit_bg.png";
    }

    public String getConversationBG() {
        return imgPath + "conversation_bg.png";
    }

    public String getConversationPath() {
        return conversationPath;
    }

    public String getConversationSuccessFilePath() {
        return conversationSuccessFilePath;
    }

    public String getConversationFailureFilePath() {
        return conversationFailureFilePath;
    }

    public String getConversationArrivalFilePath() {
        return conversationArrivalFilePath;
    }
}
