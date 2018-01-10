package org.scify.engine;

public interface Renderable extends Positionable {
    String getType();
    void setInputHandler(UserInputHandler userInputHandler);
}
