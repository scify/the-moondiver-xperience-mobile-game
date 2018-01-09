package org.scify.moonwalker.app.actors;

public class MoonWalkerPlayer extends Player implements Renderable {

    protected float x,y;
    protected float xAxisVelocity = 0f;
    protected float yAxisVelocity = 0f;

    public MoonWalkerPlayer(String sName, float x, float y) {
        setName(sName);
        setX(x);
        setY(y);
    }

    @Override
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
    public String getType() {
        return "PLAYER";
    }

    @Override
    public float getXAxisVelocity() {
        return xAxisVelocity;
    }

    public void setXAxisVelocity(float xAxisVelocity) {
        this.xAxisVelocity = xAxisVelocity;
    }

    @Override
    public float getYAxisVelocity() {
        return yAxisVelocity;
    }

    public void setYAxisVelocity(float yAxisVelocity) {
        this.yAxisVelocity = yAxisVelocity;
    }
}
