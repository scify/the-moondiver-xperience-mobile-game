package org.scify.engine;

/**
 * Describes an object that can have substance in space (has a position and dimensions)
 */
public abstract class Positionable {
    protected float xPos;
    protected float yPos;
    protected float width;
    protected float height;
    protected int zIndex;
    protected boolean positionDrawable;

    public Positionable(float xPos, float yPos, float width, float height) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        positionDrawable = true;
    }

    public boolean isPositionDrawable() {
        return positionDrawable;
    }

    public void setPositionDrawable(boolean drawblePosition) {
        this.positionDrawable = drawblePosition;
    }

    public float getxPos() {
        return xPos;
    }

    public void setxPos(float xPos) {
        this.xPos = xPos;
    }

    public float getyPos() {
        return yPos;
    }

    public void setyPos(float yPos) {
        this.yPos = yPos;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getZIndex() {
        return zIndex;
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }
}
