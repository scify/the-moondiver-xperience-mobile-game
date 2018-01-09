package org.scify.moonwalker.app.actors;

public interface Positionable {
    public float getX();
    public float getY();

    public void setX(float fX);
    public void setY(float fY);

    public float getXAxisVelocity();
    public float getYAxisVelocity();

    public void setXAxisVelocity(float xAxisVelocity);
    public void setYAxisVelocity(float yAxisVelocity);

}
