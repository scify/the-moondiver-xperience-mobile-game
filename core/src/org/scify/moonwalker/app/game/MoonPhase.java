package org.scify.moonwalker.app.game;

public class MoonPhase {

    protected String imgPath;
    protected int energyUnits;
    protected int lastingDays;

    public MoonPhase(String imgPath, int energyUnits, int lastingDays) {
        this.imgPath = imgPath;
        this.energyUnits = energyUnits;
        this.lastingDays = lastingDays;
    }

    public String getImgPath() {
        return imgPath;
    }

    public int getEnergyUnits() {
        return energyUnits;
    }

    public int getLastingDays() {
        return lastingDays;
    }
}
