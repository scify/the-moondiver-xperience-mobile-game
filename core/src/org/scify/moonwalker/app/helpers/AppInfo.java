package org.scify.moonwalker.app.helpers;

import com.badlogic.gdx.math.Vector2;

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

    /**
     * Given a reference y in the reference screen world, translates it into the real y in the screen world.
     * @param initialY The reference (initial) y to translate.
     * @return The translated y.
     */
    public float convertY(float initialY) {
        int initialBackgroundHeight = REFERENCE_SCREEN_HEIGHT;
        float ret = getScreenHeight() * initialY;
        ret = ret / initialBackgroundHeight;
        return ret;
    }

    /**
     * Given a reference y in the reference screen world, translates it into the real y in the screen world, but taking
     * into account an inverted y axis (i.e. an axis where 0,0 is on the top left).
     * @param initialY The reference (initial) y to translate.
     * @return The translated y.
     */
    public float convertYInverted(float initialY) {
        return convertY(REFERENCE_SCREEN_HEIGHT - initialY);
    }

    /**
     * Given a reference x in the reference screen world, translates it into the real x in the screen world.
     * @param initialX The reference (initial) x to translate.
     * @return The translated x.
     */
    public  float convertX(float initialX) {
        int initialBackgroundWidth = REFERENCE_SCREEN_WIDTH;
        float ret = getScreenWidth() * initialX;
        ret = ret / initialBackgroundWidth;
        return ret;
    }

    /**
     * Returns the true position of the center of a box, given box dimensions and the target initial/reference center
     * coords.
     * @param initialWidth The box reference width.
     * @param initialHeight  The box reference height.
     * @param initialCenterX The box reference center x coord.
     * @param initialCenterY The box reference center y coord.
     * @return A {@link Vector2} position, which indicates where the box should be painted to be centered around the
     * translated center.
     */
    public Vector2 convertBoxCenter(float initialWidth, float initialHeight, float initialCenterX, float initialCenterY) {
        Vector2 res = new Vector2();
        res.x = convertX(initialCenterX - initialWidth / 2.0f);
        res.y = convertY(initialCenterY - initialHeight / 2.0f);

        return res;
    }

    /**
     * Returns the true position of the center of a box, given box dimensions and the target initial/reference center
     * coords. However, it uses an inverted y axis (i.e. a vector space where point 0,0 is at the upper left position).
     * @param initialWidth The box reference width.
     * @param initialHeight  The box reference height.
     * @param initialCenterX The box reference center x coord.
     * @param initialCenterY The box reference center y coord.
     * @return A {@link Vector2} position, which indicates where the box should be painted to be centered around the
     * translated center.
     */
    public Vector2 convertBoxCenterInvertedY(float initialWidth, float initialHeight, float initialCenterX, float initialCenterY) {
        Vector2 res = new Vector2();
        res.x = convertX(initialCenterX - initialWidth / 2.0f);
        res.y = convertYInverted(initialCenterY + initialHeight / 2.0f);

        return res;
    }
}
