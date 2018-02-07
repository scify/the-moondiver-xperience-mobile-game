package org.scify.moonwalker.app.helpers;

import com.badlogic.gdx.Gdx;

public class GameInfo {
    private static GameInfo instance = new GameInfo();
    private static final String TAG = GameInfo.class.getName();

    public void printInfo() {
        Gdx.app.log(TAG, "Density: "+Gdx.graphics.getDensity());
        Gdx.app.log(TAG, "Screen width: "+getScreenWidth());
        Gdx.app.log(TAG, "Screen height: "+getScreenHeight());
        Gdx.app.log(TAG, "Aspect ratio: "+getAspectRatio());
    }

    private int screenWidth = Gdx.graphics.getWidth();
    private int screenHeight = Gdx.graphics.getHeight();

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
        return (float)Gdx.graphics.getHeight()/(float) Gdx.graphics.getWidth();
    }


}
