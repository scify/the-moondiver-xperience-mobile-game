package org.scify.moonwalker.app.actors;

import org.scify.engine.Renderable;
import org.scify.engine.UserInputHandler;

public class MoonWalkerPlayer extends Player implements Renderable {

    protected float x,y;

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
    public void setInputHandler(UserInputHandler userInputHandler) {

    }
}
