package org.scify.moonwalker.app.game;

public class MoonPhase {

    protected String imgPath;
    protected int energyUnits;

    public MoonPhase(String imgPath, int energyUnits) {
        this.imgPath = imgPath;
        this.energyUnits = energyUnits;
    }

    public String getImgPath() {
        return imgPath;
    }

    public int getEnergyUnits() {
        return energyUnits;
    }
}
