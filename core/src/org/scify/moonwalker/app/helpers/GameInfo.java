package org.scify.moonwalker.app.helpers;

import com.badlogic.gdx.Gdx;

public class GameInfo {
    private static GameInfo instance = new GameInfo();
    private static final String TAG = GameInfo.class.getName();

    public GameInfo() {
        Gdx.app.log(TAG, "Density: "+Gdx.graphics.getDensity());
        Gdx.app.log(TAG, "Screen width: "+getScreenWidth());
        Gdx.app.log(TAG, "Screen height: "+getScreenHeight());
        Gdx.app.log(TAG, "Aspect ratio: "+getAspectRatio());
    }

    private int screenWidth = Gdx.graphics.getWidth();
    private int screenHeight = Gdx.graphics.getHeight();

    // box2D (the physics engine) initially uses a 1 / 1 as pixel / meter ratio
    // so if an image is 80x80 pixels, it translates to 80x80 meters in the physics engine
    // the solution for that is to create a custom pixel to meter ratio
    // defining a pixel per meter value that will be used in the physics engine
    // 100 pixels will be equal to 1 meter.
    public static final int PPM = 100;

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
