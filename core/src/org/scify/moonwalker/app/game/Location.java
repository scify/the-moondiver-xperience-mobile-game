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

    public Location(String name, String imgUrl, int posX, int posY) {
        this.name = name;
        this.imgPath = imgUrl;
        this.posX = posX;
        this.posY = posY;
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
}
