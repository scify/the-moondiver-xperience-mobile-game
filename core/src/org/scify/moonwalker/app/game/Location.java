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

    public String getMission() {
        return mission;
    }

    public Location(String name, String imgUrl, int posX, int posY, String mission) {
        this.name = name;
        this.imgPath = imgUrl;
        this.posX = posX;
        this.posY = posY;
        this.mission = mission;
        distanceFromOtherLocations = new HashMap<>();
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

    public String getImgPath() {
        return imgPath;
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
}
