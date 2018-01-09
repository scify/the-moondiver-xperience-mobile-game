package org.scify.moonwalker.app.actors;

public class Cloud implements Positionable {
    float x, y;
    public Cloud(float fX, float fY) {
        setX(fX);
        setY(fY);
    }

    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setX(float fX) {
        x = fX;
    }

    @Override
    public void setY(float fY) {
        y = fY;
    }

    @Override
    public float getXAxisVelocity() {
        return 0;
    }

    @Override
    public float getYAxisVelocity() {
        return 0;
    }

    @Override
    public void setXAxisVelocity(float xAxisVelocity) {

    }

    @Override
    public void setYAxisVelocity(float yAxisVelocity) {

    }


}
