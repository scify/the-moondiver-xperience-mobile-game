package org.scify.moonwalker.app.helpers;

public class AppInfo {

    public static final int REFERENCE_SCREEN_HEIGHT = 1080;
    public static final int REFERENCE_SCREEN_WIDTH = 1920;

    private static AppInfo instance = new AppInfo();
    private int screenWidth;
    private int screenHeight;
    private float screenDensity = 1;

    public static AppInfo getInstance() {
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
        return (float) getScreenHeight() / (float) getScreenWidth();
    }

    public void setScreenDensity(float screenDensity) {
        this.screenDensity = screenDensity;
    }

    public float getScreenDensity() { return screenDensity; }

    public float pixelsWithDensity(float pixels) {
        return pixels * screenDensity;
    }

    // TODO: Update all points in the game that use conversions

    public float convertY(float initialY) {
        int initialBackgroundHeight = REFERENCE_SCREEN_HEIGHT;
        float ret = getScreenHeight() * initialY;
        ret = ret / initialBackgroundHeight;
        return ret;
    }

    public  float convertX(float initialX) {
        int initialBackgroundWidth = REFERENCE_SCREEN_WIDTH;
        float ret = getScreenWidth() * initialX;
        ret = ret / initialBackgroundWidth;
        return ret;
    }
}
