package org.scify.moonwalker.app.helpers;

public class GameInfo {

    private static GameInfo instance = new GameInfo();
    private int screenWidth;
    private int screenHeight;
    private float screenDensity = 1;

    public static GameInfo getInstance() {
        return instance;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public float getAspectRatio() {
        return (float)getScreenHeight()/(float) getScreenWidth();
    }

    public void setScreenDensity(float screenDensity) {
        this.screenDensity = screenDensity;
    }

    public float pixelsWithDensity(float pixels) {
        return pixels * screenDensity;
    }
}
