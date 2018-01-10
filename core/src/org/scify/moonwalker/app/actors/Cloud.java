package org.scify.moonwalker.app.actors;

import org.scify.engine.Positionable;

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
}
